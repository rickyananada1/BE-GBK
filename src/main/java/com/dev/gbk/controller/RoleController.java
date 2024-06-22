package com.dev.gbk.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.dev.gbk.dto.RoleRequest;
import com.dev.gbk.model.Role;
import com.dev.gbk.service.RoleService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // GET all roles
    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_ROLE')")
    public ResponseEntity<Page<Role>> getAllRoles(@RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return ResponseEntity.ok(roleService.findAll(search, page, size));
    }

    // GET role by name
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    public ResponseEntity<Role> getRoleByName(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    // CREATE new role
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    public ResponseEntity<HttpStatus> store(@RequestBody RoleRequest roleRequest) {
        roleService.save(roleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // UPDATE role
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_ROLE')")
    public ResponseEntity<HttpStatus> updateRole(@PathVariable Long id, @RequestBody RoleRequest roleRequest) {
        roleService.update(id, roleRequest);
        return ResponseEntity.ok().build();
    }

    // DELETE role
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    public ResponseEntity<HttpStatus> deleteRole(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // UPDATE permissions for a role
    @PutMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('UPDATE_ROLE')")
    public ResponseEntity<HttpStatus> updateRolePermissions(@PathVariable Long id,
            @RequestBody List<String> permissionNames) {
        roleService.updateRolePermissions(id, permissionNames);
        return ResponseEntity.ok().build();
    }
}
