package com.dev.gbk.service;

import com.dev.gbk.dto.*;
import com.dev.gbk.model.Schedule;
import com.dev.gbk.model.Venue;
import com.dev.gbk.repository.RetailRepository;
import com.dev.gbk.repository.ScheduleRepository;

import com.dev.gbk.response.Occupancy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;
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

        public Map<String, BigDecimal> getUsageByCategory(LocalDate startDate, LocalDate endDate, String unitNames) {
                List<String> units = unitNames != null ? Arrays.asList(unitNames.split(",")) : null;
                List<Schedule> schedules = scheduleRepository.findSchedules(units, startDate, endDate);

                Map<String, Long> categoryCount = schedules.stream()
                                .filter(schedule -> PAID_STATUS.equals(schedule.getStatusPayment()))
                                .filter(schedule -> schedule.getCategory() != null)
                                .collect(Collectors.groupingBy(Schedule::getCategory, Collectors.counting()));

                long totalEvents = categoryCount.values().stream().mapToLong(Long::longValue).sum();

                return calculatePercentages(categoryCount, totalEvents);
        }

        public Map<String, BigDecimal> getUsageByProfileEvent(LocalDate startDate, LocalDate endDate,
                        String unitNames) {
                List<String> units = unitNames != null ? Arrays.asList(unitNames.split(",")) : null;
                List<Schedule> schedules = scheduleRepository.findSchedules(units, startDate, endDate);

                Map<String, Long> profileEventCount = schedules.stream()
                                .filter(schedule -> PAID_STATUS.equals(schedule.getStatusPayment()))
                                .collect(Collectors.groupingBy(Schedule::getProfileEvent, Collectors.counting()));

                long totalEvents = profileEventCount.values().stream().mapToLong(Long::longValue).sum();

                return calculatePercentages(profileEventCount, totalEvents);
        }

        public Map<String, Integer> getTotalPaidGroupedByProfileEvent(LocalDate startDate, LocalDate endDate,
                        String unitNames) {
                List<String> units = unitNames != null ? Arrays.asList(unitNames.split(",")) : null;
                List<Object[]> results = scheduleRepository.sumTotalPaidGroupedByProfileEventAndDates(startDate,
                                endDate, units);

                return results.stream()
                                .collect(Collectors.toMap(
                                                result -> (String) result[0],
                                                result -> ((Number) result[1]).intValue()));
        }

        public Map<String, Integer> getTotalPaidGroupedByGames(LocalDate startDate, LocalDate endDate,
                        String unitNames) {
                List<String> units = unitNames != null ? Arrays.asList(unitNames.split(",")) : null;

                List<Object[]> results = scheduleRepository.sumTotalPaidGroupedByCategoryAndDates(startDate,
                                endDate, units);
                return results.stream()
                                .collect(Collectors.toMap(
                                                result -> (String) result[0],
                                                result -> ((Number) result[1]).intValue()));
        }

        public Integer getMonthlyParkingFee() {
                return MONTHLY_PARKING_FEE.intValue();
        }

        public IncomeDTO getTotalIncome(LocalDate startDate, LocalDate endDate, String unitNames) {
                List<String> units = unitNames != null ? Arrays.asList(unitNames.split(",")) : null;

                BigDecimal retailIncome = safeBigDecimalFromDouble(
                                retailRepository.sumPriceByStatusAndDateRangeAndArea("Sewa", null));

                BigDecimal retailOccupied = safeBigDecimalFromDouble(
                                retailRepository.sumSizeByStatusAndDateRangeAndArea("Sewa", null));

                BigDecimal retailNonOccupied = safeBigDecimalFromDouble(
                                retailRepository.sumSizeByStatusAndDateRangeAndArea("Belum Sewa",
                                                null));

                BigDecimal maintenanceVenue = safeBigDecimalFromDouble(
                                scheduleRepository.sumMaintenanceByType(units, startDate, endDate));

                List<Object> response = scheduleRepository.sumSewaLahanByStatusPayment(startDate, endDate);

                BigDecimal sewaLahan = safeBigDecimalFromDouble(response.stream()
                                .filter(obj -> "Sewa Lahan".equals(((Object[]) obj)[0]))
                                .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                .sum());

                BigDecimal sewaLahanProyeksi = safeBigDecimalFromDouble(response.stream()
                                .filter(obj -> "Sewa Lahan Proyeksi".equals(((Object[]) obj)[0]))
                                .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                .sum());

                BigDecimal gamesUmum = safeBigDecimalFromDouble(response.stream()
                                .filter(obj -> "Games Umum".equals(((Object[]) obj)[0]))
                                .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                .sum());

                BigDecimal gamesUmumProyeksi = safeBigDecimalFromDouble(response.stream()
                                .filter(obj -> "Games Umum Proyeksi".equals(((Object[]) obj)[0]))
                                .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                .sum());

                BigDecimal gamesTimnas = safeBigDecimalFromDouble(response.stream()
                                .filter(obj -> "Games Timnas".equals(((Object[]) obj)[0]))
                                .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                .sum());

                BigDecimal gamesTimnasProyeksi = safeBigDecimalFromDouble(response.stream()
                                .filter(obj -> "Games Timnas Proyeksi".equals(((Object[]) obj)[0]))
                                .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                .sum());
                BigDecimal maintenance = safeBigDecimalFromDouble(response.stream()
                                .filter(obj -> "Maintenance".equals(((Object[]) obj)[0]))
                                .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                .sum());

                BigDecimal events = safeBigDecimalFromDouble(response.stream()
                                .filter(obj -> "Events".equals(((Object[]) obj)[0]))
                                .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                .sum());

                BigDecimal eventsProyeksi = safeBigDecimalFromDouble(response.stream()
                                .filter(obj -> "Events Proyeksi".equals(((Object[]) obj)[0]))
                                .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                .sum());

                long monthsBetween = calculateMonthsBetween(startDate, endDate);
                BigDecimal totalParkingFee = MONTHLY_PARKING_FEE.multiply(BigDecimal.valueOf(monthsBetween));

                return new IncomeDTO(retailIncome, retailOccupied, retailNonOccupied, maintenanceVenue,
                                totalParkingFee,sewaLahan, sewaLahanProyeksi, gamesUmum, gamesUmumProyeksi,gamesTimnas, gamesTimnasProyeksi, maintenance, events, eventsProyeksi);
        }

        public Map<String, Integer> getProjectionTotalPaidGroupedByProfileEvent(LocalDate startDate,
                        LocalDate endDate, String unitNames) {
                List<String> units = unitNames != null ? Arrays.asList(unitNames.split(",")) : null;
                List<Object[]> results = scheduleRepository
                                .sumSoftBookingTotalPaidGroupedByProfileEventAndDates(startDate, endDate, units);
                return results.stream()
                                .collect(Collectors.toMap(
                                                result -> (String) result[0],
                                                result -> ((Number) result[1]).intValue()));
        }

        public Map<String, Integer> getProjectionTotalPaidGroupedByGames(LocalDate startDate, LocalDate endDate,
                        String unitNames) {
                List<String> units = unitNames != null ? Arrays.asList(unitNames.split(",")) : null;
                List<Object[]> results = scheduleRepository.sumSoftBookingTotalPaidGroupedByCategoryAndDates(startDate,
                                endDate, units);
                return results.stream()
                                .collect(Collectors.toMap(
                                                result -> (String) result[0],
                                                result -> ((Number) result[1]).intValue()));
        }

        public List<CardGamesDTO> getGamesCardData(LocalDate startDate, LocalDate endDate, String unitNames) {
                List<String> units = unitNames != null ? Arrays.asList(unitNames.split(",")) : null;
                List<Schedule> schedules = scheduleRepository.findSchedules(units, startDate, endDate);

                Map<String, List<Schedule>> groupedSchedules = schedules.stream()
                                .filter(schedule -> schedule.getCategory() != null)
                                .collect(Collectors.groupingBy(Schedule::getCategory));

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

        public List<CardEventDTO> getEventCardData(LocalDate startDate, LocalDate endDate, String unitNames) {
                List<String> units = unitNames != null ? Arrays.asList(unitNames.split(",")) : null;
                List<Schedule> schedules = scheduleRepository.findSchedules(units, startDate, endDate);

                Map<String, CardEventDTO> eventMap = schedules.stream()
                                .filter(schedule -> schedule.getCategory() != null)
                                .collect(Collectors.groupingBy(
                                                schedule -> createEventMapKey(schedule.getVenues(),
                                                                schedule.getCategory()),
                                                Collectors.collectingAndThen(Collectors.toList(),
                                                                this::createCardEventDTO)));

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

        public Occupancy getOccupancy(LocalDate start, LocalDate end, String venue) {
                List<Schedule> scheduleBasedOnVenue = scheduleRepository.findSingleSchedules(venue, start, end);
                List<LocalDate> dayOfVenue = new ArrayList<>();
                for (Schedule schedule : scheduleBasedOnVenue) {
                        if (schedule.getScheduleStartInLoad() != null) {
                                dayOfVenue.add(schedule.getScheduleStartInLoad());
                                dayOfVenue.add(schedule.getScheduleEndInLoad());
                        }
                        if (schedule.getScheduleStartDate() != null) {
                                dayOfVenue.add(schedule.getScheduleStartDate());
                                dayOfVenue.add(schedule.getScheduleEndDate());
                        }
                        if (schedule.getScheduleStartOutLoad() != null) {
                                dayOfVenue.add(schedule.getScheduleStartOutLoad());
                                dayOfVenue.add(schedule.getScheduleEndOutLoad());
                        }
//                        jika status payment maintenance maka tidak dihitung untuk occupancy
                        if (schedule.getStatusPayment().equals("Maintenance") || schedule.getStatusPayment().equals("Soft Booking")) {
                                dayOfVenue.remove(schedule.getScheduleStartDate());
                                dayOfVenue.remove(schedule.getScheduleEndDate());
                        }

                }
                Collections.sort(dayOfVenue);
                LocalDate earliestDate = dayOfVenue.get(0);
                LocalDate latestDate = dayOfVenue.get(dayOfVenue.size() - 1);

                Double percentationOCCFisik = calculateOCCFisikPercentage(earliestDate, latestDate);
                System.out.println("OCC Fisik: " + percentationOCCFisik);
                return new Occupancy(percentationOCCFisik);
        }

        public static Double calculateOCCFisikPercentage(LocalDate startDate, LocalDate endDate) {
                long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
                YearMonth yearMonth = YearMonth.from(startDate);
                int daysInMonth = yearMonth.lengthOfMonth();

                return (double) daysBetween / daysInMonth * 100;
        }
}