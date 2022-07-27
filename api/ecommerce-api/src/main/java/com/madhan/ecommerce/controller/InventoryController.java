package com.madhan.ecommerce.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madhan.ecommerce.delegate.InventoryDelegate;
import com.madhan.ecommerce.dto.Response;
import com.madhan.ecommerce.entity.Product;
import com.madhan.ecommerce.util.AppUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/products")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Inventory")
public class InventoryController {

	@Autowired
	InventoryDelegate inventoryDelegate;

	@GetMapping
	@ApiResponses(value = { @ApiResponse(description = "Product Fetched Successfully", responseCode = "200"),
			@ApiResponse(description = "No Product Data Found", responseCode = "404"),
			@ApiResponse(description = "Internal Server Error", responseCode = "500"), })
	public ResponseEntity<Response> getAllProducts(@RequestParam(value = "page", defaultValue = "1") String page,
			@RequestParam(value = "limit", defaultValue = "10") String limit,
			@RequestParam(value = "categories", required = false) String categories,
			@RequestParam(value = "size", required = false) String size,
			@RequestParam(value = "sortBy", defaultValue = "price") String sortBy,
			@RequestParam(value = "orderBy", defaultValue = "asc") String orderBy) throws UnsupportedEncodingException {
		if (categories != null) {
			categories = URLDecoder.decode(categories, "UTF-8");
		}

		if (size != null) {
			size = URLDecoder.decode(size, "UTF-8");
		}

		return new ResponseEntity<Response>(
				inventoryDelegate.getAllProducts(page, limit, categories, size, sortBy, orderBy), HttpStatus.OK);
	}

	@GetMapping("/{productId}")
	@ApiResponses(value = { @ApiResponse(description = "Product Fetched Successfully", responseCode = "200"),
			@ApiResponse(description = "No Product Data Found", responseCode = "404"),
			@ApiResponse(description = "Internal Server Error", responseCode = "500"), })
	public ResponseEntity<Response> getproduct(@PathVariable final String productId) {

		return new ResponseEntity<Response>(inventoryDelegate.getProduct(productId), HttpStatus.OK);
	}

	@PostMapping
	@Operation(security = @SecurityRequirement(name = "JwtToken"))
	@ApiResponses(value = { @ApiResponse(description = "Product Saved Successfully", responseCode = "201"),
			@ApiResponse(description = "Bad Request", responseCode = "400"),
			@ApiResponse(description = "UnAuthorized Access", responseCode = "401"),
			@ApiResponse(description = "Insufficient Access Permission", responseCode = "403"),
			@ApiResponse(description = "Internal Server Error", responseCode = "500"), })
	public ResponseEntity<Response> saveproduct(@RequestBody Product product, HttpServletRequest request) {
		return new ResponseEntity<Response>(
				inventoryDelegate.saveproduct(product, request.getHeader(AppUtil.AUTHORIZATION)), HttpStatus.CREATED);
	}

	@PutMapping
	@Operation(security = @SecurityRequirement(name = "JwtToken"))
	@ApiResponses(value = { @ApiResponse(description = "Product Updated Successfully", responseCode = "200"),
			@ApiResponse(description = "Bad Request", responseCode = "400"),
			@ApiResponse(description = "UnAuthorized Access", responseCode = "401"),
			@ApiResponse(description = "Insufficient Access Permission", responseCode = "403"),
			@ApiResponse(description = "Internal Server Error", responseCode = "500"), })
	public ResponseEntity<Response> updateProduct(@RequestBody Product product, HttpServletRequest request) {
		return new ResponseEntity<Response>(
				inventoryDelegate.updateProduct(product, request.getHeader(AppUtil.AUTHORIZATION)), HttpStatus.OK);
	}

	@DeleteMapping("/{productId}")
	@Operation(security = @SecurityRequirement(name = "JwtToken"))
	@ApiResponses(value = { @ApiResponse(description = "Product Deleted Successfully", responseCode = "204"),
			@ApiResponse(description = "Bad Request", responseCode = "400"),
			@ApiResponse(description = "UnAuthorized Access", responseCode = "401"),
			@ApiResponse(description = "Insufficient Access Permission", responseCode = "403"),
			@ApiResponse(description = "Internal Server Error", responseCode = "500"), })
	public ResponseEntity<Response> deleteProduct(@PathVariable final String productId, HttpServletRequest request) {
		return new ResponseEntity<Response>(
				inventoryDelegate.deleteProduct(productId, request.getHeader(AppUtil.AUTHORIZATION)),
				HttpStatus.NO_CONTENT);
	}
}
