package com.dev.gbk.service;

import org.springframework.stereotype.Service;

import com.dev.gbk.model.Permission;
import com.dev.gbk.repository.PermissionRepository;

import java.util.Optional;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Iterable<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public void save(Permission permission) {
        if (this.findByName(permission.getName()).isPresent()) {
            throw new RuntimeException("Permission already exists");
        }
        permissionRepository.save(permission);
    }

    public Optional<Permission> findByName(String name) {
        return permissionRepository.findByName(name);
    }
}
