package com.group9.columbus.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.group9.columbus.dto.TripDto;
import com.group9.columbus.entity.ApplicationUser;

@RestController
@RequestMapping(value = "/trip/{loginId}")
public class TripController {
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public void createTrip(@PathVariable("loginId") String loginId, @Validated @RequestBody TripDto user) {
		
	}

	@RequestMapping(path = "/{tripId}",method = RequestMethod.GET, produces = "application/json")
	public void getTrip(@PathVariable("loginId") String loginId, @Validated @RequestBody ApplicationUser user) {
		
	}
}
