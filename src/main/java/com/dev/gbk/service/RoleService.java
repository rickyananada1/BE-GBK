package com.dev.gbk.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dev.gbk.model.Role;
import com.dev.gbk.repository.RoleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    public void save(Role role) {
        if (this.findByName(role.getName()).isPresent()) {
            throw new RuntimeException("Role already exists");
        }
        roleRepository.save(role);
    }

    public void delete(Role role) {
        roleRepository.delete(role);
    }
}
