package com.madhan.ecommerce.exception;

public class InvalidRequestDataException extends  RuntimeException{

	private static final long serialVersionUID = 1481718126833408390L;

	public InvalidRequestDataException(String message) {
        super(message);
    }
}
