package com.group9.columbus.utils;

import com.google.maps.model.LatLng;

/**
 * GreatCircleDistance computation class.
 */
public class GreatCircleDistance {

	double x1;
	double y1;
	double x2;
	double y2;
	
	public GreatCircleDistance(LatLng pt1, LatLng pt2) {
		this.x1 = Math.toRadians(pt1.lat);
		this.y1 = Math.toRadians(pt1.lng);
		this.x2 = Math.toRadians(pt2.lat);
		this.y2 = Math.toRadians(pt2.lng);
	}
	
	public double getDistance() {
		return computeCosine();
	}

	private double computeCosine() {
		// great circle distance in radians
		double angle1 = Math.acos(Math.sin(x1) * Math.sin(x2) + Math.cos(x1) * Math.cos(x2) * Math.cos(y1 - y2));

		// convert back to degrees
		angle1 = Math.toDegrees(angle1);

		// each degree on a great circle of Earth is 60 nautical miles
		double distance = 60 * angle1;
		return distance;
	}
}
