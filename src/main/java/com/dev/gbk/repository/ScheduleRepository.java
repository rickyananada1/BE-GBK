package com.dev.gbk.repository;

import com.dev.gbk.dto.OccupancyDTO;
import com.dev.gbk.model.Schedule;

import java.time.LocalDate;
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

        List<Schedule> findByStatusAndCreatedAtBefore(String status, LocalDate date);

        List<Schedule> findByStatusAndVenueIdAndCreatedAtBefore(String status, Long venueId, LocalDate date);

        boolean existsByScheduleDateAndScheduleTimeFromAndScheduleTimeTo(LocalDate date, LocalTime timeFrom,
                        LocalTime timeTo);

        boolean existsByBookingNumber(String bookingNumber);

        // @Query("SELECT new com.dev.gbk.dto.OccupancyDTO(s.category as judul,(
        // COUNT(s) * 100.0 / (SELECT COUNT(s2) FROM Schedule s2 WHERE s2.venue.unit =
        // :unit AND s2.scheduleDate BETWEEN :startDate AND :endDate)) as occPercent) "
        // +
        // "FROM Schedule s WHERE s.venue.unit = :unit AND s.scheduleDate BETWEEN
        // :startDate AND :endDate GROUP BY s.category")
        // List<OccupancyDTO> findCategoryUsage(@Param("unit") String unit,
        // @Param("startDate") LocalDate startDate,
        // @Param("endDate") LocalDate endDate);

        // @Query("SELECT new com.dev.gbk.dto.OccupancyDTO(s.profileEvent as judul,
        // (COUNT(s) * 100.0 / (SELECT COUNT(s2) FROM Schedule s2 WHERE s2.venue.unit =
        // :unit AND s2.scheduleDate BETWEEN :startDate AND :endDate)) as occPercent) "
        // +
        // "FROM Schedule s WHERE s.venue.unit = :unit AND s.scheduleDate BETWEEN
        // :startDate AND :endDate GROUP BY s.profileEvent")
        // List<OccupancyDTO> findProfileEventUsage(@Param("unit") String unit,
        // @Param("startDate") LocalDate startDate,
        // @Param("endDate") LocalDate endDate);

}
