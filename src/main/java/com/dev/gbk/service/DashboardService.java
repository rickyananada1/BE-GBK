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
        private static final BigDecimal MONTHLY_PARKING_FEE = BigDecimal.valueOf(1_500_000); // 1.5 million

        private final ScheduleRepository scheduleRepository;
        private final RetailRepository retailRepository;

        public DashboardService(ScheduleRepository scheduleRepository, RetailRepository retailRepository) {
                this.scheduleRepository = scheduleRepository;
                this.retailRepository = retailRepository;
        }

        public Map<String, BigDecimal> getUsageByCategory(LocalDate startDate, LocalDate endDate, String unitName) {
                List<Schedule> schedules = fetchSchedules(unitName, endDate);

                Map<String, Long> categoryCount = schedules.stream()
                                .filter(schedule -> isWithinDateRange(schedule, startDate, endDate))
                                .collect(Collectors.groupingBy(Schedule::getCategory, Collectors.counting()));

                long totalEvents = categoryCount.values().stream().mapToLong(Long::longValue).sum();

                return calculatePercentages(categoryCount, totalEvents);
        }

        public Map<String, BigDecimal> getUsageByProfileEvent(LocalDate startDate, LocalDate endDate, String unitName) {
                List<Schedule> schedules = fetchSchedules(unitName, endDate);

                Map<String, Long> profileEventCount = schedules.stream()
                                .filter(schedule -> isWithinDateRange(schedule, startDate, endDate))
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

        public CardGamesDTO getGamesCardData(String startDateStr, String endDateStr, String unit) {
                LocalDate start = parseDateOrDefault(startDateStr, LocalDate.of(LocalDate.now().getYear(), 1, 1));
                LocalDate end = parseDateOrDefault(endDateStr, LocalDate.now());
                ensureStartBeforeEnd(start, end);

                List<Object[]> totals = scheduleRepository.findGamesCardTotals(unit, start, end);
                int totalPaid = totals.isEmpty() ? 0 : ((Number) totals.get(0)[0]).intValue();
                int totalMaintenance = totals.isEmpty() ? 0 : ((Number) totals.get(0)[1]).intValue();

                List<Schedule> schedules = scheduleRepository.findGamesCardSchedules(unit, start, end);

                List<ScheduleDTO> paidSchedules = mapSchedulesToDTO(schedules, PAID_STATUS);
                List<ScheduleDTO> maintenanceSchedules = mapSchedulesToDTO(schedules, MAINTENANCE_STATUS);

                return new CardGamesDTO(totalPaid, totalMaintenance, paidSchedules, maintenanceSchedules);
        }

        public List<CardEventDTO> getEventCardData(String startDateStr, String endDateStr, String unit) {
                LocalDate start = parseDateOrDefault(startDateStr, LocalDate.of(LocalDate.now().getYear(), 1, 1));
                LocalDate end = parseDateOrDefault(endDateStr, LocalDate.now());
                ensureStartBeforeEnd(start, end);

                List<Object[]> totals = scheduleRepository.findEventCardTotals(unit, start, end);
                Map<String, CardEventDTO> eventMap = createEventMapFromTotals(totals);

                List<Schedule> schedules = scheduleRepository.findEventCardSchedules(unit, start, end);
                populateEventMapWithSchedules(eventMap, schedules);

                return new ArrayList<>(eventMap.values());
        }

        public List<CardRetailDTO> getRetailCardData() {
                return retailRepository.findRetailCardData();
        }

        // === Helper Methods ===

        private List<Schedule> fetchSchedules(String unitName, LocalDate endDate) {
                return (unitName != null)
                                ? scheduleRepository.findByUnitNameAndStatusPaymentAndCreatedAtBefore(unitName,
                                                endDate.atStartOfDay())
                                : scheduleRepository.findByStatusPaymentAndCreatedAtBefore(PAID_STATUS,
                                                endDate.atStartOfDay());
        }

        private boolean isWithinDateRange(Schedule schedule, LocalDate startDate, LocalDate endDate) {
                return !schedule.getScheduleStartDate().isBefore(startDate)
                                && !schedule.getScheduleEndDate().isAfter(endDate);
        }

        private <T> Map<String, BigDecimal> calculatePercentages(Map<String, T> countMap, long total) {
                return countMap.entrySet().stream()
                                .collect(Collectors.toMap(
                                                Map.Entry::getKey,
                                                entry -> BigDecimal
                                                                .valueOf(((Number) entry.getValue()).doubleValue()
                                                                                * 100.0 / total)
                                                                .setScale(2, RoundingMode.HALF_UP)));
        }

        private void ensureStartBeforeEnd(LocalDate start, LocalDate end) {
                if (end.isBefore(start)) {
                        LocalDate temp = start;
                        start = end;
                        end = temp;
                }
        }

        private BigDecimal safeBigDecimalFromDouble(Double value) {
                return (value != null) ? BigDecimal.valueOf(value) : BigDecimal.ZERO;
        }

        private long calculateMonthsBetween(LocalDate start, LocalDate end) {
                YearMonth startYM = YearMonth.from(start);
                YearMonth endYM = YearMonth.from(end);
                return startYM.until(endYM, java.time.temporal.ChronoUnit.MONTHS) + 1;
        }

        private LocalDate parseDateOrDefault(String dateStr, LocalDate defaultValue) {
                return (dateStr != null) ? LocalDate.parse(dateStr) : defaultValue;
        }

        private List<ScheduleDTO> mapSchedulesToDTO(List<Schedule> schedules, String statusPayment) {
                return schedules.stream()
                                .filter(s -> statusPayment.equals(s.getStatusPayment()))
                                .map(s -> new ScheduleDTO(
                                                s.getVenues().stream().map(Venue::getVenue)
                                                                .collect(Collectors.toList()),
                                                s.getScheduleStartDate(),
                                                s.getScheduleEndDate(),
                                                s.getStatusPayment()))
                                .collect(Collectors.toList());
        }

        private Map<String, CardEventDTO> createEventMapFromTotals(List<Object[]> totals) {
                Map<String, CardEventDTO> eventMap = new HashMap<>();

                for (Object[] total : totals) {
                        List<Venue> venues = (List<Venue>) total[0];
                        String category = (String) total[1];
                        int totalPaid = ((Number) total[2]).intValue();
                        int totalMaintenance = ((Number) total[3]).intValue();

                        String key = createEventMapKey(venues, category);

                        CardEventDTO dto = new CardEventDTO();
                        dto.setVenue(venues.stream().map(Venue::getVenue).collect(Collectors.joining(", ")));
                        dto.setCategory(category);
                        dto.setTotalPaid(totalPaid);
                        dto.setTotalMaintenance(totalMaintenance);
                        dto.setSchedules(new ArrayList<>());

                        eventMap.put(key, dto);
                }

                return eventMap;
        }

        private void populateEventMapWithSchedules(Map<String, CardEventDTO> eventMap, List<Schedule> schedules) {
                for (Schedule schedule : schedules) {
                        String key = createEventMapKey(schedule.getVenues(), schedule.getCategory());

                        ScheduleDTO scheduleDTO = new ScheduleDTO(
                                        schedule.getVenues().stream().map(Venue::getVenue).collect(Collectors.toList()),
                                        schedule.getScheduleStartDate(),
                                        schedule.getScheduleEndDate(),
                                        schedule.getStatusPayment());

                        if (eventMap.containsKey(key)) {
                                eventMap.get(key).getSchedules().add(scheduleDTO);
                        }
                }
        }

        private String createEventMapKey(List<Venue> venues, String category) {
                return venues.stream().map(Venue::getVenue).collect(Collectors.joining(",")) + ":" + category;
        }

}