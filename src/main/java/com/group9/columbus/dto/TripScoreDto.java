package com.group9.columbus.dto;

import com.group9.columbus.entity.Trip;

public class TripScoreDto {

	Trip trip;
	
	long score = 0;
	
	public TripScoreDto(Trip trip, long score) {
		super();
		this.trip = trip;
		this.score = score;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}
	
	
}
