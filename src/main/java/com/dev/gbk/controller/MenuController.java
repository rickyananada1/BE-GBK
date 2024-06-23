package com.dev.gbk.controller;

import com.dev.gbk.model.Menu;
import com.dev.gbk.service.MenuService;
import com.dev.gbk.utils.ResponseHandler;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/menus")
    public ResponseEntity<Object> findAll() {
        return ResponseHandler.generateResponse("Success get all menus", HttpStatus.OK, menuService.findAll());
    }
}