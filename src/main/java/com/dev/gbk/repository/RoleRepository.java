package com.dev.gbk.repository;

import java.util.Optional;

import com.dev.gbk.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
	Optional<Role> findByName(String name);

	Boolean existsByName(String name);
}
