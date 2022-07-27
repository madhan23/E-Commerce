package com.madhan.ecommerce.exception;

public class OrderException extends RuntimeException {
	private static final long serialVersionUID = 3204290252062L;

	public OrderException(String message) {
		super(message);
	}
}
