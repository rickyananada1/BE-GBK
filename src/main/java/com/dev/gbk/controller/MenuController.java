package com.dev.gbk.controller;

import com.dev.gbk.repo.MenuRepo;
import com.dev.gbk.service.GbkFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api")
public class MenuController {
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private MenuRepo menuRepo;

    @Autowired
    GbkFeignClient gbkFeignClient;

    @Autowired
    Environment env;

    @GetMapping("/menus")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllBookings() {
        return ResponseEntity.ok(menuRepo.findAll());
    }
}