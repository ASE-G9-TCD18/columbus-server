package com.group9.columbus.entity;

import org.springframework.data.geo.Point;

public class TripStop {
	
	private int sequenceNumber;
	
	//private GeoPoint coordinate;
	
	private Point coordinate;

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Point getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Point coordinate) {
		this.coordinate = coordinate;
	}

//	public GeoPoint getCoordinate() {
//		return coordinate;
//	}
//
//	public void setCoordinate(GeoPoint coordinate) {
//		this.coordinate = coordinate;
//	}
	
	
}
