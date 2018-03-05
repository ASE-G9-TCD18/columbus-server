package com.group9.columbus.entity;

import org.springframework.data.geo.Point;

public class TripStop {
	
	private int sequenceNumber;
	
	private Point coordinate;

	public TripStop() {
	}
	
	public TripStop(int sequenceNumber, Point coordinate) {
		super();
		this.sequenceNumber = sequenceNumber;
		this.coordinate = coordinate;
	}

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

}
