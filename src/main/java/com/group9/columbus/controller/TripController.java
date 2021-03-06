package com.group9.columbus.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.group9.columbus.dto.TripJoinRequestDto;
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
			Trip trip = tripService.requestJoinTrip(loginId, tripId);
			logger.info("Request to join ("+tripId+") by ("+loginId+") processed successfully.");
			
			return JsonUtils.getJsonForResponse(trip);
		}
		catch(Exception ex){
			logger.error(ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(CommonUtils.createErrorResponseMessage(ex.getMessage()));
		}
	}

	@RequestMapping(path = "/{tripId}/accept", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> acceptTripJoinRequest(@PathVariable("tripId") String tripId, 
			@RequestBody TripJoinRequestDto tripJoinRequest) {
		String loginId = commonUtils.getLoggedInUserLoginId();

		logger.info("Request received to accept join trip ("+tripId+") by ("+loginId+").");
		try {
			tripService.acceptJoinTrip(loginId, tripJoinRequest);
			logger.info("Request to accept join trip ("+tripId+") by ("+loginId+") processed successfully.");
		}
		catch(Exception ex){
			logger.error(ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(CommonUtils.createErrorResponseMessage(ex.getMessage()));
		}
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(CommonUtils.createResponseMessage("Accepted join request for trip: "+tripId+" by "
		+tripJoinRequest.getRequestFrom()));
	}

	@RequestMapping(path="/criteria", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<List<Preference>> getCriteria(){
		List<Preference> prefs= new ArrayList<>();
		Preference<Integer> minSize = new Preference<>(PreferenceType.GROUP_MIN_SIZE, 0);
		Preference<Integer> maxSize = new Preference<>(PreferenceType.GROUP_MIN_SIZE, 0);
		prefs.add(minSize);
		prefs.add(maxSize);
		return ResponseEntity.ok(prefs);
	}
	
	@RequestMapping(path = "/{tripId}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<String> deleteTrip(@PathVariable("tripId") String tripId) {
		String loginId = commonUtils.getLoggedInUserLoginId();
		
		try {
			tripService.deleteTrip(loginId, tripId);
			logger.info("Request for trip details for tripId ("+tripId+") by ("+loginId+") processed successfully.");
			
		} catch (TripManagementException tme) {
			logger.error(tme);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(CommonUtils.createErrorResponseMessage(tme.getMessage()));
		}
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(CommonUtils.createResponseMessage("Trip: "+tripId+" deleted successfully."));
	}
	
	@RequestMapping(path = "/{tripId}/leavetrip", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<String> leaveTrip(@PathVariable("tripId") String tripId) {
		String loginId = commonUtils.getLoggedInUserLoginId();
		
		try {
			tripService.leaveTrip(loginId, tripId);
			logger.info("Leave trip request by ("+loginId+") for trip ("+tripId+") processed successfully.");
			
		} catch (TripManagementException tme) {
			logger.error(tme);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(CommonUtils.createErrorResponseMessage(tme.getMessage()));
		}
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(CommonUtils.createResponseMessage("Leave trip request by ("+loginId+") for trip ("+tripId+") processed successfully."));
	}
	
	/**
	 * API called when Admin wants to reject a join request for a user.
	 * @param tripId
	 * @param tripJoinRequest
	 * @return
	 */
	@RequestMapping(path = "/{tripId}/reject", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<String> rejectTripJoinRequest(@PathVariable("tripId") String tripId, 
			@RequestBody TripJoinRequestDto tripJoinRequest) {
		String loginId = commonUtils.getLoggedInUserLoginId();
		
		try {
			tripService.rejectTripJoinRequest(loginId, tripJoinRequest);
			logger.info("Request to reject join trip ("+tripId+") for ("+tripJoinRequest.getRequestFrom()+") processed successfully.");
			
		} catch (TripManagementException tme) {
			logger.error(tme);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(CommonUtils.createErrorResponseMessage(tme.getMessage()));
		}
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(CommonUtils.createResponseMessage("Request to reject join trip ("+tripId+") for ("+tripJoinRequest.getRequestFrom()+") processed successfully."));
	}
	
	
}
