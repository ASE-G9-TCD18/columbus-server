package com.group9.columbus.exception;

public class PasswordChangedException extends UserManagementException {

	private static final long serialVersionUID = 2124745688012716457L;

	public PasswordChangedException(String message) {
		super(message);
	}

}
