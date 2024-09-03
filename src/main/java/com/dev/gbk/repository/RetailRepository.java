package com.dev.gbk.repository;

import com.dev.gbk.dto.CardRetailDTO;
import com.dev.gbk.model.Retail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        Double getOverallPercentage();

        @Query("SELECT new com.dev.gbk.dto.CardRetailDTO("
                + "r.masterRetail.tenant_name, "
                + "r.masterRetail.area, "
                + "(SUM(CASE WHEN r.statusBooking = 'Sewa' THEN r.price ELSE 0 END) / SUM(r.price)) * 100) "
                + "FROM Retail r "
                + "WHERE r.statusBooking = 'Sewa' "
                + "GROUP BY r.masterRetail.tenant_name, r.masterRetail.area")
        List<CardRetailDTO> getRetailCardData();

        @Query(value = "SELECT "
            + "SUM(CASE WHEN r.status_booking = 'SEWA' THEN CAST(r.size AS DECIMAL(10, 2)) ELSE 0 END) AS total_size_paid,"
            + "SUM(CAST(r.size AS DECIMAL(10, 2))) AS total_size_all"
            + " FROM retails r"
            + " JOIN master_retails mr ON r.master_retail_id = mr.id" + " WHERE mr.area = :venue"
            + " AND CAST(CONCAT(r.month, '-01') AS DATE) >= :startDate " + " AND CAST(CONCAT(r.month, '-01') AS DATE) <= :endDate" , nativeQuery = true)
        List<Object[]> findSumPaidAndAllRecordForRetail(@Param("venue") String venue,
            @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
