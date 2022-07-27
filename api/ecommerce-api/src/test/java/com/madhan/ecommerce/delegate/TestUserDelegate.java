package com.madhan.ecommerce.delegate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madhan.ecommerce.dto.UserLogin;
import com.madhan.ecommerce.dto.UserResponse;
import com.madhan.ecommerce.entity.User;
import com.madhan.ecommerce.exception.InvalidRequestDataException;
import com.madhan.ecommerce.service.UserService;
import com.madhan.ecommerce.util.JwtTokenUtil;

@ExtendWith(MockitoExtension.class)
public class TestUserDelegate {
	@InjectMocks
	UserDelegate userDelegate;
	@Mock
	private UserService userService;
	@Mock
	JwtTokenUtil jwt;
	@Mock
	ObjectMapper mapper;

	@Test
	public void testSignUp() {
		Mockito.doNothing().when(userService).validateEmailFromDB(Mockito.anyString());
		Mockito.doNothing().when(userService).validateUsernameFromDB(Mockito.anyString());
		Mockito.when(userService.saveUser(Mockito.any(User.class))).thenReturn(getUserObj());
		User user = userDelegate.signUp(getUserObj());
		assertEquals("test@gmail.com", user.getEmail());
	}

	@Test
	public void testSignUpForInvalidUsername() {
		User user = new User();
		user.setId(UUID.randomUUID().toString());
		try {
			userDelegate.signUp(user);
		} catch (InvalidRequestDataException e) {
			assertEquals("Username field cannot be null or empty", e.getMessage());
		}

	}
	
	
	@Test
	public void testSignUpForInvalidPassword() {
		User user = new User();
		user.setUsername("Test");
		user.setEmail("test@gmail.com");
		user.setId(UUID.randomUUID().toString());
		try {
			userDelegate.signUp(user);
		} catch (InvalidRequestDataException e) {
			assertEquals("password field cannot be null or empty", e.getMessage());
		}

	}
	
	
	@Test
	public void testSignUpForInvalidEmail() {
		User user = new User();
		user.setUsername("Test");
		user.setPassword("pasword");
		user.setEmail("tes.*t1234gmail.com");
		user.setId(UUID.randomUUID().toString());
		try {
			userDelegate.signUp(user);
		} catch (InvalidRequestDataException e) {
			assertEquals("Invalid Email Id", e.getMessage());
		}

	}


	
	@Test
	public void testGetUser() {
		Mockito.doNothing().when(jwt).authorizeRequest(Mockito.anyString());
		Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(getUserObj());
		User user = userDelegate.getUser(UUID.randomUUID().toString(),"Berer Token");
		assertEquals("test@gmail.com", user.getEmail());
	}
	
	
	@Test
	public void testLogin() throws JsonProcessingException {
		UserLogin request = new UserLogin("test@gmail.com","pasword");
		Mockito.when(userService.userLogin(Mockito.any(UserLogin.class))).thenReturn(getUserObj());
		Mockito.when(mapper.writeValueAsString(Mockito.any())).thenReturn("");
		Mockito.when(jwt.generateJWTToken(Mockito.anyString())).thenReturn("etyujhjGHJ");
		UserResponse data = userDelegate.login(request);
		assertEquals("test@gmail.com", data.getEmailId());
		assertEquals("etyujhjGHJ", data.getAccessToken());
		
	}
	
	private User getUserObj() {
		User user = new User();
		user.setId(UUID.randomUUID().toString());
		user.setEmail("test@gmail.com");
		user.setUsername("Test");
		user.setPassword("pasword");
		user.setCreatedAt(LocalDateTime.now());

		return user;
	}
}
