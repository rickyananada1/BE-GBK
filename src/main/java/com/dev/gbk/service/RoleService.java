package com.dev.gbk.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dev.gbk.model.Permission;
import com.dev.gbk.repository.PermissionRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dev.gbk.dto.RoleRequest;
import com.dev.gbk.exception.ResourceNotFoundException;
import com.dev.gbk.model.Role;
import com.dev.gbk.repository.RoleRepository;
import com.dev.gbk.spesification.SpecificationBuilderImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final SpecificationBuilderImpl<Role> specificationBuilder = new SpecificationBuilderImpl<>(
            new ObjectMapper(), Role.class);

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Page<Role> findAll(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Optional<Specification<Role>> specification = specificationBuilder.parseAndBuild(search);
        return specification.isPresent() ? roleRepository.findAll(specification.get(), pageable)
                : roleRepository.findAll(pageable);
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    public Role findById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    public void save(RoleRequest roleRequest) {
        if (roleRepository.existsByName(roleRequest.getName())) {
            throw new ResourceNotFoundException("Role already exists");
        }
        Role role = Role.builder().name(roleRequest.getName()).build();
        roleRepository.save(role);
    }

    public void update(Long id, RoleRequest roleRequest) {
        Role role = findById(id);
        role.setName(roleRequest.getName());
        roleRepository.save(role);
    }

    public void delete(Long id) {
        findById(id);
        roleRepository.deleteById(id);
    }

    public void updateRolePermissions(Long id, List<String> permissionNames) {
        Role role = findById(id);
        List<Permission> permissions = permissionNames.stream()
                .map(name -> permissionRepository.findByName(name).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        role.setPermissions(permissions);
        roleRepository.save(role);
    }
}
