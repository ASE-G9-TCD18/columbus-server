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
	
	private TripType tripType;
	 
	@DBRef
	private List<ApplicationUser> tripUsers;
	
	private List<TripStop> tripStops;
	
	@DBRef
	private ApplicationUser admin;
	
	

	
}
