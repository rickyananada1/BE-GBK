package com.dev.gbk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import com.dev.gbk.model.Unit;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long>, JpaSpecificationExecutor<Unit> {

    @Query("SELECT u FROM Unit u WHERE LOWER(u.name) = LOWER(:name)")
    Optional<Unit> findByName(@Param("name") String name);
}
