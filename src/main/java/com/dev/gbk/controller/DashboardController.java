package com.dev.gbk.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DASHBOARD')")
    public String dashboard() {
        return "Welcome to the dashboard GBK!";
    }
}
