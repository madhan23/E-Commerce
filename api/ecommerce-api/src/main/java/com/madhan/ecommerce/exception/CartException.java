package com.madhan.ecommerce.exception;

public class CartException extends RuntimeException {

	private static final long serialVersionUID = 7754290252062L;

	public CartException(String message) {
		super(message);
	}
}
