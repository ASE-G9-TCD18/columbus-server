package com.group9.columbus.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.group9.columbus.entity.Preference;
import com.group9.columbus.exception.TripManagementException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.group9.columbus.comparator.DestinationDistanceComparator;
import com.group9.columbus.dto.TripAndDistanceDto;
import com.group9.columbus.dto.TripDto;
import com.group9.columbus.dto.TripJoinRequestDto;
import com.group9.columbus.entity.ApplicationUser;
import com.group9.columbus.entity.Conversation;
import com.group9.columbus.entity.Trip;
import com.group9.columbus.entity.TripStop;
import com.group9.columbus.exception.IncorrectValueFormat;
import com.group9.columbus.exception.TripRequestedByUnAuthorizedUserException;
import com.group9.columbus.repository.TripRepository;
import com.group9.columbus.utils.GreatCircleDistance;

import static com.group9.columbus.enums.PreferenceType.GROUP_MAX_SIZE;

/**
 * Service class for {@link Trip}.
 * @author amit
 */
@Service
public class TripService {

	Logger logger = Logger.getLogger(TripService.class);

	@Autowired
	private UserManagementService userMgmtService;

	@Autowired
	private ConversationService convService;

	@Autowired
	private TripRepository tripRepo;

	@Autowired
	private NotificationService notifService;

	@Autowired
	private TripValidatorService tripValidatorService;

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

	/**
	 * Service method that creates a daily trip when requested by the user.
	 * 
	 * @param loginId
	 *            user login id
	 * @param tripDto
	 *            trip Dto
	 * @throws IncorrectValueFormat
	 * 
	 */
	@Transactional
	public Trip createTrip(String loginId, TripDto tripDto) throws IncorrectValueFormat {
		Conversation conversation = convService.createConversation();
		ApplicationUser user = userMgmtService.findUserByUsername(loginId);

		// Validate TripDto
		tripValidatorService.validateTripCreationDetails(tripDto);

		List<String> appUsers = new ArrayList<>();
		appUsers.add(user.getLoginId());

		// Create a new Trip and set attributes
		Trip trip = new Trip();
		trip.setTripId(createTripId());
		trip.setTripType(tripDto.getTripType());
		trip.setPreferences(tripDto.getPreferences());
		trip.setAdmin(user.getLoginId());
		trip.setTripUsersLoginIds(appUsers);
		trip.setTripStops(tripDto.getTripStops());
		trip.setTripRating(user.getUserRating());
		trip.setConversation(conversation);

		trip = tripRepo.save(trip);

		// Set this trip to the user
		if (user.getTrips() == null || user.getTrips().isEmpty()) {
			List<Trip> trips = new ArrayList<>();
			trips.add(trip);
			user.setTrips(trips);
		} else {
			user.getTrips().add(trip);
		}

		userMgmtService.saveUser(user);
		return trip;
	}

	/**
	 * This method returns the trip for the logged in user by tripId
	 * 
	 * @param loginId
	 *            user login id
	 * @param tripId
	 *            trip id
	 * @throws TripRequestedByUnAuthorizedUserException
	 */
	public Trip getTripById(String loginId, String tripId) throws TripRequestedByUnAuthorizedUserException {
		Trip trip = tripRepo.findByTripId(tripId);

		// Check if the trip details has been requested by a user
		// who is originally not part of the trip
		if (trip != null && !trip.getTripUsersLoginIds().contains(loginId)) {
			throw new TripRequestedByUnAuthorizedUserException(
					"You do not have the permissions to request" + "details for this trip.");
		}

		return trip;
	}

	/**
	 * This method returns the trip
	 * 
	 * @param tripId
	 *            trip id
	 */
	public Trip getTripById(String tripId) {
		Trip trip = tripRepo.findByTripId(tripId);
		return trip;
	}

	/**
	 * This method return all the trips that the user is a part of.
	 * 
	 * @param loginId
	 *            user login id
	 * @return all the trips that user has joined.
	 */
	public List<Trip> getTripsByLoginId(String loginId) {
		List<Trip> trips = tripRepo.findByTripUsersLoginIds(loginId);
		return trips;
	}

	/**
	 * This method return all the trips for all users.
	 * 
	 * @return all the trips.
	 */
	public List<Trip> getAllTrips() {
		List<Trip> trips = tripRepo.findByTripType("SCHEDULED");
		logger.warn("TRIPS:" + trips);
		return trips;
	}

	/**
	 * User tried to request joining a daily trip.
	 * 
	 * @param loginId
	 *            user login id
	 * @param tripId
	 *            trip id
	 * @throws TripManagementException
	 */
	@Transactional
	public Trip requestJoinTrip(String loginId, String tripId) throws TripManagementException {

		boolean sentNotif = false;

		Trip trip = getTripById(tripId);
		ApplicationUser requestFrom = userMgmtService.findUserByUsername(loginId);
		ApplicationUser requestTo = userMgmtService.findUserByUsername(trip.getAdmin());

		// TODO: Checks for group capacity

		// TODO: Check if join request already sent

		// If join request notification send successfully to admin then
		// persist to database
		if (notifService.sendNewJoinRequestNotification(requestTo.getDeviceId(), requestFrom.getLoginId())) {
			sentNotif = true;
		}

		logger.info("Notification to join trip by (" + requestFrom.getLoginId() + ") sent to (" + requestTo.getLoginId()
				+ ").");

		// Add this trip to the list of trips whose join request is pending
		requestFrom = setTripJoinReqForJoinee(requestFrom, trip);

		// Add this request to list of requests in the admin
		TripJoinRequestDto tripJoinRequestDto = new TripJoinRequestDto(requestFrom.getLoginId(), requestTo.getLoginId(),
				trip);
		requestTo = setTripJoinReqForAdmin(requestTo, tripJoinRequestDto);

		// TODO discuss whether join request is a trip specific thing or user specific
		// Save the user
		userMgmtService.saveUser(requestFrom, requestTo);

		if (!sentNotif) {
			String message = "Unable to send notification. User: " + loginId + " successfully joined the trip: "
					+ tripId;
			logger.error(message);
		}

		return trip;
	}

	/**
	 * Accepts trip join request
	 * 
	 * @param adminLoginId
	 * @param tripJoinRequest
	 * @throws TripRequestedByUnAuthorizedUserException
	 */
	@Transactional
	public void acceptJoinTrip(String adminLoginId, TripJoinRequestDto tripJoinRequest)
			throws TripRequestedByUnAuthorizedUserException {

		if (!adminLoginId.equals(tripJoinRequest.getTrip().getAdmin())) {
			throw new TripRequestedByUnAuthorizedUserException(
					"You do not have sufficient permissions to " + "accept this request");
		}

		// update the joinee info about the trip
		ApplicationUser user = userMgmtService.findUserByUsername(tripJoinRequest.getRequestFrom());

		// Some other logic...
		Trip trip = tripRepo.findByTripId(tripJoinRequest.getTrip().getTripId());
		// Add the user to the Trip
		trip.getTripUsersLoginIds().add(tripJoinRequest.getRequestFrom());
		int n = trip.getTripUsersLoginIds().size();

		double tripRating = 0.0;
		if (trip.getTripRating() != null)
			tripRating = trip.getTripRating();

		double userRating = user.getUserRating();
		trip.setTripRating(getRunningMean(n, tripRating, userRating));
		trip = tripRepo.save(trip);

		// add the trip
		if (user.getTrips() == null) {
			user.setTrips(new ArrayList<Trip>());
		}
		user.getTrips().add(trip);

		// remove trip request
		List<Trip> reqTrips = user.getTripsRequestsMade();
		for (Iterator<Trip> iter = reqTrips.listIterator(); iter.hasNext();) {
			Trip reqTrip = iter.next();
			if (reqTrip.getTripId().equals(trip.getTripId())) {
				iter.remove();
			}
		}
		userMgmtService.saveUser(user);

		// remove the request from admin
		ApplicationUser admin = userMgmtService.findUserByUsername(adminLoginId);

		List<TripJoinRequestDto> joinReqDtos = admin.getTripsRequestsAwaitingConfirmation();
		for (Iterator<TripJoinRequestDto> iter = joinReqDtos.listIterator(); iter.hasNext();) {

			TripJoinRequestDto joinReqDto = iter.next();
			if (joinReqDto.getTrip().getTripId().equals(tripJoinRequest.getTrip().getTripId())) {
				iter.remove();
			}

		}
		userMgmtService.saveUser(admin);

		// Update User Rating in accordance with Trip Rating
		userMgmtService.updateUserRating(trip.getTripRating(), trip.getTripUsersLoginIds());

	}

	// TODO: Optimize this later to get limited details only
	public List<Trip> getTripsNearToDestination(TripStop dest, TravelMode travelMode) {

		List<Trip> alltrips = tripRepo.findAllTrips();
		List<TripAndDistanceDto> distanceList = new ArrayList<>();

		for (Trip trip : alltrips) {
			LatLng tripDest = trip.getTripStops().get(1).getCoordinate();
			LatLng ownDest = dest.getCoordinate();

			GreatCircleDistance gcd = new GreatCircleDistance(tripDest, ownDest);

			distanceList.add(new TripAndDistanceDto(trip, gcd.getDistance()));

		}

		distanceList.sort(new DestinationDistanceComparator());

		return getTopKTrip(20, distanceList);
	}

	/**
	 * 
	 * Adds the trip join request in the list of pending requests of Admin.
	 * 
	 * @param admin
	 *            admin
	 * @param tripJoinRequestDto
	 * @return Applicationuser admin
	 */
	private ApplicationUser setTripJoinReqForAdmin(ApplicationUser admin, TripJoinRequestDto tripJoinRequestDto) {
		List<TripJoinRequestDto> pendingRequests = admin.getTripsRequestsAwaitingConfirmation();
		if (pendingRequests == null) {
			pendingRequests = new ArrayList<>();
		}
		pendingRequests.add(tripJoinRequestDto);
		admin.setTripsRequestsAwaitingConfirmation(pendingRequests);
		return admin;
	}

	/**
	 * Add the trip join request in the list of request in the joinee.
	 * 
	 * @param joinee
	 * @param trip
	 * @return ApplicationUser joinee
	 */
	private ApplicationUser setTripJoinReqForJoinee(ApplicationUser joinee, Trip trip) {
		List<Trip> tripJoinRequest = joinee.getTripsRequestsMade();
		if (tripJoinRequest == null) {
			tripJoinRequest = new ArrayList<>();
		}
		tripJoinRequest.add(trip);
		joinee.setTripsRequestsMade(tripJoinRequest);
		return joinee;
	}

	/**
	 * User Joins into a trip or approved by the group admin
	 * 
	 * @param loginId
	 *            user login id
	 * @param tripId
	 *            trip id
	 * @return TripId
	 */
	public void joinTrip(String loginId, String tripId) throws TripManagementException {
		Trip trip = tripRepo.findByTripId(tripId);
		if (trip == null) {
			throw new TripManagementException("Trip doesn't exist with id: " + tripId);
		}
		if (!isFull(trip)) {
			List<String> ids = trip.getTripUsersLoginIds();
			ids.add(loginId);
			tripRepo.save(trip);
		} else {
			throw new TripManagementException("Trip " + tripId + " is full.");
		}
	}

	/**
	 * Service method that deletes the trip if the logged in user is the trip owner.
	 * 
	 * @param adminLoginId
	 * @param tripId
	 * @return
	 * @throws TripRequestedByUnAuthorizedUserException
	 */
	@Transactional
	public boolean deleteTrip(String adminLoginId, String tripId)
			throws TripRequestedByUnAuthorizedUserException, TripManagementException {

		Trip trip = getTripById(tripId);

		if (trip == null)
			throw new TripManagementException("No such trip found with id (" + tripId + ") found!");

		if (!adminLoginId.equals(trip.getAdmin())) {
			throw new TripRequestedByUnAuthorizedUserException(
					"You do not have sufficient permissions to " + "accept this request");
		}

		// Delete the join requests made by all users
		userMgmtService.deleteTripRequestsInUsers(trip);

		// Delete the acceptance requests to the admin
		userMgmtService.deleteTripAccRequestsByAdmin(trip);

		// Delete the trip in all users
		List<String> tripUsers = trip.getTripUsersLoginIds();
		for (String tripUser : tripUsers) {
			removeTripFromUser(tripUser, trip);
		}

		// Delete the trip
		tripRepo.delete(trip.getId());

		return true;
	}

	
	/**
	 * Service method that checks if the trip is existent and then deletes the user from trip as well as the trip
	 * from the user.
	 * @param loginId
	 * @param tripId
	 * @throws TripManagementException
	 */
	@Transactional
	public void leaveTrip(String loginId, String tripId) throws TripManagementException {
		Trip trip = getTripById(tripId);

		if (trip == null)
			throw new TripManagementException("No such trip found with id (" + tripId + ") found!");

		ApplicationUser user = userMgmtService.findUserByUsername(loginId);

		// Remvoe the trip from the user.
		removeTripFromUser(loginId, trip);
		
		// Remove the user from the trip.
		removeUserFromTrip(loginId, trip);

	}

	public List<Trip> getAllCreatedTrips(String adminLoginId) {
		List<Trip> trips = tripRepo.findByAdmin(adminLoginId);
		return trips;
	}

	
	/**
	 * Generates a unique tripId which is a combination of yyMMdd + 4 random digits.
	 * 
	 * @return trip id
	 */
	private String createTripId() {
		String date = sdf.format(new Date());
		String tripId = date + String.format("%04d", new Random().nextInt(10000));
		return tripId;
	}

	private Integer getTripCapacity(Trip trip) {
		for (Preference pref : trip.getPreferences()) {
			if (pref.getPreferenceType() == GROUP_MAX_SIZE) {
				return (Integer) pref.getValue();
			}
		}
		throw new RuntimeException("No Preference as " + GROUP_MAX_SIZE + " found in trip");
	}
	

	/**
	 * Check if trip is full or not.
	 * 
	 * @param trip
	 *            trip instance
	 * @return full or not
	 * @throws Exception
	 */
	private boolean isFull(Trip trip) {
		return getTripCapacity(trip) < trip.getTripUsersLoginIds().size() + 1;
	}
	

	/**
	 * Helper method that extracts the {@link Trip} 's from {@link TripAndDistanceDto} 's and returns the 
	 * top K {@link Trip}s.
	 * @param k
	 * @param tripAndDistanceDtos
	 * @return
	 */
	private List<Trip> getTopKTrip(int k, List<TripAndDistanceDto> tripAndDistanceDtos) {
		List<Trip> trips = new ArrayList<>();
		if (k > tripAndDistanceDtos.size())
			k = tripAndDistanceDtos.size();

		for (int i = 0; i < k; i++) {
			trips.add(tripAndDistanceDtos.get(i).getTrip());
		}

		return trips;
	}

	/**
	 * Helper method to calculate the running mean.
	 * @param n
	 * @param mean
	 * @param a
	 * @return
	 */
	private double getRunningMean(int n, double mean, double a) {
		try {

			return (mean * n + a) / (n + 1);
		} catch (Exception e) {
			return 0.0;
		}
	}
	

	/**
	 * Helper method that removes the trip from a user with loginId.
	 * @param loginId
	 * @param trip
	 */
	private void removeTripFromUser(String loginId, Trip trip) {
		ApplicationUser appUser = userMgmtService.findUserByUsername(loginId);

		List<Trip> trips = appUser.getTrips();
		for (Iterator<Trip> iter = trips.listIterator(); iter.hasNext();) {
			Trip t = iter.next();

			if (t.getTripId().equals(trip.getTripId())) {
				iter.remove();
			}
		}
		userMgmtService.saveUser(appUser);
	}
	

	/**
	 * Helper method that removes the user with loginid from the trip.
	 * @param loginId
	 * @param trip
	 */
	private void removeUserFromTrip(String loginId, Trip trip) {

		List<String> tripUsers = trip.getTripUsersLoginIds();

		if (tripUsers != null && tripUsers.size() != 0) {

			for (Iterator<String> iter = tripUsers.listIterator(); iter.hasNext();) {
				String tripUser = iter.next();

				if (tripUser.equals(loginId)) {
					iter.remove();
				}
			}
			tripRepo.save(trip);
		}
		
	}
}
