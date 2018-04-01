package com.group9.columbus.service;

import com.group9.columbus.dto.TripRatingDto;
import com.group9.columbus.entity.Trip;
import com.group9.columbus.exception.TripManagementException;
import com.group9.columbus.repository.TripRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class to provide Rating service.
 * @author amit
 */
@Service
public class RatingService {
    Logger logger = Logger.getLogger(TripService.class);
    @Autowired
    private TripRepository tripRepo;

    public void rateTrip(String tripId, Double rating) throws TripManagementException {
        Trip trip = tripRepo.findByTripId(tripId);
        if(trip==null){
            throw new TripManagementException("Trip doesn't exist with tripId: "+ tripId);
        }
        trip.setTripRating(rating);
        tripRepo.save(trip);
    }

    public TripRatingDto getRating(String tripId){
        Trip trip = tripRepo.findByTripId(tripId);
        return new TripRatingDto(tripId, trip.getTripRating());
    }
}
