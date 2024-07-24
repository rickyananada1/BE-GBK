package com.dev.gbk.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dev.gbk.dto.CardRetailDTO;
import com.dev.gbk.model.Retail;

public interface RetailRepository extends JpaRepository<Retail, Long>, JpaSpecificationExecutor<Retail> {
    // find all by area
    List<Retail> findAllByArea(String area);

    @Query("SELECT r FROM Retail r WHERE r.tenant_name = ?1")
    Optional<Retail> findByTenantName(String name);

    @Query("SELECT r FROM Retail r WHERE r.tenant_number = ?1")
    Optional<Retail> findByTenantNumber(String number);

    @Query("SELECT SUM(r.price) FROM Retail r WHERE r.status = :status")
    Double sumPriceByStatus(@Param("status") String status);

    @Query("SELECT SUM(r.size) FROM Retail r WHERE r.status = :status")
    Double sumSizeByStatus(@Param("status") String status);

    @Query("SELECT new com.dev.gbk.dto.CardRetailDTO(r.tenant_name, (SUM(CASE WHEN r.status = 'SEWA' THEN r.price ELSE 0 END) / SUM(r.price)) * 100) "
            + "FROM Retail r WHERE r.status = 'SEWA'"
            + "GROUP BY r.tenant_name")
    List<CardRetailDTO> findRetailCardData();
}
