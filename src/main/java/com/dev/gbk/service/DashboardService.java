package com.dev.gbk.service;

import com.dev.gbk.dto.*;
import com.dev.gbk.model.Schedule;
import com.dev.gbk.model.Venue;
import com.dev.gbk.repository.RetailRepository;
import com.dev.gbk.repository.ScheduleRepository;

import org.apache.commons.lang3.function.TriFunction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
public class DashboardService {

        private static final String PAID_STATUS = "Paid";
        private static final String MAINTENANCE_STATUS = "Maintenance";
        private static final BigDecimal MONTHLY_PARKING_FEE = BigDecimal.valueOf(1_600_000); // 1.6 million

        private final ScheduleRepository scheduleRepository;
        private final RetailRepository retailRepository;

        public DashboardService(ScheduleRepository scheduleRepository, RetailRepository retailRepository) {
                this.scheduleRepository = scheduleRepository;
                this.retailRepository = retailRepository;
        }

        public Map<String, BigDecimal> getUsageByCategory(LocalDate startDate, LocalDate endDate, String unitName) {
                List<Schedule> schedules = scheduleRepository.findSchedules(unitName, startDate, endDate);

                // filter schedules statusPayment is paid
                Map<String, Long> categoryCount = schedules.stream()
                                .filter(schedule -> PAID_STATUS.equals(schedule.getStatusPayment()))
                                .collect(Collectors.groupingBy(
                                                schedule -> Optional.ofNullable(schedule.getCategory())
                                                                .orElse("Unknown Category"),
                                                Collectors.counting()));

                long totalEvents = categoryCount.values().stream().mapToLong(Long::longValue).sum();

                return calculatePercentages(categoryCount, totalEvents);
        }

        public Map<String, BigDecimal> getUsageByProfileEvent(LocalDate startDate, LocalDate endDate, String unitName) {
                List<Schedule> schedules = scheduleRepository.findSchedules(unitName, startDate, endDate);

                Map<String, Long> profileEventCount = schedules.stream()
                                .filter(schedule -> PAID_STATUS.equals(schedule.getStatusPayment()))
                                .collect(Collectors.groupingBy(Schedule::getProfileEvent, Collectors.counting()));

                long totalEvents = profileEventCount.values().stream().mapToLong(Long::longValue).sum();

                return calculatePercentages(profileEventCount, totalEvents);
        }

        public Map<String, Integer> getTotalPaidGroupedByProfileEvent(LocalDate startDate, LocalDate endDate,
                        String unitName) {
                return getTotalPaidGroupedBy(startDate, endDate, unitName,
                                scheduleRepository::sumTotalPaidGroupedByProfileEventAndUnitAndDates,
                                scheduleRepository::sumTotalPaidGroupedByProfileEventAndDates);
        }

        public Map<String, Integer> getTotalPaidGroupedByGames(LocalDate startDate, LocalDate endDate,
                        String unitName) {
                return getTotalPaidGroupedBy(startDate, endDate, unitName,
                                scheduleRepository::sumTotalPaidGroupedByCategoryAndUnitAndDates,
                                scheduleRepository::sumTotalPaidGroupedByCategoryAndDates);
        }

        private Map<String, Integer> getTotalPaidGroupedBy(LocalDate startDate, LocalDate endDate, String unitName,
                        TriFunction<String, LocalDate, LocalDate, List<Object[]>> unitQuery,
                        BiFunction<LocalDate, LocalDate, List<Object[]>> generalQuery) {

                List<Object[]> results = (unitName != null)
                                ? unitQuery.apply(unitName, startDate, endDate)
                                : generalQuery.apply(startDate, endDate);

                return results.stream()
                                .collect(Collectors.toMap(
                                                result -> (String) result[0],
                                                result -> ((Number) result[1]).intValue()));
        }

        public Map<LocalDate, Integer> getTotalMaintenanceByDay(LocalDate startDate, LocalDate endDate) {
                List<Object[]> results = scheduleRepository.sumMaintenanceByDay(startDate, endDate);

                return results.stream()
                                .collect(Collectors.toMap(
                                                result -> (LocalDate) result[0],
                                                result -> ((Number) result[1]).intValue()));
        }

        public Integer getMonthlyParkingFee() {
                return MONTHLY_PARKING_FEE.intValue();
        }

        public IncomeDTO getTotalIncome(LocalDate startDate, LocalDate endDate) {

                BigDecimal retailIncome = safeBigDecimalFromDouble(
                                retailRepository.sumPriceByStatusAndDateRangeAndArea("Sewa", null));

                BigDecimal retailOccupied = safeBigDecimalFromDouble(
                                retailRepository.sumSizeByStatusAndDateRangeAndArea("Sewa", null));

                BigDecimal retailNonOccupied = safeBigDecimalFromDouble(
                                retailRepository.sumSizeByStatusAndDateRangeAndArea("Belum Sewa",
                                                null));

                BigDecimal maintenanceVenue = safeBigDecimalFromDouble(
                                scheduleRepository.sumMaintenanceByType(startDate, endDate));

                long monthsBetween = calculateMonthsBetween(startDate, endDate);
                BigDecimal totalParkingFee = MONTHLY_PARKING_FEE.multiply(BigDecimal.valueOf(monthsBetween));

                return new IncomeDTO(retailIncome, retailOccupied, retailNonOccupied, maintenanceVenue,
                                totalParkingFee);
        }

        public Map<String, Integer> getProjectionTotalPaidGroupedByProfileEvent(LocalDate startDate,
                        LocalDate endDate) {
                List<Object[]> results = scheduleRepository
                                .sumSoftBookingTotalPaidGroupedByProfileEventAndDates(startDate, endDate);
                return results.stream()
                                .collect(Collectors.toMap(
                                                result -> (String) result[0],
                                                result -> ((Number) result[1]).intValue()));
        }

        public Map<String, Integer> getProjectionTotalPaidGroupedByGames(LocalDate startDate, LocalDate endDate) {
                List<Object[]> results = scheduleRepository.sumSoftBookingTotalPaidGroupedByCategoryAndDates(startDate,
                                endDate);
                return results.stream()
                                .collect(Collectors.toMap(
                                                result -> (String) result[0],
                                                result -> ((Number) result[1]).intValue()));
        }

        public List<CardGamesDTO> getGamesCardData(LocalDate startDate, LocalDate endDate, String unit) {
                // Ambil semua data schedule
                List<Schedule> schedules = scheduleRepository.findSchedules(unit, startDate, endDate);

                // Kelompokkan berdasarkan kategori "Timnas" dan "Umum"
                Map<String, List<Schedule>> groupedSchedules = schedules.stream()
                                .collect(Collectors.groupingBy(
                                                schedule -> Optional.ofNullable(schedule.getGames())
                                                                .orElse("Unknown Category")));

                // Buat daftar CardGamesDTO untuk setiap kategori
                List<CardGamesDTO> cardGamesDTOList = new ArrayList<>();

                for (Map.Entry<String, List<Schedule>> entry : groupedSchedules.entrySet()) {
                        String category = entry.getKey();
                        List<Schedule> categorySchedules = entry.getValue();

                        int totalPaid = (int) categorySchedules.stream()
                                        .filter(schedule -> PAID_STATUS.equals(schedule.getStatusPayment()))
                                        .count();

                        int totalMaintenance = (int) categorySchedules.stream()
                                        .filter(schedule -> MAINTENANCE_STATUS.equals(schedule.getStatusPayment()))
                                        .count();

                        List<ScheduleDTO> paidSchedules = categorySchedules.stream()
                                        .filter(schedule -> PAID_STATUS.equals(schedule.getStatusPayment()))
                                        .map(schedule -> new ScheduleDTO(
                                                        schedule.getVenues().stream().map(Venue::getVenue)
                                                                        .collect(Collectors.toList()),
                                                        schedule.getScheduleStartDate(),
                                                        schedule.getScheduleEndDate(),
                                                        schedule.getStatusPayment()))
                                        .collect(Collectors.toList());

                        List<ScheduleDTO> maintenanceSchedules = categorySchedules.stream()
                                        .filter(schedule -> MAINTENANCE_STATUS.equals(schedule.getStatusPayment()))
                                        .map(schedule -> new ScheduleDTO(
                                                        schedule.getVenues().stream().map(Venue::getVenue)
                                                                        .collect(Collectors.toList()),
                                                        schedule.getScheduleStartDate(),
                                                        schedule.getScheduleEndDate(),
                                                        schedule.getStatusPayment()))
                                        .collect(Collectors.toList());

                        cardGamesDTOList.add(new CardGamesDTO(totalPaid, totalMaintenance, paidSchedules,
                                        maintenanceSchedules, category));
                }

                return cardGamesDTOList;
        }

        public List<CardEventDTO> getEventCardData(LocalDate startDate, LocalDate endDate, String unit) {
                List<Schedule> schedules = scheduleRepository.findSchedules(unit, startDate, endDate);
                // Group schedules by the concatenation of venues and category
                Map<String, CardEventDTO> eventMap = schedules.stream().collect(Collectors.groupingBy(
                                schedule -> createEventMapKey(schedule.getVenues(), schedule.getCategory()),
                                Collectors.collectingAndThen(Collectors.toList(), this::createCardEventDTO)));

                return new ArrayList<>(eventMap.values());
        }

        private CardEventDTO createCardEventDTO(List<Schedule> schedules) {
                int totalPaid = (int) schedules.stream()
                                .filter(s -> PAID_STATUS.equals(s.getStatusPayment()))
                                .count();

                int totalMaintenance = (int) schedules.stream()
                                .filter(s -> MAINTENANCE_STATUS.equals(s.getStatusPayment()))
                                .count();

                List<ScheduleDTO> scheduleDTOs = schedules.stream()
                                .map(schedule -> new ScheduleDTO(
                                                schedule.getVenues().stream().map(Venue::getVenue)
                                                                .collect(Collectors.toList()),
                                                schedule.getScheduleStartDate(),
                                                schedule.getScheduleEndDate(),
                                                schedule.getStatusPayment()))
                                .collect(Collectors.toList());

                String venue = schedules.get(0).getVenues().stream().map(Venue::getVenue)
                                .collect(Collectors.joining(","));
                String category = schedules.get(0).getCategory();

                return new CardEventDTO(venue, category, totalPaid, totalMaintenance, scheduleDTOs);
        }

        public List<CardRetailDTO> getRetailCardData() {
                return retailRepository.findRetailCardData();
        }

        // === Helper Methods ===

        private <T> Map<String, BigDecimal> calculatePercentages(Map<String, T> countMap, long total) {
                return countMap.entrySet().stream()
                                .collect(Collectors.toMap(
                                                Map.Entry::getKey,
                                                entry -> BigDecimal
                                                                .valueOf(((Number) entry.getValue()).doubleValue()
                                                                                * 100.0 / total)
                                                                .setScale(2, RoundingMode.HALF_UP)));
        }

        private BigDecimal safeBigDecimalFromDouble(Double value) {
                return (value != null) ? BigDecimal.valueOf(value) : BigDecimal.ZERO;
        }

        private long calculateMonthsBetween(LocalDate start, LocalDate end) {
                YearMonth startYM = YearMonth.from(start);
                YearMonth endYM = YearMonth.from(end);
                return startYM.until(endYM, java.time.temporal.ChronoUnit.MONTHS) + 1;
        }

        private String createEventMapKey(List<Venue> venues, String category) {
                return venues.stream().map(Venue::getVenue).collect(Collectors.joining(",")) + ":" + category;
        }

}