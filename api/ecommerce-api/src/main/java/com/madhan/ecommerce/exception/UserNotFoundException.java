package com.madhan.ecommerce.exception;

public class UserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 72704290252062L;

	public UserNotFoundException(String message) {
		super(message);
	}
}
