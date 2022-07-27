package com.madhan.ecommerce.exception;

public class ProductNotFoundException  extends RuntimeException{


	private static final long serialVersionUID = 7270426922990252062L;

	public ProductNotFoundException(String message) {
		super(message);
	}

	
}
