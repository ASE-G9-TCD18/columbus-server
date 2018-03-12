package com.group9.columbus.service;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.LatLng;

@Service
public class GpsService {
	
	@Autowired
	GeoApiContext geoApiContext;
	
	public void getDistance(LatLng departure, LatLng... arrivals) {
		DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest(geoApiContext);
		request.departureTime(new DateTime());
		
	}
	
}
