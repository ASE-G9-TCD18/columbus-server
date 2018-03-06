package com.group9.columbus.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

@Service
public class TripService {

	@Autowired
	UserManagementService userMgmtService;
	
	@Autowired
	ConversationService convService;
	
	@Autowired
	TripRepository tripRepo;
	
	@Autowired
	TripValidatorService tripValidatorService;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
	
	/**
	 * Service method that creates a daily trip when requested by the user.
	 * @param loginId
	 * @param user
	 * @throws IncorrectValueFormat 
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
	
	/** This method returns the trip for the logged in user by tripId
	 * @param loginId
	 * @param tripId
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
	
	/**
	 * This method return all the trips that the user is a part of.
	 * @param loginId
	 * @return
	 */
	public List<Trip> getTripsByLoginId(String loginId) {
		List<Trip> trips = tripRepo.findByTripUsersLoginIds(loginId);
		return trips;
	}
	
	public Trip joinTripByTripId(String loginId, String tripId) {
		
		// Validate if the trip hasn't reached maximum capacity
		
		// If all okay then send notif to Trip Admin to accept this request
		// Trip admin can view all the necessary details of this user.
		
		
		return null;
	}
	
	
	/**
	 * Generates a unique tripId which is a combination of yyMMdd + 4 random digits.
	 * @return
	 */
	private String createTripId() {
		String date = sdf.format(new Date());
		String tripId = date + String.format("%04d", new Random().nextInt(10000));
		return tripId;
	}
}
