package com.dev.gbk.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dev.gbk.dto.IncomeDTO;
import com.dev.gbk.dto.OccupancyDTO;
import com.dev.gbk.repository.RetailRepository;
import com.dev.gbk.repository.ScheduleRepository;

@Service
public class DashboardService {
    private final ScheduleRepository scheduleRepository;
    private final RetailRepository retailRepository;

    public DashboardService(ScheduleRepository scheduleRepository, RetailRepository retailRepository) {
        this.scheduleRepository = scheduleRepository;
        this.retailRepository = retailRepository;
    }

    public List<OccupancyDTO> getUsageByCategory(String startDate, String endDate, String unit) {
        LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : null;

        if (start != null && end != null && end.isBefore(start)) {
            LocalDate temp = start;
            start = end;
            end = temp;
        }

        return scheduleRepository.findCategoryUsage(unit, start, end);
    }

    public List<OccupancyDTO> getUsageByProfileEvent(String startDate, String endDate, String unit) {
        LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : null;

        if (start != null && end != null && end.isBefore(start)) {
            LocalDate temp = start;
            start = end;
            end = temp;
        }

        return scheduleRepository.findProfileEventUsage(unit, start, end);
    }

    public IncomeDTO getTotalIncome(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        if (end.isBefore(start)) {
            LocalDate temp = start;
            start = end;
            end = temp;
        }

        double eventIncome = scheduleRepository.sumTotalByCategory("Olahraga", "Non Olahraga", start, end);
        double gamesIncome = scheduleRepository.sumTotalByCategory("Games", "Umum", start, end);
        double retailIncome = retailRepository.sumPriceByStatus("Sewa");
        double retailOccupied = retailRepository.sumSizeByStatus("Sewa");
        double retailNonOccupied = retailRepository.sumSizeByStatus("Belum Sewa");
        double maintenanceLapangan = scheduleRepository.sumMaintenanceByType("venue", start, end);
        double maintenanceParkir = 1500000; // 1,5jt per bulan

        YearMonth startYM = YearMonth.from(start);
        YearMonth endYM = YearMonth.from(end);
        long monthsBetween = startYM.until(endYM, java.time.temporal.ChronoUnit.MONTHS) + 1;
        double totalMaintenanceParkir = maintenanceParkir * monthsBetween;

        return new IncomeDTO(eventIncome, gamesIncome, retailIncome, retailOccupied, retailNonOccupied,
                maintenanceLapangan, totalMaintenanceParkir);
    }
}
