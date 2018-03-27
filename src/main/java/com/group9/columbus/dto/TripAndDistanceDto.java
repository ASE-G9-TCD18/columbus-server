package com.group9.columbus.dto;

import com.group9.columbus.entity.Trip;

public class TripAndDistanceDto {
	
	private Trip trip;
	
	// Distance to a particular point
	private double distance;
	
	public TripAndDistanceDto(Trip trip, double distance) {
		super();
		this.trip = trip;
		this.distance = distance;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
}
