package com.group9.columbus.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import com.group9.columbus.comparator.TripScoreComparator;
import com.group9.columbus.dto.TripDto;
import com.group9.columbus.dto.TripScoreDto;
import com.group9.columbus.entity.Preference;
import com.group9.columbus.entity.Trip;
import com.group9.columbus.entity.TripStop;
import com.group9.columbus.enums.PreferenceType;

@Service
public class SearchService {

	private Logger logger = Logger.getLogger(SearchService.class);

	@Autowired
	private GpsService gpsService;

	@Autowired
	private TripService tripService;

	public List<Trip> searchTrips(String loginId, TripDto requestedTrip) {
		
		List<TripScoreDto> tripWithScoreList = new ArrayList<TripScoreDto>();
		TripStop source = requestedTrip.getTripStops().get(0);
		TripStop dest = requestedTrip.getTripStops().get(1);

		List<Trip> trips = tripService.getTripsNearToDestination(dest, TravelMode.BICYCLING);

		// Rank these trips on basis of similarity
		int i = 1;
		for (Trip trip : trips) {
			long score = 0;
			score = 2 / i;

			// Iterate over preferences of requested trip and match with preferences of the
			// found trip
			for (Preference<String> prefOfReqTrip : requestedTrip.getPreferences()) {

				// If Gender Preference requested
				if (prefOfReqTrip.getPreferenceType().equals(PreferenceType.GENDER)) {
					String prefValueForFoundTrip = getPreferenceForPreferenceType(requestedTrip, PreferenceType.GENDER);

					// If there is no preference in the requested trip
					if ("NA".equals(prefOfReqTrip.getValue())) {
						score += 1;
					} else if ("NA".equals(prefValueForFoundTrip)) {
						// If the found trip has no preference
						score += 0.5;
					} else if (prefOfReqTrip.getValue().equals(prefValueForFoundTrip)) {
						score += 1;
					}
				} else if (prefOfReqTrip.getPreferenceType().equals(PreferenceType.START_DATE)) {
					// The farther away from the start time lesser the score
					String prefValueForFoundTrip = getPreferenceForPreferenceType(requestedTrip, PreferenceType.START_DATE);
					
					long diff = differenceBtwnTwoDates((String)prefOfReqTrip.getValue(), prefValueForFoundTrip);
					
					score += (24-diff)/24;
				}
			}
			
			
			TripScoreDto tripWithScore = new TripScoreDto(trip, score);
			tripWithScoreList.add(tripWithScore);
		}
		
		
		tripWithScoreList.sort(new TripScoreComparator());
		trips = new ArrayList<>();
		for (TripScoreDto tripWithScore : tripWithScoreList) {
			Trip trip = tripWithScore.getTrip();

			if (trip.getTripUsersLoginIds().contains(loginId)|| trip.getAdmin().equals(loginId)){
				logger.info("current user is present in the trip! Skip this trip.");
			}
			else{
				logger.info("current user is not present in the trip.");
				trips.add(tripWithScore.getTrip());
			}
		}
		return trips;
	}

	private String getPreferenceForPreferenceType(TripDto obj, PreferenceType type) {
		//TODO:
		for (Preference pref : obj.getPreferences()) {
			if (pref.getPreferenceType().equals(type)) {
				return (String)pref.getValue();
			}
		}
		
		return null;
	}

	private long differenceBtwnTwoDates(String d1, String d2) {
		SimpleDateFormat dates = new SimpleDateFormat("MM-dd-yyyy_HH:mm:ss");

		// Setting dates
		Date date1;
		try {
			date1 = dates.parse(d1);
		Date date2 = dates.parse(d2);

		// Comparing dates
		long difference = Math.abs(date1.getTime() - date2.getTime());
		long differenceHours = difference / (60 * 60 );

		return differenceHours;
		} catch (ParseException e) {
			logger.error(e);
			return -1;
		}
	}
}
