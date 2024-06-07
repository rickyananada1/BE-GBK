package com.dev.gbk.model;

import javax.persistence.*;

import org.hibernate.annotations.NaturalId;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private RoleName name;

	@JoinTable(
			name = "roles_permissions",
			joinColumns = {@JoinColumn(name = "roles_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "permissions_id", referencedColumnName = "id")}
	)
	@ManyToMany
	private Set<Permission> permissions = new HashSet<>();

	public Set<Permission> getPermissions() {
		return permissions;
	}

	void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public Role() {

    }

    public Role(RoleName name) {
        this.name = name;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RoleName getName() {
		return name;
	}

	public void setName(RoleName name) {
		this.name = name;
	}

}
