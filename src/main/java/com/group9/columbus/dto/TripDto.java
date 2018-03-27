package com.group9.columbus.dto;

import java.util.List;

import com.group9.columbus.entity.Preference;
import com.group9.columbus.entity.TripStop;
import com.group9.columbus.enums.TripType;

public class TripDto {
	
	private TripType tripType;
	
	private List<TripStop> tripStops;
	
	private List<Preference> preferences;
	
	private String timestamp;

	public TripType getTripType() {
		return tripType;
	}

	public void setTripType(TripType tripType) {
		this.tripType = tripType;
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

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
