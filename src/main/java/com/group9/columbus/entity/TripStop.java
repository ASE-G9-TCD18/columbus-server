package com.group9.columbus.entity;

import com.mongodb.client.model.geojson.Point;

public class TripStop {
	
	private int sequenceNumber;
	
	private Point coordinates;

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Point getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Point coordinates) {
		this.coordinates = coordinates;
	}
}
