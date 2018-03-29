package com.group9.columbus.dto;


/**
 * Basic DTO that stores a trip join request.
 * @author amit
 */
public class TripJoinRequestDto {;
	
	private String requestFrom;
	
	private String requestTo;
	
	private String tripId;

	public TripJoinRequestDto() { }
	
	public TripJoinRequestDto(String requestFrom, String requestTo, String tripId) {
		super();
		this.requestFrom = requestFrom;
		this.requestTo = requestTo;
		this.tripId = tripId;
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

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}
}
