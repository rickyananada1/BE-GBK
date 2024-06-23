package com.dev.gbk.service;

import com.dev.gbk.repository.RoleRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dev.gbk.dto.UserRequest;
import com.dev.gbk.exception.GBKAPIException;
import com.dev.gbk.exception.ResourceNotFoundException;
import com.dev.gbk.model.Role;
import com.dev.gbk.model.User;
import com.dev.gbk.repository.UserRepository;
import com.dev.gbk.spesification.SpecificationBuilderImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final SpecificationBuilderImpl<User> specificationBuilder = new SpecificationBuilderImpl<>(
            new ObjectMapper(), User.class);

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<User> findAll(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Optional<Specification<User>> specification = specificationBuilder.parseAndBuild(search);
        return specification.map(userSpecification -> userRepository.findAll(userSpecification, pageable))
                .orElseGet(() -> userRepository.findAll(pageable));
    }

    public User save(UserRequest userRequest) {
        // check if user exists
        if (userRepository.existsByUsernameOrEmail(userRequest.getUsername(), userRequest.getEmail())) {
            throw new GBKAPIException("User already exists");
        }
        User user = User.builder().name(userRequest.getName()).username(userRequest.getUsername())
                .email(userRequest.getEmail()).password(passwordEncoder.encode(userRequest.getPassword()))
                .contact_person(userRequest.getContact_person()).division(userRequest.getDivision())
                .unit(userRequest.getUnit()).status(userRequest.getStatus())
                .roles(userRequest.getRoles().stream().map(roleName -> roleRepository.findByName(roleName).orElse(null))
                        .filter(Objects::nonNull).collect(Collectors.toList()))
                .unit(userRequest.getUnit()).build();

        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User update(Long id, UserRequest userRequest) {
        if (userRepository.existsByUsernameAndIdNot(userRequest.getUsername(), id)) {
            throw new GBKAPIException("Username is already exists!.");
        }

        // add check for email exists in database
        if (userRepository.existsByEmailAndIdNot(userRequest.getEmail(), id)) {
            throw new GBKAPIException("Email is already exists!.");
        }

        User user = findById(id);
        user.setName(userRequest.getName());
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setContact_person(userRequest.getContact_person());
        user.setDivision(userRequest.getDivision());
        user.setStatus(userRequest.getStatus());
        user.setRoles(userRequest.getRoles().stream().map(roleName -> roleRepository.findByName(roleName).orElse(null))
                .filter(Objects::nonNull).collect(Collectors.toList()));
        user.setUnit(userRequest.getUnit());
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.deleteById(id);
    }

    public User updateUserRoles(Long id, Collection<String> roleNames) {
        User user = findById(id);
        Collection<Role> roles = roleNames.stream()
                .map(name -> roleRepository.findByName(name).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        user.setRoles(roles);
        return userRepository.save(user);
    }
}
