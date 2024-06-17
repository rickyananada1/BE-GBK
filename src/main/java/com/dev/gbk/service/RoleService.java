package com.dev.gbk.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.dev.gbk.model.Permission;
import com.dev.gbk.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import com.dev.gbk.dto.RoleRequest;
import com.dev.gbk.exception.ResourceNotFoundException;
import com.dev.gbk.model.Role;
import com.dev.gbk.repository.RoleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    public void save(RoleRequest roleRequest) {
        if (roleRepository.existsByName(roleRequest.getName())) {
            throw new ResourceNotFoundException("Role already exists");
        }
        Role role = Role.builder().name(roleRequest.getName()).build();
        roleRepository.save(role);
    }

    public void update(String name, RoleRequest roleRequest) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        role.setName(roleRequest.getName());
        roleRepository.save(role);
    }

    public void delete(String name) {
        Role role = roleRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        if (role == null) {
            throw new ResourceNotFoundException("Role not found");
        }
        roleRepository.delete(role);
    }

    public void updateRolePermissions(String roleName, List<String> permissionNames) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        List<Permission> permissions = permissionNames.stream()
                .map(name -> permissionRepository.findByName(name).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        role.setPermissions(permissions);
        roleRepository.save(role);
    }
}
