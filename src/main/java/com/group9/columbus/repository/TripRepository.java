package com.group9.columbus.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.group9.columbus.entity.Trip;

public interface TripRepository extends MongoRepository<Trip, String> {
	
	public List<Trip> findByTripUsersLoginIds(String loginId);
	
	public Trip findByTripId(String tripId);
	
	@Query(value = "{}", fields="{ 'id' : 1, 'tripStops' : 1, 'tripId' : 1}")
	public List<Trip> findAllTrips();
}
