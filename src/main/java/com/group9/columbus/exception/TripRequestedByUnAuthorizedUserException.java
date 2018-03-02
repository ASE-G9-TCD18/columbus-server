package com.group9.columbus.exception;

public class TripRequestedByUnAuthorizedUserException extends TripManagementException {

	private static final long serialVersionUID = -3355965838274177200L;

	public TripRequestedByUnAuthorizedUserException(String message) {
		super(message);
	}

}
