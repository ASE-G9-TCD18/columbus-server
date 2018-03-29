package com.group9.columbus.dto;

import com.group9.columbus.utils.JsonUtils;

public class TripRatingDto {

    private String tripId;
    private Double rating;

    public TripRatingDto(){

    }

    public TripRatingDto(String tripId, Double rating){
        this.tripId = tripId;
        this.rating = rating;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

}
