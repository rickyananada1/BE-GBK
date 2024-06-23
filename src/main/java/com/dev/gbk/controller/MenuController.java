package com.dev.gbk.controller;

import com.dev.gbk.model.Menu;
import com.dev.gbk.service.MenuService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
public class MenuController {
    private final MenuService menuService;
    private static final Logger log = LoggerFactory.getLogger(MenuController.class);

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/menus")
    public ResponseEntity<List<Menu>> findAll() {
        log.info("REST request to get all Menus");
        return ResponseEntity.ok(menuService.findAll());
    }
}