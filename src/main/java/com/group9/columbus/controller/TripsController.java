package com.group9.columbus.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.group9.columbus.entity.Trip;
import com.group9.columbus.repository.UserRepository;
import com.group9.columbus.service.TripService;
import com.group9.columbus.utils.CommonUtils;
import com.group9.columbus.utils.JsonUtils;

@RestController
@RequestMapping(value = "/trips")
public class TripsController {

	Logger logger = Logger.getLogger(TripsController.class);
	
	@Autowired
	CommonUtils commonUtils;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	TripService tripService;
	
	/**
	 * Method returns all trips for this particular user
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getAllTrips() {
		String loginId = commonUtils.getLoggedInUserLoginId();
		
		List<Trip> trips = tripService.getTripsByLoginId(loginId);
		logger.info("Request for all trips for user ("+loginId+") processed.");
		
		return JsonUtils.getJsonForResponse(trips);
	}
	

	/**
	 * Method that returns all {@link Trip}'s.
	 * @return
	 */
	@RequestMapping(path = "/all", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getSuperAllTrips() {
		List<Trip> trips = tripService.getAllTrips();
		logger.info("Request for all trips for all users");

		return JsonUtils.getJsonForResponse(trips);
	}
	
	
	@RequestMapping(path = "/created", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getAllCreatedTrips() {
		String loginId = commonUtils.getLoggedInUserLoginId();
		
		List<Trip> trips = tripService.getAllCreatedTrips(loginId);
		logger.info("Request for all created trips for logged in user ("+loginId+").");

		return JsonUtils.getJsonForResponse(trips);
	}
	
}

