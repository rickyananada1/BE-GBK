package com.dev.gbk.repository;

import java.util.List;
import java.util.Optional;

import com.dev.gbk.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(String name);

	List<Role> findAll();
}
