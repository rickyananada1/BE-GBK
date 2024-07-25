package com.dev.gbk.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dev.gbk.dto.CardEventDTO;
import com.dev.gbk.dto.CardGamesDTO;
import com.dev.gbk.dto.CardRetailDTO;
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

        Double eventIncomeResult = scheduleRepository.sumTotalByCategory("Olahraga", "Non Olahraga", start, end);
        Double gamesIncomeResult = scheduleRepository.sumTotalByCategory("Games", "Umum", start, end);
        Double retailIncomeResult = retailRepository.sumPriceByStatus("Sewa");
        Double retailOccupiedResult = retailRepository.sumSizeByStatus("Sewa");
        Double retailNonOccupiedResult = retailRepository.sumSizeByStatus("Belum Sewa");
        Double maintenanceLapanganResult = scheduleRepository.sumMaintenanceByType("venue", start, end);

        BigDecimal eventIncome = (eventIncomeResult != null) ? BigDecimal.valueOf(eventIncomeResult) : BigDecimal.ZERO;
        BigDecimal gamesIncome = (gamesIncomeResult != null) ? BigDecimal.valueOf(gamesIncomeResult) : BigDecimal.ZERO;
        BigDecimal retailIncome = (retailIncomeResult != null) ? BigDecimal.valueOf(retailIncomeResult)
                : BigDecimal.ZERO;
        BigDecimal retailOccupied = (retailOccupiedResult != null) ? BigDecimal.valueOf(retailOccupiedResult)
                : BigDecimal.ZERO;
        BigDecimal retailNonOccupied = (retailNonOccupiedResult != null) ? BigDecimal.valueOf(retailNonOccupiedResult)
                : BigDecimal.ZERO;
        BigDecimal maintenanceLapangan = (maintenanceLapanganResult != null)
                ? BigDecimal.valueOf(maintenanceLapanganResult)
                : BigDecimal.ZERO;
        BigDecimal maintenanceParkir = BigDecimal.valueOf(1500000); // 1,5jt per bulan

        YearMonth startYM = YearMonth.from(start);
        YearMonth endYM = YearMonth.from(end);
        long monthsBetween = startYM.until(endYM, java.time.temporal.ChronoUnit.MONTHS) + 1;
        BigDecimal totalMaintenanceParkir = maintenanceParkir.multiply(BigDecimal.valueOf(monthsBetween));

        return new IncomeDTO(eventIncome, gamesIncome, retailIncome, retailOccupied, retailNonOccupied,
                maintenanceLapangan, totalMaintenanceParkir);
    }

    public List<CardGamesDTO> getGamesCardData(String startDate, String endDate, String unit) {
        LocalDate start = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : LocalDate.now();

        return scheduleRepository.findGamesCardData(unit, start, end);
    }

    public List<CardEventDTO> getEventCardData(String startDate, String endDate, String unit) {
        LocalDate start = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : LocalDate.now();

        return scheduleRepository.findEventCardData(unit, start, end);
    }

    public List<CardRetailDTO> getRetailCardData() {
        return retailRepository.findRetailCardData();
    }
}
