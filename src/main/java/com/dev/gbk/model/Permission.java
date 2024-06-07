package com.dev.gbk.model;

import javax.persistence.*;

@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    private String id;

    @Column(name = "name")
    private String name;

    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }
}