package com.madhan.ecommerce.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.madhan.ecommerce.dto.Response;
import com.madhan.ecommerce.util.AppUtil;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ProductNotFoundException.class)
	public final ResponseEntity<Response> handleProductNotFoundException(ProductNotFoundException ex, WebRequest request) {

		Response response = Response.builder().status(AppUtil.SUCCESS).dateTime(LocalDateTime.now())
				.message(ex.getMessage()).build();

		return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public final ResponseEntity<Response> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {

		Response response = Response.builder().status(AppUtil.SUCCESS).dateTime(LocalDateTime.now())
				.message(ex.getMessage()).build();

		return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
	}

	
	@ExceptionHandler(InvalidRequestDataException.class)
	public final ResponseEntity<Response> handleInvalidRequestException(InvalidRequestDataException ex,
			WebRequest request) {

		Response response = Response.builder().status(AppUtil.SUCCESS).dateTime(LocalDateTime.now())
				.message(ex.getMessage()).build();

		return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
	}

	
	@ExceptionHandler(UserException.class)
	public final ResponseEntity<Response> handleUserException(UserException ex, WebRequest request) {

		Response response = Response.builder().status(AppUtil.SUCCESS).dateTime(LocalDateTime.now())
				.message(ex.getMessage()).build();

		return new ResponseEntity<Response>(response, ex.getStatus());
	}
	
	@ExceptionHandler(value = { ProductException.class, CartException.class,
			OrderException.class })
	public final ResponseEntity<Response> handleUserException(Exception ex, WebRequest request) {

		Response response = Response.builder().status(AppUtil.FAILURE).dateTime(LocalDateTime.now())
				.message(ex.getMessage()).build();

		return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
