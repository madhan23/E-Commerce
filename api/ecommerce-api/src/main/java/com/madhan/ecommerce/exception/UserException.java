package com.madhan.ecommerce.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String message;
	private HttpStatus status;

}
