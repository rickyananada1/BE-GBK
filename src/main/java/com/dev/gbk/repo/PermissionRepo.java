package com.dev.gbk.repo;

import java.util.List;
import java.util.Optional;

import com.dev.gbk.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepo extends JpaRepository<Permission, Long> {

//    Optional<Role> findByName(RoleName roleName);
}
