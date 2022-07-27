package com.madhan.ecommerce.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductException extends RuntimeException {

	private static final long serialVersionUID = 825787721949519369L;

	private String message;
	private HttpStatus status;

}
