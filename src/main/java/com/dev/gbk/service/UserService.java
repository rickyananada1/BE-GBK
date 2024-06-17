package com.dev.gbk.service;

import com.dev.gbk.repository.RoleRepository;
import org.springframework.stereotype.Service;

import com.dev.gbk.dto.UserRequest;
import com.dev.gbk.model.Role;
import com.dev.gbk.model.User;
import com.dev.gbk.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void save(UserRequest userRequest) {
        // check if user exists
        if (userRepository.findByUsernameOrEmail(userRequest.getUsername(), userRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        User user = User.builder().name(userRequest.getName()).username(userRequest.getUsername())
                .email(userRequest.getEmail()).password(userRequest.getPassword()).build();

        userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void update(Long id, UserRequest userRequest) {
        User user = this.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setName(userRequest.getName());
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        userRepository.save(user);
    }

    public void delete(Long id) {
        // check if user exists
        if (this.findById(id).isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void updateUserRoles(Long id, Collection<String> roleNames) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Collection<Role> roles = roleNames.stream()
                .map(name -> roleRepository.findByName(name).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        user.setRoles(roles);
    }
}
