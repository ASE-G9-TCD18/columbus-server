package com.group9.columbus.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.group9.columbus.entity.Preference;
import com.group9.columbus.exception.TripManagementException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group9.columbus.dto.TripDto;
import com.group9.columbus.entity.ApplicationUser;
import com.group9.columbus.entity.Conversation;
import com.group9.columbus.entity.Trip;
import com.group9.columbus.exception.IncorrectValueFormat;
import com.group9.columbus.exception.TripRequestedByUnAuthorizedUserException;
import com.group9.columbus.repository.TripRepository;

import static com.group9.columbus.enums.PreferenceType.GROUP_SIZE;

@Service
public class TripService {

	@Autowired
	UserManagementService userMgmtService;
	
	@Autowired
	ConversationService convService;
	
	@Autowired
	TripRepository tripRepo;

	Logger logger = Logger.getLogger(this.getClass());

//	@Autowired
//	TripValidatorService tripValidatorService;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

	/**
	 * Service method that creates a daily trip when requested by the user.
	 * @param loginId user login id
	 * @param tripDto  trip Dto
	 * @throws IncorrectValueFormat

	 */
	@Transactional
	public Trip createTrip(String loginId, TripDto tripDto) throws IncorrectValueFormat {
		Conversation conversation = convService.createConversation();
		ApplicationUser user = userMgmtService.findUserByUsername(loginId);

//		// Validate TripDto
//		tripValidatorService.validateTripCreationDetails(tripDto);

		
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
		trip.setConversation(conversation);


		trip = tripRepo.save(trip);
		
		// Set this trip to the user
		if(user.getTrips() == null || user.getTrips().isEmpty()) {
			List<Trip> trips = new ArrayList<>();
			trips.add(trip);
			user.setTrips(trips);
		} else {
			user.getTrips().add(trip);
		}
		
		user = userMgmtService.saveUser(user);
		return trip;
	}

	/**
	 * Return all the trips matching the criteria
	 * @param criteria the trip criteria
	 * @return all tripId
	 */
	public List<Trip> searchTrip(List<Preference> criteria){

		return null;
	}

	/** This method returns the trip for the logged in user by tripId
	 * @param loginId user login id
	 * @param tripId  trip id
	 * @throws TripRequestedByUnAuthorizedUserException 
	 */
	public Trip getTripById(String loginId, String tripId) throws TripRequestedByUnAuthorizedUserException {
		Trip trip = tripRepo.findByTripId(tripId);
		
		// Check if the trip details has been requested by a user
		// who is originally not part of the trip
		if(trip != null && !trip.getTripUsersLoginIds().contains(loginId)) {
			throw new TripRequestedByUnAuthorizedUserException("You do not have the permissions to request"
					+ "details for this trip.");
		}
		
		return trip;
	}

	/** This method returns the trip for the logged in user by tripId
	 * @param tripId trip id
	 * @throws TripRequestedByUnAuthorizedUserException when trip isn't created by the user
	 */
	public Trip getTripById(String tripId) throws TripRequestedByUnAuthorizedUserException {
		Trip trip = tripRepo.findByTripId(tripId);
		return trip;
	}
	
	/**
	 * This method return all the trips that the user is a part of.
	 * @param loginId user login id
	 * @return all the trips that user has joined.
	 */
	public List<Trip> getTripsByLoginId(String loginId) {
		List<Trip> trips = tripRepo.findByTripUsersLoginIds(loginId);
		return trips;
	}

	/**
	 * User tried to request joining a daily trip.
	 * @param loginId user login id
	 * @param tripId  trip id
	 */
	public void requestJoinTrip(String loginId, String tripId){
		/*
		* Send notification to specific
		*/

	}

	/**
	 * User Joins into a trip or approved by the group admin
	 * @param loginId user login id
	 * @param tripId  trip id
	 * @return TripId
	 */
	public void joinTrip(String loginId, String tripId) throws Exception {
		Trip trip = tripRepo.findByTripId(tripId);
		if(trip==null) {
			throw new TripManagementException("Trip doesn't exist with id: " + tripId);
		}
		if(!isFull(trip)){
			List<String> ids = trip.getTripUsersLoginIds();
			ids.add(loginId);
			tripRepo.save(trip);
		}
		else{
			throw new Exception("Full Trip!!!!");
		}
	}
	
	
	/**
	 * Generates a unique tripId which is a combination of yyMMdd + 4 random digits.
	 * @return trip id
	 */
	private String createTripId() {
		String date = sdf.format(new Date());
		String tripId = date + String.format("%04d", new Random().nextInt(10000));
		return tripId;
	}

	private Integer getTripCapacity(Trip trip) throws Exception {
		for(Preference pref : trip.getPreferences()){
			if(pref.getPreferenceType()==GROUP_SIZE){
				return (Integer)pref.getValue();
			}
		}
		throw new Exception("No Preference as "+GROUP_SIZE+" found in trip");
	}

	private boolean isFull(Trip trip) throws Exception {
		return getTripCapacity(trip)< trip.getTripUsersLoginIds().size();
	}
}
