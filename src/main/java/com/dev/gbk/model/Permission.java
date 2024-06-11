package com.dev.gbk.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "role_permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`role_id`")
    private Integer role_id;

    @Column(name = "`permission_id`")
    private Integer permission_id;

    @Column(name = "`create`")
    private Boolean create;

    @Column(name = "`read`")
    private Boolean read;

    @Column(name = "`update`")
    private Boolean update;

    @Column(name = "`delete`")
    private Boolean delete;

    public Permission() {
    }

    public Permission(Integer role_id, Integer permission_id, Boolean create, Boolean read, Boolean update, Boolean delete) {
        this.role_id = role_id;
        this.permission_id = permission_id;
        this.create = create;
        this.read = read;
        this.update = update;
        this.delete = delete;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }

    public Integer getPermission_id() {
        return permission_id;
    }

    public void setPermission_id(Integer permission_id) {
        this.permission_id = permission_id;
    }

    public Boolean getCreate() {
        return create;
    }

    public void setCreate(Boolean create) {
        this.create = create;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getUpdate() {
        return update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }
}