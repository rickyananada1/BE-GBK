package com.dev.gbk.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.dev.gbk.model.Role;
import com.dev.gbk.model.User;
import com.dev.gbk.service.RoleService;
import com.dev.gbk.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    // GET all users
    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<Collection<User>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    // GET user by username
    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // CREATE new user
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_USER')")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // UPDATE user
    @PutMapping("/{username}")
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User userDetails) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            User updatedUser = user.get();
            updatedUser.setName(userDetails.getName());
            updatedUser.setEmail(userDetails.getEmail());
            updatedUser.setPassword(userDetails.getPassword());
            updatedUser.setRoles(userDetails.getRoles());
            userService.save(updatedUser);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE user
    @DeleteMapping("/{username}")
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            userService.deleteById(user.get().getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // UPDATE roles for a user
    @PutMapping("/{username}/roles")
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    public ResponseEntity<User> updateUserRoles(@PathVariable String username,
            @RequestBody Collection<String> roleNames) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            User existingUser = user.get();
            Collection<Role> roles = roleNames.stream()
                    .map(name -> roleService.findByName(name).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            existingUser.setRoles(roles);
            userService.save(existingUser);
            return new ResponseEntity<>(existingUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
