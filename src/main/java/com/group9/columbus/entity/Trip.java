package com.group9.columbus.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.group9.columbus.enums.TripType;

@Document
public class Trip {
	
	@Id
	private String id;
	
	@Indexed(unique = true)
	private String tripId;
	
	private TripType tripType;
	
	private List<Preference> preferences;
	 
	private List<String> tripUsersLoginIds;
	
	private List<TripStop> tripStops;
	
	private String admin;
	
	@DBRef
	private Conversation conversation;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public TripType getTripType() {
		return tripType;
	}

	public void setTripType(TripType tripType) {
		this.tripType = tripType;
	}

	public List<Preference> getPreferences() {
		return preferences;
	}

	public void setPreferences(List<Preference> preferences) {
		this.preferences = preferences;
	}

	public List<String> getTripUsersLoginIds() {
		return tripUsersLoginIds;
	}

	public void setTripUsersLoginIds(List<String> tripUsersLoginIds) {
		this.tripUsersLoginIds = tripUsersLoginIds;
	}

	public List<TripStop> getTripStops() {
		return tripStops;
	}

	public void setTripStops(List<TripStop> tripStops) {
		this.tripStops = tripStops;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}
	
}
