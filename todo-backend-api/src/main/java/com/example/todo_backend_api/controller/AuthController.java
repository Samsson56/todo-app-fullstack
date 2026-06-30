package com.example.todo_backend_api.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo_backend_api.entity.User;
import com.example.todo_backend_api.repository.UserRepository;
import com.example.todo_backend_api.service.UserService;
import com.example.todo_backend_api.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor // constructor based dependency injection
@RequestMapping("/auth")
public class AuthController { // user controller

	private final UserService userService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody Map<String, String> body) {
		String email = body.get("email");
		String password = passwordEncoder.encode(body.get("password"));

		if (userRepository.findByEmail(email).isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");// 409
		}

		// creating new user (give object)
		userService.createUser(User.builder().email(email).password(password).build());
		return ResponseEntity.status(HttpStatus.CREATED).body("Successfully Registered.");// 201

	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody Map<String, String> body) { // (?)->any type
		String email = body.get("email");
		String password = body.get("password");

		// container object that may or may not contain a value
		Optional<User> optional = userRepository.findByEmail(email);

		if (optional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
		}
		// get user
		User user = optional.get();
		if (!passwordEncoder.matches(password, user.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid User");
		}
		String token = jwtUtil.generateToken(email);
		return ResponseEntity.ok(Map.of("token", token));

	}
}
