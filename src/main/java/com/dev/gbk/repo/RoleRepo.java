package com.dev.gbk.repo;

import java.util.Optional;

import com.dev.gbk.model.Role;
import com.dev.gbk.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

	Optional<Role> findByName(RoleName roleName);
}
