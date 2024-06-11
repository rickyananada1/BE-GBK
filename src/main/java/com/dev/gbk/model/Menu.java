 package com.dev.gbk.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 60)
    private String menu;

    public Menu() {

    }

    public Menu(String menu) {
        this.menu = menu;
    }

    public Long getId() {
        return id;
    }

    public String getMenu() {
        return menu;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

}