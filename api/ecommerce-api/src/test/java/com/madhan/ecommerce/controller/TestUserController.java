package com.madhan.ecommerce.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.madhan.ecommerce.delegate.UserDelegate;
import com.madhan.ecommerce.dto.UserLogin;
import com.madhan.ecommerce.dto.UserResponse;
import com.madhan.ecommerce.entity.User;
import com.madhan.ecommerce.util.AppUtil;

@ExtendWith(MockitoExtension.class)
public class TestUserController {

	@InjectMocks
	private UserController controller;

	@Mock
	private UserDelegate delegate;

	@Spy
	private MockHttpServletRequest httpServletRequest;

	@Test
	public void testSignUp() {

		Mockito.when(delegate.signUp(Mockito.any(User.class))).thenReturn(getUserObj());
		ResponseEntity<User> response = controller.signUp(getUserObj());
		Mockito.verify(delegate).signUp(Mockito.any(User.class));
		assertEquals("Test", response.getBody().getUsername());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

	}

	@Test
	public void testLogin() throws JsonProcessingException {
		UserResponse userResponse = new UserResponse("Test", "test@gmail.com", "FGJLCVNXMbcgfhjkhgfd");
		Mockito.when(delegate.login(Mockito.any(UserLogin.class))).thenReturn(userResponse);

		ResponseEntity<UserResponse> response = controller.login(new UserLogin("test@gmail.com", "pasword"));
		Mockito.verify(delegate).login(Mockito.any(UserLogin.class));
		assertEquals("Test", response.getBody().getUsername());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testGetuser() {

		Mockito.when(delegate.getUser(Mockito.anyString(), Mockito.anyString())).thenReturn(getUserObj());
		httpServletRequest.addHeader(AppUtil.AUTHORIZATION, "Bearer GHFDGHJK");
		ResponseEntity<User> response = controller.getuser("1", httpServletRequest);
		Mockito.verify(delegate).getUser(Mockito.anyString(), Mockito.anyString());
		assertEquals("Test", response.getBody().getUsername());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	private User getUserObj() {
		User user = new User();
		user.setId(UUID.randomUUID().toString());
		user.setEmail("test@gmail.com");
		user.setUsername("Test");
		user.setPassword("pasword");
		user.setIsAdmin(true);
		user.setCreatedAt(LocalDateTime.now());

		return user;
	}
}
