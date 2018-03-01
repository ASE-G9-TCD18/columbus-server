package com.group9.columbus.controller;

import java.awt.Point;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.group9.columbus.dto.TripDto;
import com.group9.columbus.entity.ApplicationUser;
import com.group9.columbus.entity.Trip;
import com.group9.columbus.service.TripService;
import com.group9.columbus.utils.JsonUtils;

/**
 * This class has Api for Trip CURD operations.
 * @author amit
 */
@RestController
@RequestMapping(value = "/trip/{loginId}")
public class TripController {

	@Autowired
	private TripService tripService;

	/**
	 * API to create a trip
	 * @param loginId
	 * @param tripDto
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> createTrip(@PathVariable("loginId") String loginId,
			@Validated @RequestBody TripDto tripDto) {

		// forward this request to the TripService
		Trip trip = tripService.createTrip(loginId, tripDto);

		return JsonUtils.getJsonForResponse(trip);

	}

	@RequestMapping(path = "/{tripId}", method = RequestMethod.GET, produces = "application/json")
	public void getTrip(@PathVariable("loginId") String loginId, @Validated @RequestBody ApplicationUser user) {

	}
	
	
	@RequestMapping(path = "/temp", method = RequestMethod.GET, produces = "application/json")
	public Point testGeo() {
		Point pt = new Point();
		pt.setLocation(45, 45);
		return pt;
	}
}
