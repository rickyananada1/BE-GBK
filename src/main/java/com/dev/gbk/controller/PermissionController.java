package com.dev.gbk.controller;

import com.dev.gbk.model.Permission;
import com.dev.gbk.repo.PermissionRepo;
import com.dev.gbk.service.GbkFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import payloads.ApiResponse;
import payloads.PermissionRequest;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class PermissionController {
    @Autowired
    private PermissionRepo permissionRepo;

    @Autowired
    GbkFeignClient gbkFeignClient;

    @Autowired
    Environment env;

    @GetMapping("/permissions")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getAllPermissions() {
        return ResponseEntity.ok(permissionRepo.findAll());
    }

    @PostMapping("/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createPermissions(@Valid @RequestBody PermissionRequest permissionRequest) {
        Permission permission = new Permission(permissionRequest.getRole_id(), permissionRequest.getPermission_id(),
                permissionRequest.getCreate(), permissionRequest.getRead(), permissionRequest.getUpdate(),
                permissionRequest.getDelete());

        Permission result = permissionRepo.save(permission);
        System.out.println(result);
        return ResponseEntity.ok(new ApiResponse(true, "Permission created successfully"));
    }
}