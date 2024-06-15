package com.dev.gbk.controller;

import com.dev.gbk.repo.MenuRepo;
import com.dev.gbk.service.GbkFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MenuController {
    @Autowired
    private MenuRepo menuRepo;

    @Autowired
    GbkFeignClient gbkFeignClient;

    @Autowired
    Environment env;

    @GetMapping("/menus")
    public ResponseEntity<?> getAllMenus() {
        return ResponseEntity.ok(menuRepo.findAll());
    }
}