package com.dev.gbk.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dev.gbk.model.Menu;
import com.dev.gbk.repository.MenuRepository;

@Service
public class MenuService {
    private MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
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

    public Iterable<Menu> findAll() {
        return menuRepository.findAll();
    }

}
