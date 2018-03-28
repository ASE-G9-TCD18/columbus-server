package com.group9.columbus.dto;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.group9.columbus.entity.Trip;

/**
 * Basic DTO that stores a trip join request.
 * @author amit
 */
public class TripJoinRequestDto {;
	
	private String requestFrom;
	
	private String requestTo;
	
	@DBRef(lazy=true)
	private Trip trip;

	public TripJoinRequestDto() { }
	
	public TripJoinRequestDto(String requestFrom, String requestTo, Trip trip) {
		super();
		this.requestFrom = requestFrom;
		this.requestTo = requestTo;
		this.trip = trip;
	}

	public String getRequestFrom() {
		return requestFrom;
	}

	public void setRequestFrom(String requestFrom) {
		this.requestFrom = requestFrom;
	}

	public String getRequestTo() {
		return requestTo;
	}

	public void setRequestTo(String requestTo) {
		this.requestTo = requestTo;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}
}
