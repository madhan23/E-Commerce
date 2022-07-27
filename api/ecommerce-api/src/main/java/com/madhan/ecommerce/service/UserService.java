package com.madhan.ecommerce.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.madhan.ecommerce.dto.UserLogin;
import com.madhan.ecommerce.entity.User;
import com.madhan.ecommerce.exception.UserException;
import com.madhan.ecommerce.exception.UserNotFoundException;
import com.madhan.ecommerce.repository.IUserRepository;

@Service
public class UserService {

	@Autowired
	private IUserRepository userRepository;

	public User saveUser(User user) {

		try {
			if(user.getPassword()!=null){
				final byte[] authBytes = user.getPassword().getBytes(StandardCharsets.UTF_8);
				final String encodeddata = Base64.getEncoder().encodeToString(authBytes);
				user.setPassword(encodeddata);
			}
			return userRepository.save(user);
		} catch (Exception e) {
			throw new UserException("Error Occurred During User Save  ::" + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	public User getUser(String id) {

		return userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User Details Not Found for UserId ::" + id));

	}

	public void deleteUser(String id) {
		try {
			userRepository.deleteById(id);
		} catch (Exception e) {
			throw new UserException("Error Occurred During Delete User ::" + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public void validateEmailFromDB(String email) {

		User user =  userRepository.findByEmail(email);

		if (user != null) {
			throw new UserException("emailId already Present", HttpStatus.CONFLICT);
		}

	}

	public  User getUserProvider(String email,String provider) {

		return userRepository.findByEmailAndProvider(email,provider);
	}

	public void validateUsernameFromDB(String username) {

		User user = userRepository.findByUsername(username);

		if (user != null) {
			throw new UserException("username already Present", HttpStatus.CONFLICT);
		}
	}

	public User userLogin(UserLogin user) {
		final byte[] authBytes = user.getPassword().getBytes(StandardCharsets.UTF_8);
		final String encodedPwd = Base64.getEncoder().encodeToString(authBytes);
		return userRepository.Login(user.getEmail(), encodedPwd);
	}

	public User checkUserProviderDetail(String email, String provider) {

		return userRepository.findByEmailAndProvider(email,provider);
	}
}
