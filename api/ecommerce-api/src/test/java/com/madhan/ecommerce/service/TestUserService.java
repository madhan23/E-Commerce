package com.madhan.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.madhan.ecommerce.dto.UserLogin;
import com.madhan.ecommerce.entity.User;
import com.madhan.ecommerce.exception.UserException;
import com.madhan.ecommerce.exception.UserNotFoundException;
import com.madhan.ecommerce.repository.IUserRepository;

@ExtendWith(MockitoExtension.class)
public class TestUserService {

	@InjectMocks
	UserService service;

	@Mock
	IUserRepository repo;

	@Test
	public void testSaveUser() {
		User user = getUserObj();
		Mockito.when(repo.save(Mockito.any(User.class))).thenReturn(user);

		User response = service.saveUser(user);
		Mockito.verify(repo, Mockito.times(1)).save(Mockito.any(User.class));
		assertEquals("Test", response.getUsername());
		assertEquals("test@gmail.com", response.getEmail());
	}

	@Test
	public void testSaveUserException() {
		User user = getUserObj();
		Mockito.when(repo.save(Mockito.any(User.class))).thenThrow(new RuntimeException("something went wrong"));
		assertThrows(RuntimeException.class, () -> service.saveUser(user));

	}

	@Test
	public void testGetUser() {

		User user = getUserObj();
		String id = UUID.randomUUID().toString();
		user.setId(id);
		Mockito.when(repo.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		User response = service.getUser(id);
		assertEquals("Test", response.getUsername());
		assertEquals("test@gmail.com", response.getEmail());
	}

	@Test
	public void testGetUserForUserNotFoundException() {
		String id = UUID.randomUUID().toString();
		Mockito.when(repo.findById(Mockito.anyString()))
				.thenThrow(new UserNotFoundException("User Id ::" + id + "not found"));
		assertThrows(RuntimeException.class, () -> service.getUser(id));
	}

	@Test
	public void testDeleteUser() {
		String id = UUID.randomUUID().toString();
		Mockito.doNothing().when(repo).deleteById(Mockito.anyString());
		service.deleteUser(id);
	}

	@Test
	public void testDeleteUserException() {

		String id = UUID.randomUUID().toString();
		Mockito.doThrow(new RuntimeException("something went wrong")).when(repo).deleteById(Mockito.anyString());
		assertThrows(RuntimeException.class, () -> service.deleteUser(id));

	}

	@Test
	public void testValidateEmailFromDB() {

		Mockito.when(repo.findByEmail(Mockito.anyString())).thenReturn(null);
		service.validateEmailFromDB("admin@gmail.com");

	}

	@Test
	public void testValidateEmailFromDBForUserException() {
		try {
			User user = getUserObj();
			Mockito.when(repo.findByEmail(Mockito.anyString())).thenReturn(user);
			service.validateEmailFromDB("test@gmail.com");
		} catch (UserException e) {
			assertEquals("emailId already Present", e.getMessage());
		}

	}

	@Test
	public void testValidateUsernameFromDB() {

		Mockito.when(repo.findByUsername(Mockito.anyString())).thenReturn(null);
		service.validateUsernameFromDB("Test");

	}

	@Test
	public void testValidateUsernameFromDBForUserException() {
		try {
			User user = getUserObj();
			Mockito.when(repo.findByUsername(Mockito.anyString())).thenReturn(user);
			service.validateUsernameFromDB("Test");
		} catch (UserException e) {
			assertEquals("username already Present", e.getMessage());
		}

	}

	@Test
	public void testUserLogin() {

		User user = new User();
		user.setEmail("test@gmail.com");
		user.setUsername("Test");
		UserLogin request = new UserLogin("test@gmail.com", "GHJKLHGF");
		Mockito.when(repo.Login(Mockito.anyString(), Mockito.anyString())).thenReturn(user);
		User response = service.userLogin(request);
		Mockito.verify(repo, Mockito.times(1)).Login(Mockito.anyString(), Mockito.anyString());
		assertEquals("Test", response.getUsername());
		assertEquals("test@gmail.com", response.getEmail());
	}

	private User getUserObj() {
		User user = new User();
		user.setEmail("test@gmail.com");
		user.setUsername("Test");
		user.setPassword("pasword");
		user.setIsAdmin(true);
		user.setCreatedAt(LocalDateTime.now());

		return user;
	}
}
