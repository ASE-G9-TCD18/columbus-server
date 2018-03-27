package com.group9.columbus.entity;


import com.google.maps.model.LatLng;

public class TripStop {
	
	private int sequenceNumber;
	
	private LatLng coordinate;

	public TripStop() {
	}
	
	public TripStop(int sequenceNumber, LatLng coordinate) {
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

	public LatLng getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(LatLng coordinate) {
		this.coordinate = coordinate;
	}

}
