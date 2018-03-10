package com.group9.columbus.controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.group9.columbus.entity.Criteria;
import com.group9.columbus.entity.Preference;
import com.group9.columbus.enums.PreferenceType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.group9.columbus.dto.TripDto;
import com.group9.columbus.entity.Trip;
import com.group9.columbus.exception.IncorrectValueFormat;
import com.group9.columbus.exception.TripManagementException;
import com.group9.columbus.service.TripService;
import com.group9.columbus.utils.CommonUtils;
import com.group9.columbus.utils.JsonUtils;

/**
 * This class has Api for Trip CURD operations.
 * 
 * @author amit
 */
@RestController
@RequestMapping(value = "/trip")
public class TripController {

	private Logger logger = Logger.getLogger(TripController.class);

	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	private TripService tripService;

	/**
	 * API to create a trip
	 *
	 * @param tripDto
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> createTrip(@Validated @RequestBody TripDto tripDto) {
		String loginId = commonUtils.getLoggedInUserLoginId();

		// forward this request to the TripService
		Trip trip;
		try {
			trip = tripService.createTrip(loginId, tripDto);
			return JsonUtils.getJsonForResponse(trip);
		} catch (IncorrectValueFormat e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(CommonUtils.createErrorResponseMessage(e.getMessage()));
		}
	}

	/**
	 * API that returns Trip for currently logged in User.
	 * @param tripId
	 * @return
	 */
	@RequestMapping(path = "/{tripId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getTrip(@PathVariable("tripId") String tripId) {
		String loginId = commonUtils.getLoggedInUserLoginId();
		Trip trip = null;
		
		try {
			trip = tripService.getTripById(loginId, tripId);
			logger.info("Request for trip details for tripId ("+tripId+") by ("+loginId+") processed successfully.");
			
		} catch (TripManagementException tme) {
			logger.error(tme);
			CommonUtils.createErrorResponseMessage(tme.getMessage());
		}
		
		return JsonUtils.getJsonForResponse(trip);
	}

	@RequestMapping(path = "/{tripId}/join", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> joinTripByTripId(@PathVariable("tripId") String tripId) {
		String loginId = commonUtils.getLoggedInUserLoginId();

		logger.info("Request received to join trip ("+tripId+") by ("+loginId+").");
		try {
			tripService.requestJoinTrip(loginId, tripId);
			logger.info("Request to join ("+tripId+") by ("+loginId+") processed successfully.");
		}
		catch(Exception ex){
			logger.error(ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(CommonUtils.createErrorResponseMessage(ex.getMessage()));
		}
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(CommonUtils.createResponseMessage("User: "+loginId+" successfully joined the trip: "+tripId));
	}


	@RequestMapping(path="/criteria", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<List<Preference>> getCriteria(){
		List<Preference> prefs= new ArrayList<>();
		Preference<Integer> sizePref = new Preference<>(PreferenceType.GROUP_SIZE, 10);
		prefs.add(sizePref);
		return ResponseEntity.ok(prefs);
	}

//	@RequestMapping(path="/criteria", method=RequestMethod.POST, produces="application/json")
//	public ResponseEntity<Criteria> getCriteria(@RequestBody Criteria criteria){
//
//		return ResponseEntity.ok(criteria);
//
//	}

	@RequestMapping(path = "/temp", method = RequestMethod.GET, produces = "application/json")
	public Point testGeo() {
		Point pt = new Point();
		pt.setLocation(45, 45);
		return pt;
	}
}
