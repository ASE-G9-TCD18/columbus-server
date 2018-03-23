package com.group9.columbus.comparator;

import java.util.Comparator;

import com.group9.columbus.dto.TripAndDistanceDto;

public class DestinationDistanceComparator implements Comparator<TripAndDistanceDto> {
	
	@Override
	public int compare(TripAndDistanceDto obj1, TripAndDistanceDto obj2) {
		return (int)(obj1.getDistance() - obj2.getDistance());
	}
}
