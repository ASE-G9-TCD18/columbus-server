package com.group9.columbus.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.group9.columbus.dto.TripDto;
import com.group9.columbus.entity.Trip;
import com.group9.columbus.service.SearchService;
import com.group9.columbus.utils.CommonUtils;
import com.group9.columbus.utils.JsonUtils;

/**
 * Container for search APIs.
 * @author amit
 */
@RestController
@RequestMapping(value = "/search")
public class SearchController {
	
	private Logger logger = Logger.getLogger(SearchController.class);

	@Autowired
	private CommonUtils commonUtils;
	
	
	@Autowired
	private SearchService searchService;
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> searchTrip(@Validated @RequestBody TripDto tripDto) {
		String loginId = commonUtils.getLoggedInUserLoginId();
		List<Trip> trips = searchService.searchTrips(loginId, tripDto);
		return JsonUtils.getJsonForResponse(trips);
	}

}
