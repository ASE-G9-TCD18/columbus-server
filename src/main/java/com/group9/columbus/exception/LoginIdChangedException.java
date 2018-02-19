package com.group9.columbus.exception;

public class LoginIdChangedException extends UserManagementException {

	private static final long serialVersionUID = 6896888611874340214L;

	public LoginIdChangedException(String message) {
		super(message);
	}

}
