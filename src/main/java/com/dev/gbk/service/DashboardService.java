package com.dev.gbk.service;

import com.dev.gbk.dto.*;
import com.dev.gbk.model.Schedule;
import com.dev.gbk.model.Venue;
import com.dev.gbk.properties.SystemProperties;
import com.dev.gbk.repository.RetailRepository;
import com.dev.gbk.repository.ScheduleRepository;
import com.dev.gbk.repository.VenueRepository;
import com.dev.gbk.response.Occupancy;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DashboardService {

        private static final String PAID_STATUS = "Paid";
        private static final String MAINTENANCE_STATUS = "Maintenance";
        private static final BigDecimal MONTHLY_PARKING_FEE = BigDecimal.valueOf(1_600_000); // 1.6 million

        private final ScheduleRepository scheduleRepository;
        private final RetailRepository retailRepository;
        private final VenueRepository venueRepository;
        private final SystemProperties systemProperties;

        public DashboardService(ScheduleRepository scheduleRepository,
            RetailRepository retailRepository, VenueRepository venueRepository,
            @Qualifier("systemProperties") SystemProperties systemProperties) {
                this.scheduleRepository = scheduleRepository;
                this.retailRepository = retailRepository;
                this.venueRepository = venueRepository;
                this.systemProperties = systemProperties;
        }

        public Map<String, BigDecimal> getUsageByCategory(LocalDate startDate, LocalDate endDate, String unitNames) {
                List<String> units = unitNames != null ? Arrays.asList(unitNames.split(",")) : null;
                List<Schedule> schedules = scheduleRepository.findSingleSchedules(unitNames, startDate, endDate);

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
                List<Schedule> schedules = scheduleRepository.findSingleSchedules(unitNames, startDate, endDate);

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
                                retailRepository.sumPriceByStatusAndDateRangeAndArea("Sewa", unitNames));

                BigDecimal retailOccupied = safeBigDecimalFromDouble(
                                retailRepository.sumSizeByStatusAndDateRangeAndArea("Sewa", unitNames));

                BigDecimal retailNonOccupied = safeBigDecimalFromDouble(
                                retailRepository.sumSizeByStatusAndDateRangeAndArea("Belum Sewa",
                                        unitNames));

                BigDecimal maintenanceVenue = safeBigDecimalFromDouble(
                                scheduleRepository.sumMaintenanceByType(units, startDate, endDate));

                List<Object> response = scheduleRepository.sumSewaLahanByStatusPayment(startDate, endDate, unitNames);

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

                //Get Total Pendapatan Maintenance based on day
                List<Schedule> scheduleBasedOnVenue =
                    scheduleRepository.findSingleSchedulesMaintenance(unitNames, startDate,
                        endDate);
                List<String> days = new ArrayList<>();
                for (Schedule schedule : scheduleBasedOnVenue) {
                        if (schedule.getStatusPayment().equals("Maintenance")) {
                                    days.addAll(getDayNamesBetween(schedule.getScheduleStartDate(),
                                        schedule.getScheduleEndDate()));
                        }
                }
                BigDecimal totalIncomeForMaintenance = BigDecimal.ZERO;
                for (String day : days) {
                        BigDecimal priceForMaintenance = systemProperties.getMaintenance().get(day);
                        totalIncomeForMaintenance = totalIncomeForMaintenance.add(priceForMaintenance);
                }

                BigDecimal events = safeBigDecimalFromDouble(response.stream()
                                .filter(obj -> "Events Olahraga".equals(((Object[]) obj)[0]))
                                .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                .sum());

                BigDecimal eventsProyeksi = safeBigDecimalFromDouble(response.stream()
                                .filter(obj -> "Events Olahraga Proyeksi".equals(((Object[]) obj)[0]))
                                .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                .sum());

                BigDecimal eventsNon = safeBigDecimalFromDouble(response.stream()
                        .filter(obj -> "Events Non-Olahraga".equals(((Object[]) obj)[0]))
                        .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                        .sum());

                BigDecimal eventsProyeksiNon = safeBigDecimalFromDouble(response.stream()
                        .filter(obj -> "Events Non-Olahraga Proyeksi".equals(((Object[]) obj)[0]))
                        .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                        .sum());

                long monthsBetween = calculateMonthsBetween(startDate, endDate);
                BigDecimal totalParkingFee = MONTHLY_PARKING_FEE.multiply(BigDecimal.valueOf(monthsBetween));

                return new IncomeDTO(retailIncome, retailOccupied, retailNonOccupied,
                    maintenanceVenue, totalParkingFee, sewaLahan, sewaLahanProyeksi, gamesUmum,
                    gamesUmumProyeksi, gamesTimnas, gamesTimnasProyeksi, totalIncomeForMaintenance,
                    events, eventsProyeksi, eventsNon, eventsProyeksiNon);
        }

        private List<String> getDayNamesBetween(LocalDate startDate, LocalDate endDate) {
                List<String> dayNames = new ArrayList<>();
                LocalDate currentDate = startDate;

                while (!currentDate.isAfter(endDate)) {
                        String dayName = currentDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("id"));
                        dayNames.add(dayName);
                        currentDate = currentDate.plusDays(1);
                }

                return dayNames;
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
                List<Schedule> schedules = scheduleRepository.findSingleSchedules(unitNames, startDate, endDate);

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
                List<Schedule> schedules = scheduleRepository.findSingleSchedules(unitNames, startDate, endDate);

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

        public Map<String, Object> getRetailCardData(String unit) {
                Double percentage = retailRepository.getOverallPercentage(unit);
                List<CardRetailDTO> data = retailRepository.getRetailCardData(unit);

                Map<String, Object> result = new HashMap<>();
                result.put("percentage", percentage);
                result.put("data", data);
                return result;
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
                Venue venue1 = this.venueRepository.findByVenue(venue).orElse(null);
                if (Objects.nonNull(venue1)) {
                        if (venue1.getIsEligibleSession()) {
                                return getOccupancyForEligibleSessionVenue(start,end,venue);
                        } else {
                                return getOccupancyForNotEligibleSessionVenue(start ,end, venue);
                        }
                }
                return null;
        }

        private Occupancy getOccupancyForEligibleSessionVenue(LocalDate start, LocalDate end, String venue) {

                List<Object[]> schedule = this.scheduleRepository.findSumOfSchedulesPerDay(venue, start, end);
                int count = 0;
                BigDecimal sumOfPercentage = BigDecimal.ZERO;
                BigDecimal sumOfMaintenance = BigDecimal.ZERO;
                for (Object[] ob : schedule) {
                        BigDecimal percentage = getSingleValueWIthIndex(ob, 0); // Index 0 for totalPercentage
                        BigDecimal percentageMaintenance = getSingleValueWIthIndex(ob, 1); // Index 0 for totalPercentage
                        sumOfPercentage = sumOfPercentage.add(percentage);
                        sumOfMaintenance = sumOfMaintenance.add(percentageMaintenance);
                        count++;

                }
                BigDecimal resultOfPercentage = sumOfPercentage.divide(BigDecimal.valueOf(count), BigDecimal.ROUND_HALF_UP);

                BigDecimal resultOfPercentageMaintenance =
                    sumOfMaintenance.divide(BigDecimal.valueOf(count), BigDecimal.ROUND_HALF_UP);

                List<String> schedulePkblu = this.scheduleRepository.findScheduleWithPaidStatus(venue, start, end);
                int daysInMonth = start.lengthOfMonth();
                double pkblu = (double) schedulePkblu.size() / daysInMonth * 100;
                return new Occupancy(resultOfPercentage.doubleValue() * 100, pkblu,
                    resultOfPercentageMaintenance.doubleValue(), 0d);
        }


        private Occupancy getOccupancyForNotEligibleSessionVenue(LocalDate start, LocalDate end, String venue) {
                List<Schedule> scheduleBasedOnVenue = scheduleRepository.findSingleSchedules(venue, start, end);
                long daysEventTime = 0;
                long daysMaintenance = 0;
                boolean isEventDaySameWithInLoad = false;
                boolean isEventDaySameWithOutLoad = false;
                for (Schedule schedule : scheduleBasedOnVenue) {
                        long daysBetween = 0;
                        if (schedule.getStatusPayment().equals("Maintenance")) {
                                daysBetween =
                                    ChronoUnit.DAYS.between(schedule.getScheduleStartDate(),
                                        schedule.getScheduleEndDate()) + 1;
                                daysMaintenance += daysBetween;
                                continue;
                        }
                        if (schedule.getScheduleStartDate() != null) {
                                daysBetween =
                                    ChronoUnit.DAYS.between(schedule.getScheduleStartDate(),
                                        schedule.getScheduleEndDate()) + 1;
                                daysEventTime += daysBetween;
                                isEventDaySameWithOutLoad = schedule.getScheduleStartOutLoad().equals(schedule.getScheduleEndDate());
                                isEventDaySameWithInLoad = schedule.getScheduleStartInLoad().equals(schedule.getScheduleStartDate());
                        }
                        if (schedule.getScheduleStartInLoad() != null && !(isEventDaySameWithInLoad)) {
                                daysBetween =
                                    ChronoUnit.DAYS.between(schedule.getScheduleStartInLoad(),
                                        schedule.getScheduleEndInLoad()) + 1;
                                daysEventTime += daysBetween;
                        }
                        if (schedule.getScheduleStartOutLoad() != null && !isEventDaySameWithOutLoad) {
                                daysBetween =
                                    ChronoUnit.DAYS.between(schedule.getScheduleStartOutLoad(),
                                        schedule.getScheduleEndOutLoad()) + 1;
                                daysEventTime += daysBetween;
                        }

                }

                List<Object[]> retailFromVenue =
                    this.retailRepository.findSumPaidAndAllRecordForRetail(venue, start, end);
                BigDecimal retailOccupied = BigDecimal.ZERO;
                for (Object[] retail : retailFromVenue) {
                  BigDecimal totalRetailOccupied = getSingleValueWIthIndex(retail, 0); // Index 0 for totalPercentage
                  BigDecimal totalAllRetail = getSingleValueWIthIndex(retail, 1); // Index 0 for totalPercentage
                  if (totalRetailOccupied == BigDecimal.ZERO && totalAllRetail == BigDecimal.ZERO)
                    break;
                  retailOccupied = totalRetailOccupied.divide(totalAllRetail,
                          MathContext.DECIMAL128) // Use a context with sufficient precision
                      .multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                Double percentationOCCFisik = calculateOCCFisikPercentage(start, daysEventTime);
                Double percentationOCCPKBLU = calculateOCCPKBLUPercentageDays(start, daysEventTime, daysMaintenance);
                Double percentationOccMaintenance = calculateOCCMaintenancePercentage(start, daysMaintenance);
                if (Double.isInfinite(percentationOCCPKBLU)) {
                        percentationOCCPKBLU = 100.0;
                }
          return new Occupancy(percentationOCCFisik, percentationOCCPKBLU,
              percentationOccMaintenance, retailOccupied.doubleValue());
        }

        @NotNull
        private BigDecimal getSingleValueWIthIndex(Object[] retail, int x) {
                return retail[x] != null ? new BigDecimal(retail[x].toString()) : BigDecimal.ZERO;
        }

        public static Double calculateOCCFisikPercentage(LocalDate startDate, Long daysBetween) {
                YearMonth yearMonth = YearMonth.from(startDate);
                int daysInMonth = yearMonth.lengthOfMonth();

                return (double) daysBetween / daysInMonth * 100;
        }


        public static Double calculateOCCMaintenancePercentage(LocalDate startDate, Long daysBetween) {
                return  calculateOCCFisikPercentage(startDate, daysBetween);
        }

        public static Double calculateOCCPKBLUPercentageDays(LocalDate startDate, Long daysBetween, Long maintenance) {
                YearMonth yearMonth = YearMonth.from(startDate);
                int daysInMonth = yearMonth.lengthOfMonth();

                return (double) daysBetween / (daysInMonth - maintenance )* 100;
        }

        public Map<String, Object> getSewaLahanCardData(String unit, LocalDate startDate, LocalDate endDate) {
                Long total = scheduleRepository.getOverallPercentage(unit, startDate, endDate);
                List<CardSewaLahanDTO> data = scheduleRepository.getSewaLahanCardData(unit, startDate, endDate);
                System.out.println("sewa lahan card data" + data);
                Map<String, Object> result = new HashMap<>();
                result.put("percentage", total);
                result.put("details", data);

                return result;
        }

}