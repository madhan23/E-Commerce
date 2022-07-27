package com.madhan.ecommerce.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.madhan.ecommerce.delegate.OrderDelegator;
import com.madhan.ecommerce.dto.OrderInfoDto;
import com.madhan.ecommerce.dto.Response;
import com.madhan.ecommerce.entity.Order;
import com.madhan.ecommerce.util.AppUtil;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "*",allowedHeaders = "*")
@SecurityRequirement(name = "JwtToken")
@Tag(name="Order")
public class OrderController {

	@Autowired
	private OrderDelegator orderDelegator;


	@GetMapping("/orders/{id}")
	@ApiResponses(value = {
			@ApiResponse(description = "Order Data Fetched Successfully",responseCode = "200"),
			@ApiResponse(description = "Bad Request",responseCode = "400"),
			@ApiResponse(description = "No Order details found",responseCode = "404"),		
			@ApiResponse(description = "Unauthorized Access",responseCode = "401"),
			@ApiResponse(description = "Internal Server Error",responseCode = "500"),
	})
	public ResponseEntity<Response> getOrder(@PathVariable String id,
			HttpServletRequest request) {
		return new ResponseEntity<Response>(orderDelegator.getOrder(id, request.getHeader(AppUtil.AUTHORIZATION)), HttpStatus.OK);
	}

	@PostMapping("/orders/checkout")
	@ApiResponses(value = {
			@ApiResponse(description = "Order Created Fetched Successfully",responseCode = "201"),
			@ApiResponse(description = "Bad Request",responseCode = "400"),	
			@ApiResponse(description = "Unauthorized Access",responseCode = "401"),
			@ApiResponse(description = "Internal Server Error",responseCode = "500"),
	})
	public ResponseEntity<Response> createOrder(@RequestBody Order order,
			HttpServletRequest request) {
		return new ResponseEntity<Response>(orderDelegator.createOrder(order, request.getHeader(AppUtil.AUTHORIZATION)), HttpStatus.CREATED);
	}

	@PutMapping("/orders/update")
	@ApiResponses(value = {
			@ApiResponse(description = "Order Data Updated  Successfully",responseCode = "200"),
			@ApiResponse(description = "Bad Request",responseCode = "400"),	
			@ApiResponse(description = "No Order details found",responseCode = "404"),	
			@ApiResponse(description = "Unauthorized Access",responseCode = "401"),
			@ApiResponse(description = "Internal Server Error",responseCode = "500"),
	})
	public ResponseEntity<Response> updateOrder(@RequestBody OrderInfoDto orderInfo) {
		return new ResponseEntity<Response>(orderDelegator.updateOrder( orderInfo), HttpStatus.OK);
	}
}
