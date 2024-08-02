package com.dev.gbk.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dev.gbk.dto.*;
import com.dev.gbk.model.Schedule;

import org.springframework.stereotype.Service;

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

    // public List<OccupancyDTO> getUsageByCategory(String startDate, String
    // endDate, String unit) {
    // LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : null;
    // LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : null;

    // if (start != null && end != null && end.isBefore(start)) {
    // LocalDate temp = start;
    // start = end;
    // end = temp;
    // }

    // return scheduleRepository.findCategoryUsage(unit, start, end);
    // }

    // public List<OccupancyDTO> getUsageByProfileEvent(String startDate, String
    // endDate, String unit) {
    // LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : null;
    // LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : null;

    // if (start != null && end != null && end.isBefore(start)) {
    // LocalDate temp = start;
    // start = end;
    // end = temp;
    // }

    // return scheduleRepository.findProfileEventUsage(unit, start, end);
    // }

    // public Double getTotalByGame(String game, String startDate, String endDate) {
    // LocalDate start = LocalDate.parse(startDate);
    // LocalDate end = LocalDate.parse(endDate);
    // if (end.isBefore(start)) {
    // LocalDate temp = start;
    // start = end;
    // end = temp;
    // }

    // // Mengambil total berdasarkan game ("Timnas" atau "Umum")
    // return scheduleRepository.sumTotalByGame(game, start, end);
    // }

    // public Double getTotalByType(String type, String startDate, String endDate) {
    // LocalDate start = LocalDate.parse(startDate);
    // LocalDate end = LocalDate.parse(endDate);
    // if (end.isBefore(start)) {
    // LocalDate temp = start;
    // start = end;
    // end = temp;
    // }

    // return scheduleRepository.sumTotalByType(type, start, end);
    // }

    // public IncomeDTO getTotalIncome(String startDate, String endDate) {
    // LocalDate start = LocalDate.parse(startDate);
    // LocalDate end = LocalDate.parse(endDate);
    // if (end.isBefore(start)) {
    // LocalDate temp = start;
    // start = end;
    // end = temp;
    // }

    // Double retailIncomeResult = retailRepository.sumPriceByStatus("Sewa");
    // Double retailOccupiedResult = retailRepository.sumSizeByStatus("Sewa");
    // Double retailNonOccupiedResult = retailRepository.sumSizeByStatus("Belum
    // Sewa");
    // Double maintenanceLapanganResult =
    // scheduleRepository.sumMaintenanceByType(start, end);

    // BigDecimal retailIncome = (retailIncomeResult != null) ?
    // BigDecimal.valueOf(retailIncomeResult)
    // : BigDecimal.ZERO;
    // BigDecimal retailOccupied = (retailOccupiedResult != null) ?
    // BigDecimal.valueOf(retailOccupiedResult)
    // : BigDecimal.ZERO;
    // BigDecimal retailNonOccupied = (retailNonOccupiedResult != null) ?
    // BigDecimal.valueOf(retailNonOccupiedResult)
    // : BigDecimal.ZERO;
    // BigDecimal maintenanceLapangan = (maintenanceLapanganResult != null)
    // ? BigDecimal.valueOf(maintenanceLapanganResult)
    // : BigDecimal.ZERO;
    // BigDecimal maintenanceParkir = BigDecimal.valueOf(1500000); // 1,5jt per
    // bulan

    // YearMonth startYM = YearMonth.from(start);
    // YearMonth endYM = YearMonth.from(end);
    // long monthsBetween = startYM.until(endYM,
    // java.time.temporal.ChronoUnit.MONTHS) + 1;
    // BigDecimal totalMaintenanceParkir =
    // maintenanceParkir.multiply(BigDecimal.valueOf(monthsBetween));

    // return new IncomeDTO(retailIncome, retailOccupied,
    // retailNonOccupied,
    // maintenanceLapangan, totalMaintenanceParkir);
    // }

    // public CardGamesDTO getGamesCardData(String startDate, String endDate, String
    // unit) {
    // LocalDate start = startDate != null ? LocalDate.parse(startDate)
    // : LocalDate.of(LocalDate.now().getYear(), 1, 1);
    // LocalDate end = (endDate != null) ? LocalDate.parse(endDate) :
    // LocalDate.now();
    // if (end.isBefore(start)) {
    // LocalDate temp = start;
    // start = end;
    // end = temp;
    // }

    // // Query untuk mendapatkan total Paid dan Maintenance
    // List<Object[]> totals = scheduleRepository.findGamesCardTotals(unit, start,
    // end);
    // int totalPaid = 0;
    // int totalMaintenance = 0;
    // if (!totals.isEmpty()) {
    // totalPaid = ((Number) totals.get(0)[0]).intValue();
    // totalMaintenance = ((Number) totals.get(0)[1]).intValue();
    // }

    // // Query untuk mendapatkan list schedule
    // List<Schedule> schedules = scheduleRepository.findGamesCardSchedules(unit,
    // start, end);

    // // Membuat list ScheduleDTO terpisah untuk Paid dan Maintenance
    // List<ScheduleDTO> paidSchedules = schedules.stream()
    // .filter(s -> "Paid".equals(s.getStatus()))
    // .map(s -> new ScheduleDTO(s.getVenue().getVenue(), s.getScheduleStartDate(),
    // s.getScheduleEndDate(),
    // s.getStatus()))
    // .collect(Collectors.toList());

    // List<ScheduleDTO> maintenanceSchedules = schedules.stream()
    // .filter(s -> "Maintenance".equals(s.getStatus()))
    // .map(s -> new ScheduleDTO(s.getVenue().getVenue(), s.getScheduleStartDate(),
    // s.getScheduleEndDate(),
    // s.getStatus()))
    // .collect(Collectors.toList());

    // // Membuat DTO utama
    // return new CardGamesDTO(totalPaid, totalMaintenance, paidSchedules,
    // maintenanceSchedules);
    // }

    // public List<CardEventDTO> getEventCardData(String startDate, String endDate,
    // String unit) {
    // LocalDate start = startDate != null ? LocalDate.parse(startDate)
    // : LocalDate.of(LocalDate.now().getYear(), 1, 1);
    // LocalDate end = (endDate != null) ? LocalDate.parse(endDate) :
    // LocalDate.now();
    // if (end.isBefore(start)) {
    // LocalDate temp = start;
    // start = end;
    // end = temp;
    // }

    // // Query untuk mendapatkan total Paid dan Maintenance
    // List<Object[]> totals = scheduleRepository.findEventCardTotals(unit, start,
    // end);
    // Map<String, CardEventDTO> eventMap = new HashMap<>();

    // for (Object[] total : totals) {
    // String venue = (String) total[0];
    // String category = (String) total[1];
    // int totalPaid = ((Number) total[2]).intValue();
    // int totalMaintenance = ((Number) total[3]).intValue();

    // CardEventDTO dto = new CardEventDTO();
    // dto.setVenue(venue);
    // dto.setCategory(category);
    // dto.setTotalPaid(totalPaid);
    // dto.setTotalMaintenance(totalMaintenance);
    // dto.setSchedules(new ArrayList<>());

    // eventMap.put(venue + ":" + category, dto);
    // }

    // // Query untuk mendapatkan list schedule
    // List<Schedule> schedules = scheduleRepository.findEventCardSchedules(unit,
    // start, end);

    // for (Schedule schedule : schedules) {
    // String key = schedule.getVenue().getVenue() + ":" + schedule.getCategory();
    // ScheduleDTO scheduleDTO = new ScheduleDTO(schedule.getVenue().getVenue(),
    // schedule.getScheduleStartDate(), schedule.getScheduleEndDate(),
    // schedule.getStatus());

    // if (eventMap.containsKey(key)) {
    // eventMap.get(key).getSchedules().add(scheduleDTO);
    // }
    // }

    // return new ArrayList<>(eventMap.values());
    // }

    public List<CardRetailDTO> getRetailCardData() {
        return retailRepository.findRetailCardData();
    }
}
