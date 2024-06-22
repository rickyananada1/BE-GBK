package com.dev.gbk.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dev.gbk.dto.LoginRequest;
import com.dev.gbk.dto.UserRequest;
import com.dev.gbk.exception.GBKAPIException;
import com.dev.gbk.exception.ResourceNotFoundException;
import com.dev.gbk.model.Role;
import com.dev.gbk.model.User;
import com.dev.gbk.repository.RoleRepository;
import com.dev.gbk.repository.UserRepository;
import com.dev.gbk.security.JwtTokenProvider;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(AuthenticationManager authenticationManager,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }

    public void register(UserRequest userRequest) {

        // add check for username exists in database
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new GBKAPIException("Username is already exists!.");
        }

        // add check for email exists in database
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new GBKAPIException("Email is already exists!.");
        }

        User user = new User();
        user.setName(userRequest.getName());
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
        for (String role : userRequest.getRoles()) {
            if (!roleRepository.existsByName(role)) {
                throw new ResourceNotFoundException("Role not found");
            }
            roles.add(roleRepository.findByName(role).get());
        }
        user.setRoles(roles);
        userRepository.save(user);

    }
}