package com.dev.gbk.repository;

import com.dev.gbk.dto.*;
import com.dev.gbk.model.Schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {
        List<Schedule> findByScheduleDateBetween(LocalDate dateStart, LocalDate dateEnd);

        List<Schedule> findByScheduleDate(Date dateStart);

        List<Schedule> findByStatusAndCreatedAtBefore(String status, LocalDateTime dateTime);

        List<Schedule> findByStatusAndVenueIdAndCreatedAtBefore(String status, Long venueId, LocalDateTime dateTime);

        boolean existsByScheduleDateAndScheduleTimeFromAndScheduleTimeTo(LocalDate date, LocalTime timeFrom,
                        LocalTime timeTo);

        boolean existsByBookingNumber(String bookingNumber);

        @Query("SELECT new com.dev.gbk.dto.OccupancyDTO(s.category, COUNT(s) * 100.0 / "
                        + "(SELECT COUNT(s2) FROM Schedule s2 WHERE "
                        + "(:unit IS NULL OR s2.venue.unit = :unit) "
                        + "AND ("
                        + "(:startDate IS NOT NULL AND :endDate IS NOT NULL AND (s2.scheduleStartDate IS NOT NULL AND s2.scheduleEndDate IS NOT NULL AND s2.scheduleStartDate >= :startDate AND s2.scheduleEndDate <= :endDate)) "
                        + "OR (:startDate IS NULL AND :endDate IS NULL AND s2.scheduleDate IS NOT NULL) "
                        + ") AND s2.status = 'Paid')) "
                        + "FROM Schedule s WHERE "
                        + "(:unit IS NULL OR s.venue.unit = :unit) "
                        + "AND ("
                        + "(:startDate IS NOT NULL AND :endDate IS NOT NULL AND (s.scheduleStartDate IS NOT NULL AND s.scheduleEndDate IS NOT NULL AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate)) "
                        + "OR (:startDate IS NULL AND :endDate IS NULL AND s.scheduleDate IS NOT NULL)"
                        + ") AND s.status = 'Paid' GROUP BY s.category")
        List<OccupancyDTO> findCategoryUsage(@Param("unit") String unit, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT new com.dev.gbk.dto.OccupancyDTO(s.profileEvent, COUNT(s) * 100.0 / "
                        + "(SELECT COUNT(s2) FROM Schedule s2 WHERE "
                        + "(:unit IS NULL OR s2.venue.unit = :unit) "
                        + "AND ("
                        + "(:startDate IS NOT NULL AND :endDate IS NOT NULL AND (s2.scheduleStartDate IS NOT NULL AND s2.scheduleEndDate IS NOT NULL AND s2.scheduleStartDate >= :startDate AND s2.scheduleEndDate <= :endDate)) "
                        + "OR (:startDate IS NULL AND :endDate IS NULL AND s2.scheduleDate IS NOT NULL) "
                        + ") AND s2.status = 'Paid')) "
                        + "FROM Schedule s WHERE "
                        + "(:unit IS NULL OR s.venue.unit = :unit) "
                        + "AND ("
                        + "(:startDate IS NOT NULL AND :endDate IS NOT NULL AND (s.scheduleStartDate IS NOT NULL AND s.scheduleEndDate IS NOT NULL AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate)) "
                        + "OR (:startDate IS NULL AND :endDate IS NULL AND s.scheduleDate IS NOT NULL)"
                        + ") AND s.status = 'Paid' GROUP BY s.profileEvent")
        List<OccupancyDTO> findProfileEventUsage(@Param("unit") String unit, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT SUM(s.total) "
                        + "FROM Schedule s "
                        + "WHERE s.type = :type "
                        + "AND s.status = 'Paid' "
                        + "AND ((:startDate IS NULL AND :endDate IS NULL AND s.scheduleDate IS NOT NULL) "
                        + "OR (:startDate IS NOT NULL AND :endDate IS NOT NULL AND "
                        + "((s.scheduleStartDate IS NOT NULL AND s.scheduleEndDate IS NOT NULL AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate) "
                        + "OR (s.scheduleDate IS NOT NULL AND s.scheduleDate BETWEEN :startDate AND :endDate))))")
        Double sumTotalByType(@Param("type") String type, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT SUM(s.total) "
                        + "FROM Schedule s "
                        + "WHERE s.games = :game "
                        + "AND s.status = 'Paid' "
                        + "AND ((:startDate IS NULL AND :endDate IS NULL AND s.scheduleDate IS NOT NULL) "
                        + "OR (:startDate IS NOT NULL AND :endDate IS NOT NULL AND "
                        + "((s.scheduleStartDate IS NOT NULL AND s.scheduleEndDate IS NOT NULL AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate) "
                        + "OR (s.scheduleDate IS NOT NULL AND s.scheduleDate BETWEEN :startDate AND :endDate))))")
        Double sumTotalByGame(@Param("game") String game, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT SUM(s.total) FROM Schedule s WHERE s.status = 'Maintenance' "
                        + "AND ((:startDate IS NULL AND :endDate IS NULL AND s.scheduleDate IS NOT NULL) "
                        + "OR (:startDate IS NOT NULL AND :endDate IS NOT NULL AND "
                        + "((s.scheduleStartDate IS NOT NULL AND s.scheduleEndDate IS NOT NULL AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate) "
                        + "OR (s.scheduleDate IS NOT NULL AND s.scheduleDate BETWEEN :startDate AND :endDate))))")
        Double sumMaintenanceByType(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        @Query("SELECT SUM(CASE WHEN s.status = 'Paid' THEN 1 ELSE 0 END) as totalPaid, "
                        + "SUM(CASE WHEN s.status = 'Maintenance' THEN 1 ELSE 0 END) as totalMaintenance "
                        + "FROM Schedule s WHERE "
                        + "(:startDate IS NOT NULL AND :endDate IS NOT NULL AND (s.scheduleStartDate IS NOT NULL AND s.scheduleEndDate IS NOT NULL AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate)) "
                        + "OR (:startDate IS NULL AND :endDate IS NULL AND s.scheduleDate IS NOT NULL) "
                        + "AND s.venue.unit = :unit AND s.category IN ('Timnas', 'Umum')")
        List<Object[]> findGamesCardTotals(@Param("unit") String unit, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT s FROM Schedule s WHERE "
                        + "(:startDate IS NOT NULL AND :endDate IS NOT NULL AND (s.scheduleStartDate IS NOT NULL AND s.scheduleEndDate IS NOT NULL AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate)) "
                        + "OR (:startDate IS NULL AND :endDate IS NULL AND s.scheduleDate IS NOT NULL) "
                        + "AND s.venue.unit = :unit AND s.category IN ('Timnas', 'Umum') "
                        + "AND s.status IN ('Paid', 'Maintenance')")
        List<Schedule> findGamesCardSchedules(@Param("unit") String unit, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT s.venue.venue, s.category, SUM(CASE WHEN s.status = 'Paid' THEN 1 ELSE 0 END) as totalPaid, "
                        + "SUM(CASE WHEN s.status = 'Maintenance' THEN 1 ELSE 0 END) as totalMaintenance "
                        + "FROM Schedule s WHERE "
                        + "(:startDate IS NOT NULL AND :endDate IS NOT NULL AND (s.scheduleStartDate IS NOT NULL AND s.scheduleEndDate IS NOT NULL AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate)) "
                        + "OR (:startDate IS NULL AND :endDate IS NULL AND s.scheduleDate IS NOT NULL) "
                        + "AND s.venue.unit = :unit GROUP BY s.venue.venue, s.category")
        List<Object[]> findEventCardTotals(@Param("unit") String unit, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT s FROM Schedule s WHERE "
                        + "(:startDate IS NOT NULL AND :endDate IS NOT NULL AND (s.scheduleStartDate IS NOT NULL AND s.scheduleEndDate IS NOT NULL AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate)) "
                        + "OR (:startDate IS NULL AND :endDate IS NULL AND s.scheduleDate IS NOT NULL) "
                        + "AND s.venue.unit = :unit AND s.status IN ('Paid', 'Maintenance')")
        List<Schedule> findEventCardSchedules(@Param("unit") String unit, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

}
