package com.group9.columbus.dto;

import java.util.Date;
import java.util.List;

import com.group9.columbus.entity.Preference;
import com.group9.columbus.entity.TripStop;
import com.group9.columbus.enums.TripType;

public class TripDto {
	
	private TripType tripType;
	
	private String loginId;
	
	private List<TripStop> tripStops;
	
	private List<Preference> preferences;
	
	private Date timestamp;

	public TripType getTripType() {
		return tripType;
	}

	public void setTripType(TripType tripType) {
		this.tripType = tripType;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public List<TripStop> getTripStops() {
		return tripStops;
	}

	public void setTripStops(List<TripStop> tripStops) {
		this.tripStops = tripStops;
	}

	public List<Preference> getPreferences() {
		return preferences;
	}

	public void setPreferences(List<Preference> preferences) {
		this.preferences = preferences;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
