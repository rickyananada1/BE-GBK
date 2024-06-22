package com.dev.gbk.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.dev.gbk.dto.UserRequest;
import com.dev.gbk.model.User;
import com.dev.gbk.service.UserService;

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
    public ResponseEntity<Page<User>> getUsers(@RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return ResponseEntity.ok(userService.findAll(search, page, size));
    }

    // GET user by username
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_USER')")
    public ResponseEntity<User> getUserByUsername(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    // CREATE new user
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CREATE_USER')")
    public ResponseEntity<HttpStatus> store(@RequestBody UserRequest userRequest) {
        userService.save(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // UPDATE user
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('UPDATE_USER')")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        userService.update(id, userRequest);
        return ResponseEntity.ok().build();
    }

    // DELETE user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('DELETE_USER')")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // UPDATE roles for a user
    @PutMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('UPDATE_USER')")
    public ResponseEntity<HttpStatus> updateUserRoles(@PathVariable Long id,
            @RequestBody Collection<String> roleNames) {
        userService.updateUserRoles(id, roleNames);
        return ResponseEntity.ok().build();
    }
}
