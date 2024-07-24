package com.dev.gbk.repository;

import com.dev.gbk.dto.CardEventDTO;
import com.dev.gbk.dto.CardGamesDTO;
import com.dev.gbk.dto.OccupancyDTO;
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

        @Query("SELECT new com.dev.gbk.dto.OccupancyDTO(s.category, COUNT(s) * 100.0 / (SELECT COUNT(s2) FROM Schedule s2 WHERE (:unit IS NULL OR s2.venue.unit = :unit) AND (:startDate IS NULL OR s2.scheduleDate >= :startDate) AND (:endDate IS NULL OR s2.scheduleDate <= :endDate))) "
                        + "FROM Schedule s WHERE (:unit IS NULL OR s.venue.unit = :unit) AND (:startDate IS NULL OR s.scheduleDate >= :startDate) AND (:endDate IS NULL OR s.scheduleDate <= :endDate) GROUP BY s.category")
        List<OccupancyDTO> findCategoryUsage(@Param("unit") String unit, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT new com.dev.gbk.dto.OccupancyDTO(s.profileEvent, COUNT(s) * 100.0 / (SELECT COUNT(s2) FROM Schedule s2 WHERE (:unit IS NULL OR s2.venue.unit = :unit) AND (:startDate IS NULL OR s2.scheduleDate >= :startDate) AND (:endDate IS NULL OR s2.scheduleDate <= :endDate))) "
                        + "FROM Schedule s WHERE (:unit IS NULL OR s.venue.unit = :unit) AND (:startDate IS NULL OR s.scheduleDate >= :startDate) AND (:endDate IS NULL OR s.scheduleDate <= :endDate) GROUP BY s.profileEvent")
        List<OccupancyDTO> findProfileEventUsage(@Param("unit") String unit, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT SUM(s.total) FROM Schedule s WHERE s.category IN (:category1, :category2) AND s.scheduleDate BETWEEN :startDate AND :endDate")
        double sumTotalByCategory(@Param("category1") String category1, @Param("category2") String category2,
                        @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        @Query("SELECT SUM(s.total) FROM Schedule s WHERE s.category = :category AND s.scheduleDate BETWEEN :startDate AND :endDate")
        double sumTotalByCategory(@Param("category") String category, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT SUM(s.total) FROM Schedule s WHERE s.type = :type AND s.scheduleDate BETWEEN :startDate AND :endDate")
        double sumMaintenanceByType(@Param("type") String type, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT new com.dev.gbk.dto.CardGamesDTO(s.venue.venue, s.scheduleDate, SUM(CASE WHEN s.status = 'TERPAKAI' THEN 1 ELSE 0 END), SUM(CASE WHEN s.status = 'MAINTENANCE' THEN 1 ELSE 0 END)) "
                        + "FROM Schedule s WHERE s.scheduleDate BETWEEN :startDate AND :endDate AND s.venue.unit = :unit AND s.category IN ('Timnas', 'Umum') "
                        + "GROUP BY s.venue.venue, s.scheduleDate")
        List<CardGamesDTO> findGamesCardData(@Param("unit") String unit, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT new com.dev.gbk.dto.CardEventDTO(s.venue.venue, s.scheduleDate, SUM(CASE WHEN s.status = 'TERPAKAI' THEN 1 ELSE 0 END), SUM(CASE WHEN s.status = 'MAINTENANCE' THEN 1 ELSE 0 END)) "
                        + "FROM Schedule s WHERE s.scheduleDate BETWEEN :startDate AND :endDate AND s.venue.unit = :unit "
                        + "GROUP BY s.venue.venue, s.scheduleDate, s.category")
        List<CardEventDTO> findEventCardData(@Param("unit") String unit, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);
}
