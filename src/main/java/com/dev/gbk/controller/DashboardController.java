package com.dev.gbk.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DashboardController {
    @GetMapping("/dashboard")
    public String dashboard() {
        return "Welcome to the dashboard GBK!";
    }
}
