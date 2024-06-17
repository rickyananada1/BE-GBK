package com.dev.gbk.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.dev.gbk.dto.RoleRequest;
import com.dev.gbk.model.Role;
import com.dev.gbk.service.PermissionService;
import com.dev.gbk.service.RoleService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
    }

    // GET all roles
    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_ROLE')")
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
    }

    // GET role by name
    @GetMapping("/{name}")
    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    public ResponseEntity<Role> getRoleByName(@PathVariable String name) {
        return new ResponseEntity<>(roleService.findByName(name), HttpStatus.OK);
    }

    // CREATE new role
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    public ResponseEntity<HttpStatus> store(@RequestBody RoleRequest roleRequest) {
        roleService.save(roleRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // UPDATE role
    @PutMapping("/{name}")
    @PreAuthorize("hasAuthority('UPDATE_ROLE')")
    public ResponseEntity<HttpStatus> updateRole(@PathVariable String name, @RequestBody RoleRequest roleRequest) {
        roleService.update(name, roleRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // DELETE role
    @DeleteMapping("/{name}")
    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    public ResponseEntity<HttpStatus> deleteRole(@PathVariable String name) {
        roleService.delete(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // UPDATE permissions for a role
    @PutMapping("/{roleName}/permissions")
    @PreAuthorize("hasAuthority('UPDATE_ROLE')")
    public ResponseEntity<HttpStatus> updateRolePermissions(@PathVariable String roleName,
            @RequestBody List<String> permissionNames) {
        roleService.updateRolePermissions(roleName, permissionNames);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
