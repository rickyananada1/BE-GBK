package com.dev.gbk.controller;

import com.dev.gbk.model.User;
import com.dev.gbk.repo.MenuRepo;
import com.dev.gbk.repo.UserRepo;
import com.dev.gbk.service.GbkFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import payloads.UserRequest;

@RestController
@RequestMapping("/api")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    GbkFeignClient gbkFeignClient;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    Environment env;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllBookings() {
        return ResponseEntity.ok(userRepo.findAll());
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        User user = new User(userRequest.getName(), userRequest.getUsername(), userRequest.getEmail(), userRequest.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return ResponseEntity.ok(user);
    }
}