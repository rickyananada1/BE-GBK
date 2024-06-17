package com.dev.gbk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dev.gbk.model.Retail;

public interface RetailRepository extends JpaRepository<Retail, Long> {
    // find all by area
    List<Retail> findAllByArea(String area);

    @Query("SELECT v FROM Retail v WHERE v.tenant_name = ?1")
    Optional<Retail> findByTenantName(String name);

    @Query("SELECT v FROM Retail v WHERE v.tenant_number = ?1")
    Optional<Retail> findByTenantNumber(String number);
}
