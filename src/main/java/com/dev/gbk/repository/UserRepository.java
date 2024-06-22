package com.dev.gbk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dev.gbk.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	Optional<User> findByUsernameOrEmail(String username, String email);

	Optional<User> findByUsername(String username);

	boolean existsByUsernameAndIdNot(String username, Long id);

	boolean existsByUsername(String username);

	boolean existsByEmailAndIdNot(String email, Long id);

	boolean existsByEmail(String email);

	boolean existsByUsernameOrEmail(String username, String email);

	boolean existsByUsernameOrEmailAndIdNot(String username, String email, Long id);
}
