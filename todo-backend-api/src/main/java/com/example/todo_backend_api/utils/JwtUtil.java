package com.example.todo_backend_api.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

//USING JWT 0.12.6(LATEST)
//getBody)() changed to getPayload()
//Jws = JSON Web Signature
//Claims = actual data inside token

@Component
public class JwtUtil {
	private final String SECRET = "SpringBootSecretKeySpringBootSecretKey123";
	private final long EXPIRATION = 1000 * 60 * 60; // 1 hour
	private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));;

	public String generateToken(String email) {
		return Jwts.builder().subject(email).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + EXPIRATION)).signWith(SECRET_KEY).compact();
	}// builder->create a empty token.

	public String extractEmail(String token) {
		return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload().getSubject();
	}

	public boolean validateJwtToken(String token) {
		try {
			extractEmail(token);
			return true;

		} catch (Exception e) {
			System.out.println("Invalid JWT: " + e.getMessage());
			return false;
		}
	}

}
