package com.dev.gbk.service;

import org.springframework.stereotype.Service;

import com.dev.gbk.exception.ResourceNotFoundException;
import com.dev.gbk.model.Permission;
import com.dev.gbk.repository.PermissionRepository;

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
        if (permissionRepository.findByName(permission.getName()).isPresent()) {
            throw new RuntimeException("Permission already exists");
        }
        permissionRepository.save(permission);
    }

    public Permission findByName(String name) {
        return permissionRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
    }
}
