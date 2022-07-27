package com.madhan.ecommerce.controller;

import javax.servlet.http.HttpServletRequest;

import com.madhan.ecommerce.dto.UserProviderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.madhan.ecommerce.delegate.UserDelegate;
import com.madhan.ecommerce.dto.UserLogin;
import com.madhan.ecommerce.dto.UserResponse;
import com.madhan.ecommerce.entity.User;
import com.madhan.ecommerce.util.AppUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name="User")
public class UserController {

	@Autowired
	UserDelegate userDelegate;

	@PostMapping("user/signup")
	@ApiResponses(value = {
			@ApiResponse(description = "User Registered Successfully",responseCode = "201"),
			@ApiResponse(description = "Bad Request",responseCode = "400"),
			@ApiResponse(description = "Invalid Credentials", responseCode  = "401"),
			@ApiResponse(description = "Username or Email Already Exist in DB", responseCode  = "409"),
			@ApiResponse(description = "Internal Server Error",responseCode = "500"),
	})
	public ResponseEntity<User> signUp(@RequestBody User user) {
		return new ResponseEntity<User>(userDelegate.signUp(user), HttpStatus.CREATED);
	}

	@PostMapping("user/signin")
	@ApiResponses(value = {
			@ApiResponse(description = "User Successfully Logged In",responseCode = "200"),
			@ApiResponse(description = "Bad Request",responseCode = "400"),
			@ApiResponse(description = "Invalid Credentials", responseCode  = "401"),
			@ApiResponse(description = "Internal Server Error",responseCode = "500"),
	})
	public ResponseEntity<UserResponse> login(@RequestBody UserLogin user) throws JsonProcessingException {
		return new ResponseEntity<UserResponse>(userDelegate.login(user), HttpStatus.OK);
	}


	@PostMapping("/provider/signin")
	@ApiResponses(value = {
			@ApiResponse(description = "User Successfully Logged In",responseCode = "200"),
			@ApiResponse(description = "Internal Server Error",responseCode = "500"),
	})
	public ResponseEntity<UserResponse> userProviderLogin(@RequestBody UserProviderDto user) throws JsonProcessingException {
		return new ResponseEntity<UserResponse>(userDelegate.userProviderLogin(user), HttpStatus.OK);
	}

	@GetMapping("/user/{userId}")
	@ApiResponses(value = {
			@ApiResponse(description = "Users Data Fetched Successfully",responseCode = "201"),
			@ApiResponse(description = "Bad Request",responseCode = "400"),
			@ApiResponse(description = "Unauthorized Access",responseCode = "401"),
			@ApiResponse(description = "Insufficient Access Permission",responseCode = "403"),
			@ApiResponse(description = "Internal Server Error",responseCode = "500"),
	})
	@Operation(security = @SecurityRequirement(name = "JwtToken"))
	public ResponseEntity<User> getuser(@PathVariable final String userId,
			HttpServletRequest request) {

		return new ResponseEntity<User>(userDelegate.getUser(userId, request.getHeader(AppUtil.AUTHORIZATION)), HttpStatus.OK);

	}

}
