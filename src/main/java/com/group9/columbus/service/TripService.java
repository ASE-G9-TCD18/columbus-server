package com.group9.columbus.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.group9.columbus.entity.Preference;
import com.group9.columbus.exception.TripManagementException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.maps.model.DistanceMatrix;
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

	@Autowired
	private GpsService gpsService;

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
	 * Return all the trips matching the criteria
	 * 
	 * @param criteria
	 *            the trip criteria
	 * @return all tripId
	 */
	public List<Trip> searchTrip(List<Preference> criteria) {

		return null;
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
	public void requestJoinTrip(String loginId, String tripId) throws TripManagementException {

		Trip trip = getTripById(tripId);
		ApplicationUser requestFrom = userMgmtService.findUserByUsername(loginId);
		ApplicationUser requestTo = userMgmtService.findUserByUsername(trip.getAdmin());

		// TODO: Checks for group capacity

		// TODO: Check if join request already sent

		// If join request notification send successfully to admin then
		// persist to database
		if (notifService.sendNewJoinRequestNotification(requestTo.getDeviceId(), requestFrom.getLoginId())) {
			logger.info("Notification to join trip by (" + requestFrom.getLoginId() + ") sent to ("
					+ requestTo.getLoginId() + ").");

			// Add this trip to the list of trips whose join request is pending
			requestFrom = setTripJoinReqForJoinee(requestFrom, trip);

			// Add this request to list of requests in the admin
			TripJoinRequestDto tripJoinRequestDto = new TripJoinRequestDto(requestFrom.getLoginId(),
					requestTo.getLoginId(), trip);
			requestTo = setTripJoinReqForAdmin(requestTo, tripJoinRequestDto);

			// TODO discuss whether join request is a trip specific thing or user specific
			// Save the user
			userMgmtService.saveUser(requestFrom, requestTo);
		} else {
			logger.error("Unable to send notification. Check error log of NotificationService.");
			throw new TripManagementException("Unable to complete request. Please try again later.");
		}
	}
	
	/**
	 * Accepts trip join request
	 * @param loginId
	 * @param tripId
	 * @throws TripRequestedByUnAuthorizedUserException 
	 */
	@Transactional
	public void acceptJoinTrip(String adminLoginId, TripJoinRequestDto tripJoinRequest) 
			throws TripRequestedByUnAuthorizedUserException {
		
		if(!adminLoginId.equals(tripJoinRequest.getTrip().getAdmin())) {
			throw new TripRequestedByUnAuthorizedUserException("You do not have sufficient permissions to "
					+ "accept this request");
		}
		
		// update the joinee info about the trip
		ApplicationUser user = userMgmtService.findUserByUsername(tripJoinRequest.getRequestFrom());
		
		// Some other logic...
		Trip trip = tripRepo.findByTripId(tripJoinRequest.getTrip().getTripId());
		// Add the user to the Trip
		trip.getTripUsersLoginIds().add(tripJoinRequest.getRequestFrom());
		trip.setTripRating(getRunningMean(trip.getTripUsersLoginIds().size(), 
				trip.getTripRating(), user.getUserRating()));
		trip = tripRepo.save(trip);
		
		
		// add the trip
		if(user.getTrips() == null) {
			user.setTrips(new ArrayList<Trip>());
		}
		user.getTrips().add(trip);
		
		// remove trip request 
		for (Trip reqTrip : user.getTripsRequestsMade()) {
			if(reqTrip.getTripId().equals(trip.getTripId())) {
				user.getTripsRequestsMade().remove(reqTrip);
				break;
			}
		}
		userMgmtService.saveUser(user);
		
		
		// remove the request from admin
		ApplicationUser admin = userMgmtService.findUserByUsername(adminLoginId);
		
		for (TripJoinRequestDto joinReqDto : admin.getTripsRequestsAwaitingConfirmation()) {
			if(joinReqDto.getTrip().getTripId().equals(tripJoinRequest.getTrip().getTripId())) {
				admin.getTripsRequestsAwaitingConfirmation().remove(joinReqDto);
				break;
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
		
		for (Trip trip : alltrips ) {
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
	 * @param Applicationuser
	 *            admin
	 * @param TripJoinRequestDto
	 *            tripJoinRequest
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
	 * @param ApplicationUser
	 *            joinee
	 * @param Trip
	 *            trip
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
	 * @param loginId
	 * @param tripId
	 * @return
	 * @throws TripRequestedByUnAuthorizedUserException 
	 */
	public boolean deleteTrip(String adminLoginId, String tripId) throws TripRequestedByUnAuthorizedUserException {

		Trip trip = getTripById(tripId);
		if(!adminLoginId.equals(trip.getAdmin())) {
			throw new TripRequestedByUnAuthorizedUserException("You do not have sufficient permissions to "
					+ "accept this request");
		}
		
		// Delete the join requests made by all users
		
		// Delete the acceptance requests to the admin
		
		// Delete the trip in all users
		List<String> tripUsers = trip.getTripUsersLoginIds();
		for(String tripUser : tripUsers) {
			
		}
		
		// Delete the trip
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
	
	
	private List<Trip> getTopKTrip(int k, List<TripAndDistanceDto> tripAndDistanceDtos) {
		List<Trip> trips = new ArrayList<>();
		if(k > tripAndDistanceDtos.size())
			k = tripAndDistanceDtos.size();
		
		for(int i = 0; i < k; i++) {
			trips.add(tripAndDistanceDtos.get(i).getTrip());
		}
		
		return trips;
	}

	private double getRunningMean(int n, double mean, double a) {
		
		return 0;
	}
}
