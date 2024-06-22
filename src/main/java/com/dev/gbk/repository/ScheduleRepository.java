package com.dev.gbk.repository;

import com.dev.gbk.model.Schedule;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {
    List<Schedule> findByScheduleDateBetween(LocalDateTime dateStart, LocalDateTime dateEnd);
    List<Schedule> findByScheduleDate(LocalDateTime dateStart);
}
