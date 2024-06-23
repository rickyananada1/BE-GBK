package com.dev.gbk.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.dev.gbk.dto.RoleRequest;
import com.dev.gbk.service.RoleService;
import com.dev.gbk.utils.ResponseHandler;

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
    public ResponseEntity<Object> getAllRoles(@RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return ResponseHandler.generateResponse("Success get all roles", HttpStatus.OK,
                roleService.findAll(search, page, size));
    }

    // GET role by name
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    public ResponseEntity<Object> getRoleByName(@PathVariable Long id) {
        return ResponseHandler.generateResponse("Success get role by name", HttpStatus.OK, roleService.findById(id));
    }

    // CREATE new role
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    public ResponseEntity<Object> store(@RequestBody RoleRequest roleRequest) {

        return ResponseHandler.generateResponse("Success create role", HttpStatus.CREATED,
                roleService.save(roleRequest));
    }

    // UPDATE role
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_ROLE')")
    public ResponseEntity<Object> updateRole(@PathVariable Long id, @RequestBody RoleRequest roleRequest) {

        return ResponseHandler.generateResponse("Success update role", HttpStatus.OK,
                roleService.update(id, roleRequest));
    }

    // DELETE role
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    public ResponseEntity<Object> deleteRole(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseHandler.generateResponse("Success delete role", HttpStatus.OK, null);
    }

    // UPDATE permissions for a role
    @PutMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('UPDATE_ROLE')")
    public ResponseEntity<Object> updateRolePermissions(@PathVariable Long id,
            @RequestBody List<String> permissionNames) {
        return ResponseHandler.generateResponse("Success update role permissions", HttpStatus.OK,
                roleService.updateRolePermissions(id, permissionNames));
    }
}
