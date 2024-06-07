package com.dev.gbk.controller;

import com.dev.gbk.exception.NotAuthorizedException;
import com.dev.gbk.security.UserPrincipal;
import com.dev.gbk.security.UserPrincipalDetails;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GenController {

	@GetMapping("/user")
//	@PreAuthorize("hasRole('USER')")
	public String forUser(@Parameter(hidden = true)@AuthenticationPrincipal UserPrincipalDetails userPrincipal) {
		if(userPrincipal.can("Test")){
			throw new NotAuthorizedException();
		}
		return "welcome " + userPrincipal.can("Test") + userPrincipal.getUsername();
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String forAdmin() {

		return "welcome admin";
	}
	
	

}
