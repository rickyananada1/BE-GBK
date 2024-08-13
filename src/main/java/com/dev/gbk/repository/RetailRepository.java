package com.dev.gbk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dev.gbk.model.Retail;

public interface RetailRepository extends JpaRepository<Retail, Long>, JpaSpecificationExecutor<Retail> {
    @Query("SELECT SUM(r.price) FROM Retail r WHERE r.statusBooking = :status")
    Double sumPriceByStatus(@Param("status") String status);

    @Query("SELECT SUM(r.size) FROM Retail r WHERE r.statusBooking = :status")
    Double sumSizeByStatus(@Param("status") String status);
}
