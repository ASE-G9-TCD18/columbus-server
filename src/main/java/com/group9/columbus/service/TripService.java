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
import com.group9.columbus.repository.TripRepository;

@Service
public class TripService {

	@Autowired
	UserManagementService userMgmtService;
	
	@Autowired
	ConversationService convService;
	
	@Autowired
	TripRepository tripRepo;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
	
	/**
	 * Service method that creates a daily trip when requested by the user.
	 * @param loginId
	 * @param user
	 */
	@Transactional
	public Trip createTrip(String loginId, TripDto tripDto) {
		Conversation conversation = convService.createConversation();
		ApplicationUser user = userMgmtService.findUserByUsername(loginId);
		
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
	
	
	private String createTripId() {
		String date = sdf.format(new Date());
		String tripId = date + String.format("%04d", new Random().nextInt(10000));
		return tripId;
	}
}
