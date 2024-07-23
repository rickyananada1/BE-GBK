package com.dev.gbk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dev.gbk.model.Retail;

public interface RetailRepository extends JpaRepository<Retail, Long>, JpaSpecificationExecutor<Retail> {
    // find all by area
    List<Retail> findAllByArea(String area);

    @Query("SELECT r FROM Retail r WHERE r.tenant_name = ?1")
    Optional<Retail> findByTenantName(String name);

    @Query("SELECT r FROM Retail r WHERE r.tenant_number = ?1")
    Optional<Retail> findByTenantNumber(String number);

    @Query("SELECT SUM(r.price) FROM Retail r WHERE r.status = :status")
    double sumPriceByStatus(@Param("status") String status);

    @Query("SELECT SUM(r.size) FROM Retail r WHERE r.status = :status")
    double sumSizeByStatus(@Param("status") String status);
}
