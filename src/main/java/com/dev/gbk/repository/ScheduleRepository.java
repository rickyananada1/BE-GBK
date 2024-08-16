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
        List<Object[]> sumTotalPaidGroupedByProfileEventAndUnitAndDates(String unitName, LocalDate startDate,
                        LocalDate endDate);

        @Query("SELECT s.category, SUM(s.totalPaid) FROM Schedule s JOIN s.venues v JOIN v.unit u WHERE u.name = :unitName AND s.statusPayment = 'Paid' AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate GROUP BY s.category")
        List<Object[]> sumTotalPaidGroupedByCategoryAndUnitAndDates(String unitName, LocalDate startDate,
                        LocalDate endDate);

        @Query("SELECT s.profileEvent, SUM(s.totalPaid) FROM Schedule s WHERE s.statusPayment = 'Paid' AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate GROUP BY s.profileEvent")
        List<Object[]> sumTotalPaidGroupedByProfileEventAndDates(LocalDate startDate, LocalDate endDate);

        @Query("SELECT s.category, SUM(s.totalPaid) FROM Schedule s WHERE s.statusPayment = 'Paid' AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate GROUP BY s.category")
        List<Object[]> sumTotalPaidGroupedByCategoryAndDates(LocalDate startDate, LocalDate endDate);

        @Query("SELECT s.profileEvent, SUM(s.totalPaid) FROM Schedule s WHERE s.statusBooking = 'Soft Booking' AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate GROUP BY s.profileEvent")
        List<Object[]> sumSoftBookingTotalPaidGroupedByProfileEventAndDates(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT s.category, SUM(s.totalPaid) FROM Schedule s WHERE s.statusBooking = 'Soft Booking' AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate GROUP BY s.category")
        List<Object[]> sumSoftBookingTotalPaidGroupedByCategoryAndDates(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT s.scheduleStartDate, SUM(s.totalPaid) FROM Schedule s WHERE s.statusPayment = 'Maintenance' AND s.scheduleStartDate BETWEEN :startDate AND :endDate GROUP BY s.scheduleStartDate")
        List<Object[]> sumMaintenanceByDay(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT SUM(s.totalPaid) FROM Schedule s WHERE s.statusPayment = 'Maintenance' "
                        + "AND ((:startDate IS NULL AND :endDate IS NULL AND s.scheduleStartDate IS NOT NULL) "
                        + "OR (:startDate IS NOT NULL AND :endDate IS NOT NULL AND "
                        + "(s.scheduleStartDate IS NOT NULL AND s.scheduleEndDate IS NOT NULL AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate)))")
        Double sumMaintenanceByType(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        @Query("SELECT SUM(CASE WHEN s.statusPayment = 'Paid' THEN s.totalPaid ELSE 0 END), " +
                        "SUM(CASE WHEN s.statusPayment = 'Maintenance' THEN s.totalPaid ELSE 0 END) " +
                        "FROM Schedule s JOIN s.venues v WHERE v.unit.name = :unitName " +
                        "AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate")
        List<Object[]> findGamesCardTotals(@Param("unitName") String unitName,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT s FROM Schedule s JOIN s.venues v WHERE v.unit.name = :unitName " +
                        "AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate")
        List<Schedule> findGamesCardSchedules(@Param("unitName") String unitName,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT s.venues, s.category, SUM(CASE WHEN s.statusPayment = 'Paid' THEN s.totalPaid ELSE 0 END)" +
                        ", SUM(CASE WHEN s.statusPayment = 'Maintenance' THEN s.totalPaid ELSE 0 END) " +
                        "FROM Schedule s " +
                        "JOIN s.venues v " +
                        "WHERE v.unit.name = :unit " +
                        "AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate " +
                        "GROUP BY s.venues, s.category")
        List<Object[]> findEventCardTotals(@Param("unit") String unit, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT s FROM Schedule s " +
                        "JOIN s.venues v " +
                        "WHERE v.unit.name = :unit " +
                        "AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate " +
                        "AND (s.statusPayment = 'Paid' OR s.statusPayment = 'Maintenance')")
        List<Schedule> findEventCardSchedules(@Param("unit") String unit, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

}
