package com.group9.columbus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.group9.columbus.entity.ApplicationUser;
import com.group9.columbus.repository.TripRepository;
import com.group9.columbus.repository.UserRepository;
import com.group9.columbus.utils.JsonUtils;



@RestController
@RequestMapping(value = "/trips/{loginId}")
public class TripsController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	TripRepository tripRepository;
	
	/**
	 * Method returns all trips for this particular user
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getAllTrips() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginId = (String)auth.getPrincipal();
		
		// fetch user from database
		ApplicationUser user = userRepository.findByLoginId(loginId);
		
		return JsonUtils.getJsonForResponse(tripRepository.findByTripUsersId(user.getId()));
	}
}
