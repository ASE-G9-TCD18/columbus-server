package com.group9.columbus.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.group9.columbus.enums.TripType;

@Document
public class Trip {
	
	@Id
	private String id;
	
	private String tripId;
	
	private TripType tripType;
	
	private List<Preference> preferences;
	 
	@DBRef
	private List<ApplicationUser> tripUsersIds;
	
	private List<String> tripUsersLoginIds;
	
	private List<TripStop> tripStops;
	
	@DBRef
	private ApplicationUser admin;
	
	@DBRef
	private Conversation conversation;
	
}
