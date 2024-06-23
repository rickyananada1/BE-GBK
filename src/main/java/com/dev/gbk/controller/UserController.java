package com.dev.gbk.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.dev.gbk.dto.UserRequest;
import com.dev.gbk.service.UserService;
import com.dev.gbk.utils.ResponseHandler;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.Collection;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET all users
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_USER')")
    public ResponseEntity<Object> getUsers(@RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return ResponseHandler.generateResponse("Success get all users", HttpStatus.OK,
                userService.findAll(search, page, size));
    }

    // GET user by username
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_USER')")
    public ResponseEntity<Object> getUserByUsername(@PathVariable Long id) {
        return ResponseHandler.generateResponse("Success get user by username", HttpStatus.OK,
                userService.findById(id));
    }

    // CREATE new user
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CREATE_USER')")
    public ResponseEntity<Object> store(@RequestBody UserRequest userRequest) {
        return ResponseHandler.generateResponse("Success create user", HttpStatus.CREATED,
                userService.save(userRequest));
    }

    // UPDATE user
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('UPDATE_USER')")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        return ResponseHandler.generateResponse("Success update user", HttpStatus.OK,
                userService.update(id, userRequest));
    }

    // DELETE user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('DELETE_USER')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseHandler.generateResponse("Success delete user", HttpStatus.OK, null);
    }

    // UPDATE roles for a user
    @PutMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('UPDATE_USER')")
    public ResponseEntity<Object> updateUserRoles(@PathVariable Long id,
            @RequestBody Collection<String> roleNames) {
        return ResponseHandler.generateResponse("Success update user roles", HttpStatus.OK,
                userService.updateUserRoles(id, roleNames));
    }
}
