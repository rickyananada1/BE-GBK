package com.dev.gbk.controller;

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
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET all users
    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<Collection<User>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    // GET user by username
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<User> getUserByUsername(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    // CREATE new user
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_USER')")
    public ResponseEntity<User> store(@RequestBody UserRequest userRequest) {
        userService.save(userRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // UPDATE user
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        userService.update(id, userRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // DELETE user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // UPDATE roles for a user
    @PutMapping("/{id}/roles")
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    public ResponseEntity<User> updateUserRoles(@PathVariable Long id,
            @RequestBody Collection<String> roleNames) {
        userService.updateUserRoles(id, roleNames);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
