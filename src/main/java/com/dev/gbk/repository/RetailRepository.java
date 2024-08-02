package com.dev.gbk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dev.gbk.dto.CardRetailDTO;
import com.dev.gbk.model.Retail;

public interface RetailRepository extends JpaRepository<Retail, Long>, JpaSpecificationExecutor<Retail> {
    // @Query("SELECT SUM(r.price) FROM Retail r WHERE r.status = :status")
    // Double sumPriceByStatus(@Param("status") String status);

    // @Query("SELECT SUM(r.size) FROM Retail r WHERE r.status = :status")
    // Double sumSizeByStatus(@Param("status") String status);

    // @Query("SELECT new com.dev.gbk.dto.CardRetailDTO("
    // + "r.masterRetail.tenant_name, " // Access tenant_name through masterRetail
    // + "r.masterRetail.area, "
    // + "(SUM(CASE WHEN r.status = 'Sewa' THEN r.price ELSE 0 END) / SUM(r.price))
    // * 100) "
    // + "FROM Retail r "
    // + "WHERE r.status = 'Sewa' "
    // + "GROUP BY r.masterRetail.tenant_name, r.masterRetail.area") // Group by
    // tenant_name from masterRetail
    // List<CardRetailDTO> findRetailCardData();
}
