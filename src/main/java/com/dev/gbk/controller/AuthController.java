package com.dev.gbk.controller;

import java.net.URI;
import java.util.Collections;

import javax.validation.Valid;

import com.dev.gbk.model.Role;
import com.dev.gbk.model.RoleName;
import com.dev.gbk.repo.RoleRepo;
import com.dev.gbk.repo.UserRepo;
import com.dev.gbk.security.JwtTokenProvider;
import com.dev.gbk.service.GbkFeignClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dev.gbk.exception.AppException;
import com.dev.gbk.model.User;

import payloads.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
    UserRepo userRepository;

	@Autowired
    RoleRepo roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
    JwtTokenProvider tokenProvider;

	@Autowired
	GbkFeignClient gbkFeignClient;

	@Autowired
	Environment env;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

		ObjectMapper objectMapper = new ObjectMapper();
		SecurityContextHolder.getContext().setAuthentication(authentication);
		ReqGbkToken reqGbkToken = new ReqGbkToken(Integer.parseInt(env.getProperty("gbk.partner.id")), env.getProperty("gbk.partner.key"));
		ResponseEntity<RespGbkToken> responseEntity = gbkFeignClient.getTokenGbk(reqGbkToken);
		RespGbkToken respGbkToken = responseEntity.getBody();
		try{
			logger.info("Request to GBK [{}]", objectMapper.writeValueAsString(reqGbkToken));
			logger.info("Response from GBK [{}]", objectMapper.writeValueAsString(respGbkToken));
		}catch (Exception e){
			logger.info("Exception object mapper [{}]", e.getMessage());
		}
		String jwt = tokenProvider.generateToken(authentication, respGbkToken.getToken());
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));

	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		System.out.println("signup masuk");
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return new ResponseEntity(new ApiResponse(false, "Username is already taken!"), HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"), HttpStatus.BAD_REQUEST);
		}

		User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
				signUpRequest.getPassword());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
				.orElseThrow(() -> new AppException("User Role not set."));
		user.setRoles(Collections.singleton(userRole));
		User result = userRepository.save(user);
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{username}")
				.buildAndExpand(result.getUsername()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
	}
}
