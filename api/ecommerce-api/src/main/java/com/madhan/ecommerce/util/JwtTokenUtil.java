package com.madhan.ecommerce.util;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madhan.ecommerce.dto.UserAuthentication;
import com.madhan.ecommerce.exception.UserException;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = 9124260504483993836L;
	@Value("${jwt.secret}")
	private String secret;

	@Autowired
	ObjectMapper mapper;

	public String generateJWTToken(String data) throws IllegalArgumentException, JWTCreationException {
		return JWT.create().withSubject("UserDetails").withClaim("userInfo", data).withIssuedAt(new Date())
				.withExpiresAt((Date.from(ZonedDateTime.now(ZoneId.systemDefault()).plusMinutes(30).toInstant())))
				.sign(Algorithm.HMAC256(secret));

	}

	public String validateJWTToken(String header) throws JWTVerificationException {
		if (header == null) {
			throw new UserException("Authorization Header Missing", HttpStatus.BAD_REQUEST);
		}

		if (header.isEmpty() || !header.startsWith("Bearer")) {
			throw new UserException("Invalid JWT Token in Bearer Header", HttpStatus.BAD_REQUEST);
		}

		String token = header.split(" ")[1];
		try {
			JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).withSubject("UserDetails").build();
			DecodedJWT jwt = verifier.verify(token);
			return jwt.getClaim("userInfo").asString();
		} catch (JWTVerificationException ex) {

			throw new UserException("Invalid JWT Token", HttpStatus.UNAUTHORIZED);
		}
	}

	public void authorizeRequest(String authHeader) {
		String data = validateJWTToken(authHeader);
		UserAuthentication auth = extractUserdetails(data);
		if (auth != null && !auth.getIsAdmin()) {
			throw new UserException(" Insufficient Permission Access Deined", HttpStatus.FORBIDDEN);
		}

	}

	public UserAuthentication extractUserdetails(String data) {
		UserAuthentication auth = null;
		try {
			auth = mapper.readValue(data, UserAuthentication.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return auth;
	}
}
