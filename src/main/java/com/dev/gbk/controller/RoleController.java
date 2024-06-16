package com.dev.gbk.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dev.gbk.model.Permission;
import com.dev.gbk.model.Role;
import com.dev.gbk.service.PermissionService;
import com.dev.gbk.service.RoleService;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    private final PermissionService permissionService;

    public RoleController(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    // GET all roles
    @GetMapping
    public ResponseEntity<Collection<Role>> getAllRoles() {
        return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
    }

    // GET role by name
    @GetMapping("/{name}")
    public ResponseEntity<Role> getRoleByName(@PathVariable String name) {
        Optional<Role> role = roleService.findByName(name);
        return role.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // CREATE new role
    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        roleService.save(role);
        return new ResponseEntity<>(role, HttpStatus.CREATED);
    }

    // UPDATE role
    @PutMapping("/{name}")
    public ResponseEntity<Role> updateRole(@PathVariable String name, @RequestBody Role roleDetails) {
        Optional<Role> role = roleService.findByName(name);
        if (role.isPresent()) {
            Role updatedRole = role.get();
            updatedRole.setName(roleDetails.getName());
            updatedRole.setPermissions(roleDetails.getPermissions());
            roleService.save(updatedRole);
            return new ResponseEntity<>(updatedRole, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE role
    @DeleteMapping("/{name}")
    public ResponseEntity<HttpStatus> deleteRole(@PathVariable String name) {
        Optional<Role> role = roleService.findByName(name);
        if (role.isPresent()) {
            roleService.delete(role.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // UPDATE permissions for a role
    @PutMapping("/{roleName}/permissions")
    public ResponseEntity<Role> updateRolePermissions(@PathVariable String roleName,
            @RequestBody Collection<String> permissionNames) {
        Optional<Role> role = roleService.findByName(roleName);
        if (role.isPresent()) {
            Role existingRole = role.get();
            Collection<Permission> permissions = permissionNames.stream()
                    .map(name -> permissionService.findByName(name).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            existingRole.setPermissions(permissions);
            roleService.save(existingRole);
            return new ResponseEntity<>(existingRole, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
