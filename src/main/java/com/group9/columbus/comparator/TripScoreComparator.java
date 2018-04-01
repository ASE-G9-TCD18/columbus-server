package com.group9.columbus.comparator;

import java.util.Comparator;

import com.group9.columbus.dto.TripScoreDto;

/**
 * Implementation of {@link Comparator} class that compares the score between two
 * {@link TripScoreComparator} objects.
 * @author amit
 */
public class TripScoreComparator implements Comparator<TripScoreDto> {

	@Override
	public int compare(TripScoreDto obj1, TripScoreDto obj2) {
		return (int)(obj1.getScore() - obj2.getScore());
	}

}
