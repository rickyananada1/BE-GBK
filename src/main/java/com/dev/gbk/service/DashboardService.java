package com.dev.gbk.service;

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

    public List<OccupancyDTO> getUsageByCategory(String startDate, String endDate, String unit) {
        return scheduleRepository.findUsageByCategory(startDate, endDate, unit);
    }

    public List<OccupancyDTO> getUsageByProfileEvent(String startDate, String endDate, String unit) {
        return scheduleRepository.findUsageByProfileEvent(startDate, endDate, unit);
    }
}
