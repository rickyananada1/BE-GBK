package com.dev.gbk.service;

import com.dev.gbk.dto.*;
import com.dev.gbk.model.Schedule;
import com.dev.gbk.model.Unit;
import com.dev.gbk.model.Venue;
import com.dev.gbk.properties.SystemProperties;
import com.dev.gbk.repository.RetailRepository;
import com.dev.gbk.repository.ScheduleRepository;
import com.dev.gbk.repository.UnitRepository;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

        private static final String PAID_STATUS = "Paid";
        private static final String MAINTENANCE_STATUS = "Maintenance";
        private static final BigDecimal MONTHLY_PARKING_FEE = BigDecimal.valueOf(1_600_000); // 1.6 million

        private final ScheduleRepository scheduleRepository;
        private final RetailRepository retailRepository;
        private final UnitRepository unitRepository;
        private final VenueRepository venueRepository;
        private final SystemProperties systemProperties;

        public DashboardService(ScheduleRepository scheduleRepository,
                        RetailRepository retailRepository, UnitRepository unitRepository,
                        VenueRepository venueRepository,
                        @Qualifier("systemProperties") SystemProperties systemProperties) {
                this.scheduleRepository = scheduleRepository;
                this.retailRepository = retailRepository;
                this.unitRepository = unitRepository;
                this.venueRepository = venueRepository;
                this.systemProperties = systemProperties;
        }

        public Map<String, BigDecimal> getUsageByCategory(LocalDate startDate, LocalDate endDate, String unitNames) {
                List<String> units = unitNames != null ? Arrays.asList(unitNames.split(",")) : null;
                Unit unitData = this.unitRepository.findByName(unitNames).orElse(null);
                Map<String, BigDecimal> category = new HashMap<>();
                if (Objects.nonNull(unitData)) {
                        for(Venue venue : unitData.getVenues()) {
                                List<Schedule> schedules = scheduleRepository.findSingleSchedules(venue.getVenue(), startDate, endDate);

                                Map<String, Long> categoryCount = schedules.stream()
                                    .filter(schedule -> PAID_STATUS.equals(schedule.getStatusPayment()))
                                    .filter(schedule -> schedule.getCategory() != null)
                                    .collect(Collectors.groupingBy(Schedule::getCategory, Collectors.counting()));

                                long totalEvents = categoryCount.values().stream().mapToLong(Long::longValue).sum();
                                category.putAll(calculatePercentages(categoryCount, totalEvents));
                        }
                }
                return category;
        }

        public Map<String, BigDecimal> getUsageByProfileEvent(LocalDate startDate, LocalDate endDate,
                        String unitNames) {
                List<String> units = unitNames != null ? Arrays.asList(unitNames.split(",")) : null;
                Unit unitData = this.unitRepository.findByName(unitNames).orElse(null);
                Map<String, BigDecimal> profile = new HashMap<>();
                if (Objects.nonNull(unitData)) {
                        for(Venue venue : unitData.getVenues()) {
                                List<Schedule> schedules = scheduleRepository.findSingleSchedules(venue.getVenue(), startDate, endDate);
                                Map<String, Long> profileEventCount = schedules.stream()
                                    .filter(schedule -> PAID_STATUS.equals(schedule.getStatusPayment()))
                                    .collect(Collectors.groupingBy(Schedule::getProfileEvent, Collectors.counting()));

                                long totalEvents = profileEventCount.values().stream().mapToLong(Long::longValue).sum();

                                profile.putAll(calculatePercentages(profileEventCount, totalEvents));
                        }
                }
                return profile;

        }

        public Map<String, Integer> getTotalPaidGroupedByProfileEvent(LocalDate startDate, LocalDate endDate,
                        String unitNames) {
                List<String> units = unitNames != null ? Arrays.asList(unitNames.split(",")) : null;
                List<Object[]> results = scheduleRepository.sumTotalPaidGroupedByProfileEventAndDates(startDate,
                                endDate, unitNames);

                return results.stream()
                                .collect(Collectors.toMap(
                                                result -> (String) result[0],
                                                result -> ((Number) result[1]).intValue()));
        }

        public Map<String, Integer> getTotalPaidGroupedByGames(LocalDate startDate, LocalDate endDate,
                        String unitNames) {
                List<String> units = unitNames != null ? Arrays.asList(unitNames.split(",")) : null;

                List<Object[]> results = scheduleRepository.sumTotalPaidGroupedByCategoryAndDates(startDate,
                                endDate, unitNames);
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
                Unit unitData = this.unitRepository.findByName(unitNames).orElse(null);
                BigDecimal retailIncome = safeBigDecimalFromDouble(
                                retailRepository.sumPriceByStatusAndDateRangeAndArea("Sewa", unitNames));

                BigDecimal retailOccupied = safeBigDecimalFromDouble(
                                retailRepository.sumSizeByStatusAndDateRangeAndArea("Sewa", unitNames));

                BigDecimal retailNonOccupied = safeBigDecimalFromDouble(
                                retailRepository.sumSizeByStatusAndDateRangeAndArea("Belum Sewa",
                                                unitNames));

                BigDecimal maintenanceVenue = BigDecimal.ZERO;
                BigDecimal sewaLahan = BigDecimal.ZERO;
                BigDecimal sewaLahanProyeksi = BigDecimal.ZERO;
                BigDecimal gamesUmum = BigDecimal.ZERO;
                BigDecimal gamesUmumProyeksi = BigDecimal.ZERO;
                BigDecimal gamesTimnas = BigDecimal.ZERO;
                BigDecimal gamesTimnasProyeksi = BigDecimal.ZERO;
                BigDecimal events = BigDecimal.ZERO;
                BigDecimal eventsProyeksi = BigDecimal.ZERO;
                BigDecimal eventsNon = BigDecimal.ZERO;
                BigDecimal eventsProyeksiNon = BigDecimal.ZERO;
                if (Objects.nonNull(unitData)) {
                        List<Venue> venues = unitData.getVenues(); // Assuming Unit entity has a list of Venues
                        if (!venues.isEmpty()) {
                                for(Venue venue : venues) {
                                        BigDecimal maintenancePerVenue = safeBigDecimalFromDouble(
                                            scheduleRepository.sumMaintenanceByType(
                                                venue.getVenue(), startDate, endDate));
                                        List<Object> response = scheduleRepository.sumSewaLahanByStatusPayment(startDate, endDate, venue.getVenue());

                                        BigDecimal sewaLahanPerVenue = safeBigDecimalFromDouble(response.stream()
                                            .filter(obj -> "Sewa Lahan".equals(((Object[]) obj)[0]))
                                            .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                            .sum());

                                        BigDecimal sewaLahanProyeksiPerVenue = safeBigDecimalFromDouble(response.stream()
                                            .filter(obj -> "Sewa Lahan Proyeksi".equals(((Object[]) obj)[0]))
                                            .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                            .sum());

                                        BigDecimal gamesUmumPerVenue = safeBigDecimalFromDouble(response.stream()
                                            .filter(obj -> "Games Umum".equals(((Object[]) obj)[0]))
                                            .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                            .sum());

                                        BigDecimal gamesUmumProyeksiPerVenue = safeBigDecimalFromDouble(response.stream()
                                            .filter(obj -> "Games Umum Proyeksi".equals(((Object[]) obj)[0]))
                                            .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                            .sum());

                                        BigDecimal gamesTimnasPerVenue = safeBigDecimalFromDouble(response.stream()
                                            .filter(obj -> "Games Timnas".equals(((Object[]) obj)[0]))
                                            .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                            .sum());

                                        BigDecimal gamesTimnasProyeksiPerVenue = safeBigDecimalFromDouble(response.stream()
                                            .filter(obj -> "Games Timnas Proyeksi".equals(((Object[]) obj)[0]))
                                            .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                            .sum());

                                        BigDecimal eventsPerVenue = safeBigDecimalFromDouble(response.stream()
                                            .filter(obj -> "Events Olahraga".equals(((Object[]) obj)[0]))
                                            .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                            .sum());

                                        BigDecimal eventsProyeksiPerVenue = safeBigDecimalFromDouble(response.stream()
                                            .filter(obj -> "Events Olahraga Proyeksi".equals(((Object[]) obj)[0]))
                                            .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                            .sum());

                                        BigDecimal eventsNonPerVenue = safeBigDecimalFromDouble(response.stream()
                                            .filter(obj -> "Events Non-Olahraga".equals(((Object[]) obj)[0]))
                                            .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                            .sum());

                                        BigDecimal eventsProyeksiNonPerVenue = safeBigDecimalFromDouble(response.stream()
                                            .filter(obj -> "Events Non-Olahraga Proyeksi".equals(((Object[]) obj)[0]))
                                            .mapToDouble(obj -> ((Number) ((Object[]) obj)[1]).doubleValue())
                                            .sum());

                                        maintenanceVenue = maintenanceVenue.add(maintenancePerVenue);
                                        sewaLahan = sewaLahan.add(sewaLahanPerVenue);
                                        sewaLahanProyeksi = sewaLahanProyeksi.add(sewaLahanProyeksiPerVenue);
                                        gamesUmum = gamesUmum.add(gamesUmumPerVenue);
                                        gamesUmumProyeksi = gamesUmumProyeksi.add(gamesUmumProyeksiPerVenue);
                                        gamesTimnas = gamesTimnas.add(gamesTimnasPerVenue);
                                        gamesTimnasProyeksi = gamesTimnasProyeksi.add(gamesTimnasProyeksiPerVenue);
                                        events = events.add(eventsPerVenue);
                                        eventsProyeksi = eventsProyeksi.add(eventsProyeksiPerVenue);
                                        eventsNon = eventsNon.add(eventsNonPerVenue);
                                        eventsProyeksiNon = eventsProyeksiNon.add(eventsProyeksiNonPerVenue);
                                }
                        }
                }

                BigDecimal totalIncomeForMaintenance = BigDecimal.ZERO;
                if (Objects.nonNull(unitData)) {
                        for(Venue venue: unitData.getVenues()) {
                                // Get Total Pendapatan Maintenance based on day
                                List<Schedule> scheduleBasedOnVenue =
                                    scheduleRepository.findSingleSchedulesMaintenance(
                                        venue.getVenue(), startDate, endDate);
                                List<String> days = new ArrayList<>();
                                for (Schedule schedule : scheduleBasedOnVenue) {
                                        if (schedule.getStatusPayment().equals("Maintenance")) {
                                                days.addAll(getDayNamesBetween(schedule.getScheduleStartDate(),
                                                    schedule.getScheduleEndDate()));
                                        }
                                }
                                BigDecimal totalIncomeForMaintenancePerVenue = BigDecimal.ZERO;
                                for (String day : days) {
                                        BigDecimal priceForMaintenance = systemProperties.getMaintenance().get(day);
                                        totalIncomeForMaintenancePerVenue = totalIncomeForMaintenancePerVenue.add(priceForMaintenance);
                                }
                                totalIncomeForMaintenance = totalIncomeForMaintenance.add(totalIncomeForMaintenancePerVenue);
                        }
                }

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
                        String dayName = currentDate.getDayOfWeek().getDisplayName(TextStyle.FULL,
                                        Locale.forLanguageTag("id"));
                        dayNames.add(dayName);
                        currentDate = currentDate.plusDays(1);
                }

                return dayNames;
        }

        public Map<String, Integer> getProjectionTotalPaidGroupedByProfileEvent(LocalDate startDate,
                        LocalDate endDate, String unitNames) {
                List<String> units = unitNames != null ? Arrays.asList(unitNames.split(",")) : null;
                List<Object[]> results = scheduleRepository
                                .sumSoftBookingTotalPaidGroupedByProfileEventAndDates(startDate, endDate, unitNames);
                return results.stream()
                                .collect(Collectors.toMap(
                                                result -> (String) result[0],
                                                result -> ((Number) result[1]).intValue()));
        }

        public Map<String, Integer> getProjectionTotalPaidGroupedByGames(LocalDate startDate, LocalDate endDate,
                        String unitNames) {
                List<String> units = unitNames != null ? Arrays.asList(unitNames.split(",")) : null;
                List<Object[]> results = scheduleRepository.sumSoftBookingTotalPaidGroupedByCategoryAndDates(startDate,
                                endDate, unitNames);
                return results.stream()
                                .collect(Collectors.toMap(
                                                result -> (String) result[0],
                                                result -> ((Number) result[1]).intValue()));
        }

        public List<CardGamesDTO> getGamesCardData(LocalDate startDate, LocalDate endDate, String unitNames) {
                Unit unitData = this.unitRepository.findByName(unitNames).orElse(null);

                if (unitData == null) {
                        return Collections.emptyList();
                }

                List<Venue> venues = unitData.getVenues();
                List<CardGamesDTO> allEvents = new ArrayList<>();

                for (Venue venue : venues) {
                        List<Schedule> schedules = scheduleRepository.findSingleSchedules(venue.getVenue(), startDate, endDate);

                        Map<String, List<Schedule>> groupedSchedules = schedules.stream()
                                .filter(schedule -> {
                                        String games = schedule.getGames();
                                        return games != null && !games.trim().isEmpty() && (games.equals("Timnas") || games.equals("Umum"));
                                })
                                .collect(Collectors.groupingBy(Schedule::getGames));

                        for (Map.Entry<String, List<Schedule>> entry : groupedSchedules.entrySet()) {
                                String games = entry.getKey();
                                List<Schedule> gameSchedules = entry.getValue();

                                int totalPaid = (int) gameSchedules.stream()
                                        .filter(schedule -> PAID_STATUS.equals(schedule.getStatusPayment()))
                                        .count();

                                int totalMaintenance = (int) gameSchedules.stream()
                                        .filter(schedule -> MAINTENANCE_STATUS.equals(schedule.getStatusPayment()))
                                        .count();

                                List<ScheduleDTO> paidSchedules = createScheduleDTOList(gameSchedules, PAID_STATUS);
                                List<ScheduleDTO> maintenanceSchedules = createScheduleDTOList(gameSchedules, MAINTENANCE_STATUS);

                                CardGamesDTO cardGamesDTO = new CardGamesDTO(totalPaid, totalMaintenance, paidSchedules,
                                        maintenanceSchedules, games);
                                allEvents.add(cardGamesDTO);
                        }
                }

                return allEvents;
        }

        private List<ScheduleDTO> createScheduleDTOList(List<Schedule> gameSchedules, String status) {
                return gameSchedules.stream()
                        .filter(schedule -> status.equals(schedule.getStatusPayment()))
                        .map(schedule -> new ScheduleDTO(
                                schedule.getVenues().stream().map(Venue::getVenue).collect(Collectors.toList()),
                                schedule.getScheduleStartDate(),
                                schedule.getScheduleEndDate(),
                                schedule.getStatusPayment(),
                                schedule.getDescriptionEvent(),
                                schedule.getScheduleTime(),
                                schedule.getSession()))
                        .collect(Collectors.toList());
        }

        public List<CardEventDTO> getEventCardData(LocalDate startDate, LocalDate endDate, String unitNames) {
                Unit unitData = this.unitRepository.findByName(unitNames).orElse(null);

                if (Objects.isNull(unitData)) {
                        return Collections.emptyList();
                }

                List<Venue> venues = unitData.getVenues();
                List<CardEventDTO> allEvents = new ArrayList<>();

                for (Venue venue : venues) {
                        if (venue.getIsEligibleSession()) {
                        }
                        List<Schedule> schedules = scheduleRepository.findSingleSchedules(venue.getVenue(), startDate, endDate);

                        Map<String, CardEventDTO> eventMap = schedules.stream()
                                .filter(schedule -> !"Sewa Lahan".equals(schedule.getCategory()) && !"Timnas".equals(schedule.getGames()) && !"Umum".equals(schedule.getGames()))
                                .collect(Collectors.groupingBy(
                                        schedule -> createEventMapKey(schedule.getVenues(), schedule.getCategory()),
                                        Collectors.collectingAndThen(Collectors.toList(), this::createCardEventDTO)
                                ));

                        allEvents.addAll(eventMap.values());
                }

                return allEvents;
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
                                                schedule.getStatusPayment(),schedule.getDescriptionEvent(),schedule.getScheduleTime(),schedule.getSession()))
                                .collect(Collectors.toList());

                String venue = schedules.get(0).getVenues().stream().map(Venue::getVenue)
                                .collect(Collectors.joining(","));
                String category = schedules.get(0).getCategory();

                return new CardEventDTO(venue, category, totalPaid, totalMaintenance, scheduleDTOs);
        }

        public Map<String, Object> getRetailCardData(String unit) {
                Double percentage = retailRepository.getOverallPercentage(unit);
                List<Object[]> results = getRetailCardDataNative(unit);
                List<CardRetailDTO> dtos = new ArrayList<>();
                for (Object[] result : results) {
                        String tenantName = (String) result[0];
                        String area = (String) result[1];

                        CardRetailDTO dto = new CardRetailDTO(tenantName, area);
                        dtos.add(dto);
                }


                Map<String, Object> result = new HashMap<>();
                result.put("percentage", percentage);
                result.put("data", dtos);
                return result;
        }

        private List<Object[]> getRetailCardDataNative(String unit) {
                return retailRepository.getRetailCardDataNative(unit);
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

        public Occupancy getOccupancyPerUnit(LocalDate start, LocalDate end, String unit) {
                Unit unitData = this.unitRepository.findByName(unit).orElse(null);

                System.out.println("Test : ");

                if (Objects.nonNull(unitData)) {
                        List<Venue> venues = unitData.getVenues(); // Assuming Unit entity has a list of Venues

                        if (!venues.isEmpty()) {
                                BigDecimal totalOccFisik = BigDecimal.ZERO;
                                BigDecimal totalOccPKBLU = BigDecimal.ZERO;
                                BigDecimal totalOccMaintenance = BigDecimal.ZERO;
                                BigDecimal totalRetailOccupied = BigDecimal.ZERO;
                                BigDecimal totalTimnasOcc = BigDecimal.ZERO;

                                for (Venue venue : venues) {
                                        // Calculate OCC for each venue, using existing methods
                                        Occupancy venueOccupancy;
                                        if (venue.getIsEligibleSession()) {
                                                venueOccupancy = getOccupancyForEligibleSessionVenue(start, end,
                                                                venue.getVenue());
                                        } else {
                                                venueOccupancy = getOccupancyForNotEligibleSessionVenue(start, end,
                                                                venue.getVenue());
                                        }

                                        // Sum up the OCC values for all venues
                                        totalOccFisik = totalOccFisik
                                                        .add(BigDecimal.valueOf(venueOccupancy.getOccFisik()));
                                        totalOccPKBLU = totalOccPKBLU
                                                        .add(BigDecimal.valueOf(venueOccupancy.getOccPKBLUHari()));
                                        totalOccMaintenance = totalOccMaintenance
                                                        .add(BigDecimal.valueOf(venueOccupancy.getOccMaintenance()));
                                        totalRetailOccupied = totalRetailOccupied
                                                        .add(BigDecimal.valueOf(venueOccupancy.getOccRetail()));
                                        totalTimnasOcc = totalTimnasOcc.add(BigDecimal.valueOf(venueOccupancy.getOccTimnas()));
                                }
                                List<Object[]> retailFromVenue = this.retailRepository.findSumPaidAndAllRecordForRetail(unit, start,
                                    end);
                                BigDecimal retailOccupied = BigDecimal.ZERO;
                                for (Object[] retail : retailFromVenue) {
                                        totalRetailOccupied = getSingleValueWIthIndex(retail, 0); // Index 0 for
                                        // totalPercentage
                                        BigDecimal totalAllRetail = getSingleValueWIthIndex(retail, 1); // Index 0 for totalPercentage
                                        if (totalRetailOccupied == BigDecimal.ZERO && totalAllRetail == BigDecimal.ZERO)
                                                break;
                                        retailOccupied = totalRetailOccupied.divide(totalAllRetail,
                                                MathContext.DECIMAL128) // Use a context with sufficient precision
                                            .multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                                }
                                int numberOfVenues = venues.size();
                                Object test = retailFromVenue.get(0);
                                if (test == null) {
                                        totalOccFisik = totalOccFisik.divide(BigDecimal.valueOf(numberOfVenues),
                                            BigDecimal.ROUND_HALF_UP);
                                } else {
                                        totalOccFisik = ((totalOccFisik.divide(BigDecimal.valueOf(numberOfVenues),
                                            BigDecimal.ROUND_HALF_UP)).add(retailOccupied)).divide(BigDecimal.valueOf(2));
                                }
                                // Calculate average occupancy across all venues for the unit
                                return new Occupancy(totalOccFisik.doubleValue(),
                                    totalOccPKBLU.divide(BigDecimal.valueOf(numberOfVenues),
                                        BigDecimal.ROUND_HALF_UP).doubleValue(),
                                    totalOccMaintenance.divide(BigDecimal.valueOf(numberOfVenues),
                                        BigDecimal.ROUND_HALF_UP).doubleValue(),
                                    retailOccupied.doubleValue(),
                                    totalTimnasOcc.divide(BigDecimal.valueOf(numberOfVenues),
                                        BigDecimal.ROUND_HALF_UP).doubleValue());
                        }
                }

                return null;
        }

        public Occupancy getOccupancy(LocalDate start, LocalDate end, String venue) {
                Venue venue1 = this.venueRepository.findByVenue(venue).orElse(null);
                if (Objects.nonNull(venue1)) {
                        if (venue1.getIsEligibleSession()) {
                                return getOccupancyForEligibleSessionVenue(start, end, venue);
                        } else {
                                return getOccupancyForNotEligibleSessionVenue(start, end, venue);
                        }
                }
                return null;
        }

        private Occupancy getOccupancyForEligibleSessionVenue(LocalDate start, LocalDate end, String venue) {

                List<Object[]> schedule = this.scheduleRepository.findSumOfSchedulesPerDay(venue, start, end);
                int count = 0;
                int daysInMonth = start.lengthOfMonth();
                int session = daysInMonth * 8;
                int tempSession = 8;
                BigDecimal sumOfPercentage = BigDecimal.ZERO;
                BigDecimal sumOfMaintenance = BigDecimal.ZERO;
                BigDecimal sumOfPercentageTimnas = BigDecimal.ZERO;
                for (Object[] ob : schedule) {
                        BigDecimal percentageTimnas = getSingleValueWIthIndex(ob, 0); // Index 0 for totalPercentage
                        BigDecimal percentage = getSingleValueWIthIndex(ob, 1); // Index 0 for totalPercentage
                        BigDecimal percentageMaintenance = getSingleValueWIthIndex(ob, 2); // Index 0 for
                                                                                           // totalPercentage
                        BigDecimal sumOfEligibleSession =
                            BigDecimal.valueOf(tempSession).subtract(percentageMaintenance);

                        percentageTimnas =
                            percentageTimnas.divide(sumOfEligibleSession, MathContext.DECIMAL128);

                        percentage =
                            percentage.divide(sumOfEligibleSession, MathContext.DECIMAL128);

                        sumOfMaintenance = sumOfMaintenance.add(percentageMaintenance);

                        sumOfPercentageTimnas = sumOfPercentageTimnas.add(percentageTimnas);

                        sumOfPercentage = sumOfPercentage.add(percentage);

                        count++;
                }
                if (count < daysInMonth) {
                        count += daysInMonth - count;
                }
                BigDecimal resultOfPercentageTimnas = BigDecimal.ZERO;
                try {
                        resultOfPercentageTimnas = sumOfPercentageTimnas.divide(BigDecimal.valueOf(count),
                            MathContext.DECIMAL128);
                } catch (ArithmeticException e) {
                        System.out.println("There is No Used Session");
                }
                BigDecimal resultOfPercentage = BigDecimal.ZERO;
                try {
                        resultOfPercentage = sumOfPercentage.divide(BigDecimal.valueOf(count),
                            MathContext.DECIMAL128);
                        resultOfPercentage = resultOfPercentage.setScale(2, RoundingMode.HALF_UP);
                        resultOfPercentage =
                            (resultOfPercentage.add(resultOfPercentageTimnas));
                } catch (ArithmeticException e) {
                        System.out.println("There is No Used Session");
                }

                BigDecimal resultOfPercentageMaintenance = BigDecimal.ZERO;
                try {
                        resultOfPercentageMaintenance =
                            sumOfMaintenance.divide(BigDecimal.valueOf(session),
                                MathContext.DECIMAL128);

                } catch (ArithmeticException e) {
                        System.out.println("There is No Used Session");
                }

                double pkblu = 0.0;
                try {
                        List<String> schedulePkblu =
                            this.scheduleRepository.findScheduleWithPaidStatus(venue, start, end);
                        pkblu = (double) schedulePkblu.size() / daysInMonth * 100;
                } catch (ArithmeticException e) {
                        System.out.println("There is No Used Session");
                }
                return new Occupancy(resultOfPercentage.doubleValue() * 100, pkblu,
                    resultOfPercentageMaintenance.doubleValue() * 100, 0d, resultOfPercentageTimnas.doubleValue() * 100);
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
                                daysBetween = ChronoUnit.DAYS.between(schedule.getScheduleStartDate(),
                                                schedule.getScheduleEndDate()) + 1;
                                daysMaintenance += daysBetween;
                                continue;
                        }
                        if (schedule.getScheduleStartDate() != null) {
                                daysBetween = ChronoUnit.DAYS.between(schedule.getScheduleStartDate(),
                                                schedule.getScheduleEndDate()) + 1;
                                daysEventTime += daysBetween;
                                isEventDaySameWithOutLoad = schedule.getScheduleStartOutLoad()
                                                .equals(schedule.getScheduleEndDate());
                                isEventDaySameWithInLoad = schedule.getScheduleStartInLoad()
                                                .equals(schedule.getScheduleStartDate());
                        }
                        if (schedule.getScheduleStartInLoad() != null && !(isEventDaySameWithInLoad)) {
                                daysBetween = ChronoUnit.DAYS.between(schedule.getScheduleStartInLoad(),
                                                schedule.getScheduleEndInLoad()) + 1;
                                daysEventTime += daysBetween;
                        }
                        if (schedule.getScheduleStartOutLoad() != null && !isEventDaySameWithOutLoad) {
                                daysBetween = ChronoUnit.DAYS.between(schedule.getScheduleStartOutLoad(),
                                                schedule.getScheduleEndOutLoad()) + 1;
                                daysEventTime += daysBetween;
                        }

                }
                Double percentationOCCFisik = calculateOCCFisikPercentage(start, end, daysEventTime);
                Double percentationOCCPKBLU = calculateOCCPKBLUPercentageDays(start, end, daysEventTime,
                                daysMaintenance);
                Double percentationOccMaintenance = calculateOCCMaintenancePercentage(start, end, daysMaintenance);
                if (Double.isInfinite(percentationOCCPKBLU)) {
                        percentationOCCPKBLU = 100.0;
                }
                return new Occupancy(percentationOCCFisik, percentationOCCPKBLU,
                                percentationOccMaintenance, 0.0, 0.0);
        }

        @NotNull
        private BigDecimal getSingleValueWIthIndex(Object[] retail, int x) {
                return retail[x] != null ? new BigDecimal(retail[x].toString()) : BigDecimal.ZERO;
        }

        public static Double calculateOCCFisikPercentage(LocalDate startDate, LocalDate endDate, Long daysBetween) {
                long totalDaysInRange = ChronoUnit.DAYS.between(startDate, endDate) + 1;
                return (double) daysBetween / totalDaysInRange * 100;
        }

        public static Double calculateOCCMaintenancePercentage(LocalDate startDate, LocalDate endDate,
                        Long daysBetween) {
                return calculateOCCFisikPercentage(startDate, endDate, daysBetween);
        }

        public static Double calculateOCCPKBLUPercentageDays(LocalDate startDate, LocalDate endDate, Long daysBetween,
                        Long maintenance) {
                long totalDaysInRange = ChronoUnit.DAYS.between(startDate, endDate) + 1;
                return (double) daysBetween / (totalDaysInRange - maintenance) * 100;
        }

        public Map<String, Object> getSewaLahanCardData(String unit, LocalDate startDate, LocalDate endDate) {
                Unit unitData = this.unitRepository.findByName(unit).orElse(null);
                if (Objects.isNull(unitData)) {
                        return Collections.emptyMap();
                }

                List<Venue> venues = unitData.getVenues();
                if (venues.isEmpty()) {
                        return Collections.emptyMap();
                }

                Long total = 0L;
                List<CardSewaLahanDTO> allDetails = new ArrayList<>();

                for (Venue venue : venues) {
                        Long venueTotal = scheduleRepository.getOverallPercentage(venue.getVenue(), startDate, endDate);
                        List<CardSewaLahanDTO> venueDetails = scheduleRepository.getSewaLahanCardData(venue.getVenue(), startDate, endDate);

                        if (venueTotal != null) {
                                total += venueTotal;
                        }

                        if (venueDetails != null && !venueDetails.isEmpty()) {
                                allDetails.addAll(venueDetails);
                        }
                }

                Map<String, Object> result = new HashMap<>();
                result.put("percentage", total);
                result.put("details", allDetails);

                return result;
        }


}