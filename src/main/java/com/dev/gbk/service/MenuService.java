package com.dev.gbk.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dev.gbk.model.Menu;
import com.dev.gbk.model.Permission;
import com.dev.gbk.model.User;
import com.dev.gbk.repository.MenuRepository;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final UserService userService;
    private final RoleService roleService;

    public MenuService(MenuRepository menuRepository, UserService userService, RoleService roleService) {
        this.menuRepository = menuRepository;
        this.userService = userService;
        this.roleService = roleService;
    }

    public Optional<Menu> findByName(String name) {
        return menuRepository.findByName(name);
    }

    public Menu save(Menu menu) {
        // check if menu exists
        if (this.findByName(menu.getName()).isPresent()) {
            throw new RuntimeException("Menu already exists");
        }
        return menuRepository.save(menu);
    }

    // update
    public void update(Menu menu) {
        menuRepository.save(menu);
    }

    public List<Menu> findAll() {
        User currentUser = userService.getCurrentUser();
        Set<Permission> permissions = currentUser.getRoles().stream()
                .flatMap(role -> roleService.getPermissionsByRole(role.getName()).stream())
                .collect(Collectors.toSet());

        return menuRepository.findAll().stream()
                .filter(menu -> permissions.stream()
                        .anyMatch(permission -> permission.getMenu().equals(menu)))
                .collect(Collectors.toList());
    }
}
