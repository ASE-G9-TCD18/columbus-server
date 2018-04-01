package com.group9.columbus.service;

import java.io.IOException;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

/**
 * Service class for GPS based operations.
 * @author amit
 */
@Service
public class GpsService {
	
	@Autowired
	GeoApiContext geoApiContext;
	
	public DistanceMatrix getDistance(TravelMode travelMode, LatLng source, LatLng[] destinations) {
		DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest(geoApiContext);
		try {
			DistanceMatrix dm = request.departureTime(new DateTime())
					.origins(source)
					.destinations(destinations)
					.mode(travelMode)
					.await();
			
			return dm;
		} catch (ApiException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
