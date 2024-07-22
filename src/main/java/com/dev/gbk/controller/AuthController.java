package com.dev.gbk.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.gbk.dto.LoginRequest;
import com.dev.gbk.dto.UserRequest;
import com.dev.gbk.payloads.JWTAuthResponse;
import com.dev.gbk.service.AuthService;
import com.dev.gbk.utils.ResponseHandler;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	// Build Login REST API
	@PostMapping(value = { "/login", "/signin" })
	public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
		logger.info("username: " + loginRequest.getUsernameOrEmail() + " password: " + loginRequest.getPassword());
		Map<String, Object> authResponse = authService.login(loginRequest);

		JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
		jwtAuthResponse.setAccessToken((String) authResponse.get("token"));
		jwtAuthResponse.setUser(authResponse.get("user"));

		return ResponseHandler.generateResponse("Login successfully", HttpStatus.OK, jwtAuthResponse);
	}

	// Build Register REST API
	@PostMapping(value = { "/register", "/signup" })
	public ResponseEntity<Object> register(@RequestBody UserRequest userRequest) {
		authService.register(userRequest);
		return ResponseHandler.generateResponse("User register successfully", HttpStatus.OK, null);
	}
}