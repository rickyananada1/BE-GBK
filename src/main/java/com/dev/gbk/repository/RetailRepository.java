package com.dev.gbk.repository;

import com.dev.gbk.dto.CardRetailDTO;
import com.dev.gbk.model.Retail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RetailRepository extends JpaRepository<Retail, Long>, JpaSpecificationExecutor<Retail> {

        @Query("SELECT SUM(r.price) FROM Retail r " +
                        "WHERE r.statusBooking = :status AND " +
                        "(:area IS NULL OR r.masterRetail.area = :area)")
        Double sumPriceByStatusAndDateRangeAndArea(@Param("status") String status,
                        @Param("area") String area);

        @Query("SELECT SUM(r.size) FROM Retail r " +
                        "WHERE r.statusBooking = :status AND " +
                        "(:area IS NULL OR r.masterRetail.area = :area)")
        Double sumSizeByStatusAndDateRangeAndArea(@Param("status") String status,
                        @Param("area") String area);

        @Query("SELECT (COUNT(CASE WHEN r.statusBooking = 'Sewa' THEN 1 ELSE NULL END) / " +
                "(COUNT(*) * 1.0)) * 100 AS percentage " +
                "FROM Retail r")
        Double findRetailCardData();

}
