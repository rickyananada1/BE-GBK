package com.dev.gbk.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dev.gbk.dto.OccupancyDTO;
import com.dev.gbk.repository.ScheduleRepository;

@Service
public class DashboardService {
    private final ScheduleRepository scheduleRepository;

    public DashboardService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    // public List<OccupancyDTO> getUsageByCategory(String startDate, String
    // endDate, String unit) {
    // LocalDate start = LocalDate.parse(startDate);
    // LocalDate end = LocalDate.parse(endDate);
    // if (end.isBefore(start)) {
    // LocalDate temp = start;
    // start = end;
    // end = temp;
    // }
    // return scheduleRepository.findCategoryUsage(unit, start, end);
    // }

    // public List<OccupancyDTO> getUsageByProfileEvent(String startDate, String
    // endDate, String unit) {
    // LocalDate start = LocalDate.parse(startDate);
    // LocalDate end = LocalDate.parse(endDate);
    // if (end.isBefore(start)) {
    // LocalDate temp = start;
    // start = end;
    // end = temp;
    // }
    // return scheduleRepository.findProfileEventUsage(unit, start, end);
    // }
}
