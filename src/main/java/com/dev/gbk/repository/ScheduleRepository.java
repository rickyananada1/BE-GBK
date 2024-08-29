package com.dev.gbk.repository;

import com.dev.gbk.model.Schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {
        List<Schedule> findByStatusPaymentAndCreatedAtBefore(String statusPayment, LocalDateTime dateTime);

        List<Schedule> findByStatusPaymentAndVenuesIdAndCreatedAtBefore(
                        String statusPayment, Long venueId, LocalDateTime createdAt);

        boolean existsByBookingNumber(String bookingNumber);

        Schedule findTopByOrderByIdDesc();

        @Query("SELECT s.profileEvent, SUM(s.totalPaid) FROM Schedule s JOIN s.venues v JOIN v.unit u WHERE (:unitNames IS NULL OR u.name IN :unitNames) AND s.statusPayment = 'Paid' AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate GROUP BY s.profileEvent")
        List<Object[]> sumTotalPaidGroupedByProfileEventAndDates(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("unitNames") List<String> unitNames);

        @Query("SELECT s.category, SUM(s.totalPaid) FROM Schedule s JOIN s.venues v JOIN v.unit u WHERE (:unitNames IS NULL OR u.name IN :unitNames) AND s.statusPayment = 'Paid' AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate AND s.category IS NOT NULL GROUP BY s.category")
        List<Object[]> sumTotalPaidGroupedByCategoryAndDates(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("unitNames") List<String> unitNames);

        @Query("SELECT s.profileEvent, SUM(s.totalPaid) FROM Schedule s JOIN s.venues v JOIN v.unit u WHERE (:unitNames IS NULL OR u.name IN :unitNames) AND s.statusBooking = 'Soft Booking' AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate GROUP BY s.profileEvent")
        List<Object[]> sumSoftBookingTotalPaidGroupedByProfileEventAndDates(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("unitNames") List<String> unitNames);

        @Query("SELECT s.category, SUM(s.totalPaid) FROM Schedule s JOIN s.venues v JOIN v.unit u WHERE (:unitNames IS NULL OR u.name IN :unitNames) AND s.statusBooking = 'Soft Booking' AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate GROUP BY s.category")
        List<Object[]> sumSoftBookingTotalPaidGroupedByCategoryAndDates(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("unitNames") List<String> unitNames);

        @Query("SELECT s.scheduleStartDate, SUM(s.totalPaid) FROM Schedule s " +
                        "JOIN s.venues v JOIN v.unit u " +
                        "WHERE (:unitNames IS NULL OR u.name IN :unitNames) " +
                        "AND s.statusBooking = 'Maintenance' AND s.scheduleStartDate BETWEEN :startDate AND :endDate " +
                        "GROUP BY s.scheduleStartDate")
        List<Object[]> sumMaintenanceByDay(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("unitNames") List<String> unitNames);

        @Query("SELECT SUM(s.totalPaid) FROM Schedule s JOIN s.venues v JOIN v.unit u WHERE u.name IN :unitNames AND s.statusPayment = 'Maintenance' "
                        + "AND ((:startDate IS NULL AND :endDate IS NULL AND s.scheduleStartDate IS NOT NULL) "
                        + "OR (:startDate IS NOT NULL AND :endDate IS NOT NULL AND "
                        + "(s.scheduleStartDate IS NOT NULL AND s.scheduleEndDate IS NOT NULL AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate)))")
        Double sumMaintenanceByType(@Param("unitNames") List<String> unitNames,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);


        @Query("SELECT " +
                "    CASE " +
                "        WHEN r.category = 'Sewa Lahan' AND r.statusPayment = 'Paid' AND r.statusBooking = 'Processing' THEN 'Sewa Lahan' " +
                "        WHEN (r.games = 'Umum') AND r.statusPayment = 'Paid' AND r.statusBooking = 'Processing' THEN 'Games Umum' " +
                "        WHEN (r.games = 'Timnas') AND r.statusPayment = 'Paid' AND r.statusBooking = 'Processing' THEN 'Games Timnas' " +
                "        WHEN r.statusPayment = 'Maintenance' THEN 'Maintenance' " +
                "        WHEN r.category IS NOT NULL AND r.category != '' AND r.category != 'Sewa Lahan' AND r.statusPayment = 'Paid' AND r.statusBooking = 'Processing' THEN 'Events' " +
                "        WHEN r.category = 'Sewa Lahan' AND r.statusPayment != 'Paid' AND r.statusBooking = 'Processing' THEN 'Sewa Lahan Proyeksi' " +
                "        WHEN (r.games = 'Umum') AND r.statusPayment != 'Paid' AND r.statusBooking = 'Processing' THEN 'Games Umum Proyeksi' " +
                "        WHEN (r.games = 'Timnas') AND r.statusPayment != 'Paid' AND r.statusBooking = 'Processing' THEN 'Games Timnas Proyeksi' " +
                "        WHEN r.statusPayment = 'Maintenance' THEN 'Maintenance' " +
                "        WHEN r.category IS NOT NULL AND r.category != '' AND r.category != 'Sewa Lahan' AND r.statusPayment != 'Paid' AND r.statusBooking = 'Processing' THEN 'Events Proyeksi' " +
                "    END AS kategori, " +
                "    SUM(r.totalPaid) + SUM(r.totalSF) AS total " +
                "FROM Schedule r " +
                "WHERE r.scheduleStartDate >= :startDate AND r.scheduleEndDate <= :endDate " +
                "GROUP BY kategori")
        List<Object> sumSewaLahanByStatusPayment(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);


        @Query("SELECT s FROM Schedule s " +
                        "JOIN s.venues v " +
                        "WHERE (:units IS NULL OR v.unit.name IN :units) " +
                        "AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate AND s.statusPayment != 'Maintenance'")
        List<Schedule> findSchedules(@Param("units") List<String> units, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT s FROM Schedule s JOIN s.venues v WHERE v.venue = :unit AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate")
        List<Schedule> findSingleSchedules(@Param("unit") String unit, @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

}
