package com.dev.gbk.repository;

import com.dev.gbk.model.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
}
