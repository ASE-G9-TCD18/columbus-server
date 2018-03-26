package com.group9.columbus.comparator;

import java.util.Comparator;

import com.group9.columbus.dto.TripScoreDto;

public class TripScoreComparator implements Comparator<TripScoreDto> {

	@Override
	public int compare(TripScoreDto obj1, TripScoreDto obj2) {
		return (int)(obj1.getScore() - obj2.getScore());
	}

}
