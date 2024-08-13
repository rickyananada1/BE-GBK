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
        List<Schedule> findByStatusBookingAndCreatedAtBefore(String status, LocalDateTime dateTime);

        List<Schedule> findByStatusBookingAndVenuesIdAndCreatedAtBefore(
                        String statusBooking, Long venueId, LocalDateTime createdAt);

        boolean existsByBookingNumber(String bookingNumber);

        @Query("SELECT s FROM Schedule s JOIN s.venues v JOIN v.unit u WHERE u.name = :unitName AND s.statusPayment = 'Paid' AND s.createdAt <= :createdAt")
        List<Schedule> findByUnitNameAndStatusPaymentAndCreatedAtBefore(String unitName, LocalDateTime createdAt);

        @Query("SELECT s FROM Schedule s JOIN s.venues v JOIN v.unit u WHERE u.name = :unitName AND s.statusPayment = 'Paid'")
        List<Schedule> findByUnitNameAndStatusPayment(String unitName);

        List<Schedule> findByStatusPaymentAndCreatedAtBefore(String statusPayment, LocalDateTime createdAt);

        Schedule findTopByOrderByIdDesc();

        @Query("SELECT s.profileEvent, SUM(s.totalPaid) FROM Schedule s JOIN s.venues v JOIN v.unit u WHERE u.name = :unitName AND s.statusPayment = 'Paid' AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate GROUP BY s.profileEvent")
        List<Object[]> sumTotalPaidGroupedByProfileEventAndUnitAndDates(String unitName, LocalDateTime startDate,
                        LocalDateTime endDate);

        @Query("SELECT s.category, SUM(s.totalPaid) FROM Schedule s JOIN s.venues v JOIN v.unit u WHERE u.name = :unitName AND s.statusPayment = 'Paid' AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate GROUP BY s.category")
        List<Object[]> sumTotalPaidGroupedByCategoryAndUnitAndDates(String unitName, LocalDateTime startDate,
                        LocalDateTime endDate);

        @Query("SELECT s.profileEvent, SUM(s.totalPaid) FROM Schedule s WHERE s.statusPayment = 'Paid' AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate GROUP BY s.profileEvent")
        List<Object[]> sumTotalPaidGroupedByProfileEventAndDates(LocalDateTime startDate, LocalDateTime endDate);

        @Query("SELECT s.category, SUM(s.totalPaid) FROM Schedule s WHERE s.statusPayment = 'Paid' AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate GROUP BY s.category")
        List<Object[]> sumTotalPaidGroupedByCategoryAndDates(LocalDateTime startDate, LocalDateTime endDate);

        @Query("SELECT s.scheduleStartDate, SUM(s.totalPaid) FROM Schedule s WHERE s.statusPayment = 'Maintenance' AND s.scheduleStartDate BETWEEN :startDate AND :endDate GROUP BY s.scheduleStartDate")
        List<Object[]> sumMaintenanceByDay(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT SUM(s.totalPaid) FROM Schedule s WHERE s.statusPayment = 'Maintenance' "
                        + "AND ((:startDate IS NULL AND :endDate IS NULL AND s.scheduleStartDate IS NOT NULL) "
                        + "OR (:startDate IS NOT NULL AND :endDate IS NOT NULL AND "
                        + "(s.scheduleStartDate IS NOT NULL AND s.scheduleEndDate IS NOT NULL AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate)))")
        Double sumMaintenanceByType(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        // @Query("SELECT SUM(s.total) "
        // + "FROM Schedule s "
        // + "WHERE s.games = :game "
        // + "AND s.status = 'Paid' "
        // + "AND ((:startDate IS NULL AND :endDate IS NULL AND s.scheduleDate IS NOT
        // NULL) "
        // + "OR (:startDate IS NOT NULL AND :endDate IS NOT NULL AND "
        // + "((s.scheduleStartDate IS NOT NULL AND s.scheduleEndDate IS NOT NULL AND
        // s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate) "
        // + "OR (s.scheduleDate IS NOT NULL AND s.scheduleDate BETWEEN :startDate AND
        // :endDate))))")
        // Double sumTotalByGame(@Param("game") String game, @Param("startDate")
        // LocalDate startDate,
        // @Param("endDate") LocalDate endDate);

        // @Query("SELECT SUM(s.total) FROM Schedule s WHERE s.status = 'Maintenance' "
        // + "AND ((:startDate IS NULL AND :endDate IS NULL AND s.scheduleDate IS NOT
        // NULL) "
        // + "OR (:startDate IS NOT NULL AND :endDate IS NOT NULL AND "
        // + "((s.scheduleStartDate IS NOT NULL AND s.scheduleEndDate IS NOT NULL AND
        // s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate) "
        // + "OR (s.scheduleDate IS NOT NULL AND s.scheduleDate BETWEEN :startDate AND
        // :endDate))))")
        // Double sumMaintenanceByType(@Param("startDate") LocalDate startDate,
        // @Param("endDate") LocalDate endDate);

        // @Query("SELECT SUM(CASE WHEN s.status = 'Paid' THEN 1 ELSE 0 END) as
        // totalPaid, "
        // + "SUM(CASE WHEN s.status = 'Maintenance' THEN 1 ELSE 0 END) as
        // totalMaintenance "
        // + "FROM Schedule s WHERE "
        // + "(:startDate IS NOT NULL AND :endDate IS NOT NULL AND (s.scheduleStartDate
        // IS NOT NULL AND s.scheduleEndDate IS NOT NULL AND s.scheduleStartDate >=
        // :startDate AND s.scheduleEndDate <= :endDate)) "
        // + "OR (:startDate IS NULL AND :endDate IS NULL AND s.scheduleDate IS NOT
        // NULL) "
        // + "AND s.venue.unit = :unit AND s.category IN ('Timnas', 'Umum')")
        // List<Object[]> findGamesCardTotals(@Param("unit") String unit,
        // @Param("startDate") LocalDate startDate,
        // @Param("endDate") LocalDate endDate);

        // @Query("SELECT s FROM Schedule s WHERE "
        // + "(:startDate IS NOT NULL AND :endDate IS NOT NULL AND (s.scheduleStartDate
        // IS NOT NULL AND s.scheduleEndDate IS NOT NULL AND s.scheduleStartDate >=
        // :startDate AND s.scheduleEndDate <= :endDate)) "
        // + "OR (:startDate IS NULL AND :endDate IS NULL AND s.scheduleDate IS NOT
        // NULL) "
        // + "AND s.venue.unit = :unit AND s.category IN ('Timnas', 'Umum') "
        // + "AND s.status IN ('Paid', 'Maintenance')")
        // List<Schedule> findGamesCardSchedules(@Param("unit") String unit,
        // @Param("startDate") LocalDate startDate,
        // @Param("endDate") LocalDate endDate);

        // @Query("SELECT s.venue.venue, s.category, SUM(CASE WHEN s.status = 'Paid'
        // THEN 1 ELSE 0 END) as totalPaid, "
        // + "SUM(CASE WHEN s.status = 'Maintenance' THEN 1 ELSE 0 END) as
        // totalMaintenance "
        // + "FROM Schedule s WHERE "
        // + "(:startDate IS NOT NULL AND :endDate IS NOT NULL AND (s.scheduleStartDate
        // IS NOT NULL AND s.scheduleEndDate IS NOT NULL AND s.scheduleStartDate >=
        // :startDate AND s.scheduleEndDate <= :endDate)) "
        // + "OR (:startDate IS NULL AND :endDate IS NULL AND s.scheduleDate IS NOT
        // NULL) "
        // + "AND s.venue.unit = :unit GROUP BY s.venue.venue, s.category")
        // List<Object[]> findEventCardTotals(@Param("unit") String unit,
        // @Param("startDate") LocalDate startDate,
        // @Param("endDate") LocalDate endDate);

        // @Query("SELECT s FROM Schedule s WHERE "
        // + "(:startDate IS NOT NULL AND :endDate IS NOT NULL AND (s.scheduleStartDate
        // IS NOT NULL AND s.scheduleEndDate IS NOT NULL AND s.scheduleStartDate >=
        // :startDate AND s.scheduleEndDate <= :endDate)) "
        // + "OR (:startDate IS NULL AND :endDate IS NULL AND s.scheduleDate IS NOT
        // NULL) "
        // + "AND s.venue.unit = :unit AND s.status IN ('Paid', 'Maintenance')")
        // List<Schedule> findEventCardSchedules(@Param("unit") String unit,
        // @Param("startDate") LocalDate startDate,
        // @Param("endDate") LocalDate endDate);

}
