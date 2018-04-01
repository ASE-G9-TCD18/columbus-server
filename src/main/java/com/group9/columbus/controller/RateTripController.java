package com.group9.columbus.controller;

import com.group9.columbus.dto.TripRatingDto;
import com.group9.columbus.exception.TripManagementException;
import com.group9.columbus.service.RatingService;
import com.group9.columbus.utils.CommonUtils;
import com.group9.columbus.utils.JsonUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Container for trip rating APIs.
 * @author amit
 */
@RestController
@RequestMapping(value = "/rating")
public class RateTripController {
    private Logger logger = Logger.getLogger(SearchController.class);
    @Autowired
    private RatingService ratingService;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> setRatingByTripId(@RequestBody TripRatingDto ratingDto) {
        logger.info(JsonUtils.getJsonForResponse(ratingDto));
        boolean tripFound = false;
        try {
            ratingService.rateTrip(ratingDto.getTripId(), ratingDto.getRating());
            tripFound = true;
        }
        catch (TripManagementException e){
            logger.error(e.getMessage());
        }
        if(tripFound)
            return ResponseEntity.ok(CommonUtils.createResponseMessage("Successfully rating the trip with tripId: "+ratingDto.getTripId()));
        else
            return ResponseEntity.notFound().build();
    }

    @RequestMapping(path = "/{tripId}",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getRatingByTripId(@PathVariable("tripId") String tripId){
        return JsonUtils.getJsonForResponse(ratingService.getRating(tripId));
    }

}
