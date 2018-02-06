package com.group9.columbus.exception;

public class UserExistsException extends UserManagementException {
	
	private static final long serialVersionUID = -8043265473809996902L;

	public UserExistsException (String message) {
		super(message);
	}

}
