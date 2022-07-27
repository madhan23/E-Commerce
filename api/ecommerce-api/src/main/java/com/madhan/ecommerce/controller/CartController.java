package com.madhan.ecommerce.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.madhan.ecommerce.delegate.CartDelegate;
import com.madhan.ecommerce.dto.Response;
import com.madhan.ecommerce.entity.Cart;
import com.madhan.ecommerce.util.AppUtil;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/cart")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SecurityRequirement(name = "JwtToken")
@Tag(name="Cart")
public class CartController {

	@Autowired
	CartDelegate cartDelegate;

	@GetMapping
	@ApiResponses(value = {
			@ApiResponse(description = "Cart Data Fetched Successfully",responseCode = "200"),
			@ApiResponse(description = "Bad Request",responseCode = "400"),
			@ApiResponse(description = "No Cart Items Found",responseCode = "404"),
			@ApiResponse(description = "Unauthorized Access",responseCode = "401"),
			@ApiResponse(description = "Internal Server Error",responseCode = "500"),
	})
	public ResponseEntity<Response> getCart(HttpServletRequest request) {

		return new ResponseEntity<Response>(cartDelegate.getCart(request.getHeader(AppUtil.AUTHORIZATION)), HttpStatus.OK);
	}

	@PostMapping
	@ApiResponses(value = {
			@ApiResponse(description = "Cart Data Updated Successfully",responseCode = "200"),
			@ApiResponse(description = "Bad Request",responseCode = "400"),
			@ApiResponse(description = "Unauthorized Access",responseCode = "401"),
			@ApiResponse(description = "Internal Server Error",responseCode = "500"),
	})
	public ResponseEntity<Response> addCart(@RequestBody Cart cart, HttpServletRequest request) {
		return new ResponseEntity<Response>(cartDelegate.addCart(cart, request.getHeader(AppUtil.AUTHORIZATION)), HttpStatus.OK);
	}

	@DeleteMapping("/{productId}")
	@ApiResponses(value = {
			@ApiResponse(description = "Product Removed From Cart Successfully",responseCode = "204"),
			@ApiResponse(description = "Bad Request",responseCode = "400"),
			@ApiResponse(description = "Unauthorized Access",responseCode = "401"),
			@ApiResponse(description = "Internal Server Error",responseCode = "500"),
	})
	public ResponseEntity<Response> deleteItemFromCart(@PathVariable String productId,
			HttpServletRequest request) {
		return new ResponseEntity<Response>(cartDelegate.deleteItem(productId, request.getHeader(AppUtil.AUTHORIZATION)), HttpStatus.NO_CONTENT);
	}
}
