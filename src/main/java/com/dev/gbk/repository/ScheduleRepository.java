package com.dev.gbk.repository;

import com.dev.gbk.dto.CardSewaLahanDTO;
import com.dev.gbk.model.Schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {
//        buat native query
        @Query(value = "SELECT s.* " +
                "FROM schedules s " +
                "INNER JOIN schedules_venues sv ON sv.schedule_id = s.id " +
                "INNER JOIN venues v ON v.id = sv.venue_id " +
                "INNER JOIN units u ON u.id = v.unit_id " +
                "WHERE s.status_payment = :statusPayment " +
                "AND s.start_date BETWEEN NOW() AND NOW() + INTERVAL 5 DAY " +
                "ORDER BY s.updated_at DESC, s.created_at DESC", nativeQuery = true)
        List<Schedule> findByStatusPaymentAndCreatedAtBefore(String statusPayment);


        @Query(value = "SELECT s.* " +
                "FROM schedules s " +
                "INNER JOIN schedules_venues sv ON sv.schedule_id = s.id " +
                "INNER JOIN venues v ON v.id = sv.venue_id " +
                "INNER JOIN units u ON u.id = v.unit_id " +
                "WHERE s.status_payment = :statusPayment"+
                "AND s.start_date BETWEEN NOW() AND NOW() + INTERVAL 5 DAY " +
                "AND v.unit_id = :venueId " +
                "ORDER BY s.updated_at DESC, s.created_at DESC", nativeQuery = true)
        List<Schedule> findByStatusPaymentAndVenuesIdAndCreatedAtBefore(
                        String statusPayment, Long venueId);

        boolean existsByBookingNumber(String bookingNumber);

        Schedule findTopByOrderByIdDesc();

        @Query("SELECT s.profileEvent, SUM(s.totalPaid) FROM Schedule s JOIN s.venues v JOIN v.unit u WHERE (:unitNames IS NULL OR :unitNames = '' OR u.name = :unitNames) AND s.statusPayment = 'Paid' AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate GROUP BY s.profileEvent")
        List<Object[]> sumTotalPaidGroupedByProfileEventAndDates(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("unitNames") String unitNames);

        @Query("SELECT s.category, SUM(s.totalPaid) FROM Schedule s JOIN s.venues v JOIN v.unit u WHERE (:unitNames IS NULL OR :unitNames = '' OR u.name = :unitNames) AND s.statusPayment = 'Paid' AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate AND s.category IS NOT NULL GROUP BY s.category")
        List<Object[]> sumTotalPaidGroupedByCategoryAndDates(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("unitNames") String unitNames);

        @Query("SELECT s.profileEvent, SUM(s.totalPaid) FROM Schedule s JOIN s.venues v JOIN v.unit u WHERE (:unitNames IS NULL  OR :unitNames = '' OR u.name = :unitNames) AND s.statusBooking = 'Soft Booking' AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate GROUP BY s.profileEvent")
        List<Object[]> sumSoftBookingTotalPaidGroupedByProfileEventAndDates(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("unitNames") String unitNames);

        @Query("SELECT s.category, SUM(s.totalPaid) FROM Schedule s JOIN s.venues v JOIN v.unit u WHERE (:unitNames IS NULL  OR :unitNames = '' OR u.name = :unitNames) AND s.statusBooking = 'Soft Booking' AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate GROUP BY s.category")
        List<Object[]> sumSoftBookingTotalPaidGroupedByCategoryAndDates(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("unitNames") String unitNames);

        @Query("SELECT s.scheduleStartDate, SUM(s.totalPaid) FROM Schedule s " +
                        "JOIN s.venues v JOIN v.unit u " +
                        "WHERE (:unitNames IS NULL  OR :unitNames = '' OR u.name = :unitNames) " +
                        "AND s.statusBooking = 'Maintenance' AND s.scheduleStartDate BETWEEN :startDate AND :endDate " +
                        "GROUP BY s.scheduleStartDate")
        List<Object[]> sumMaintenanceByDay(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("unitNames") String unitNames);

        @Query("SELECT SUM(s.totalPaid) FROM Schedule s JOIN s.venues v JOIN v.unit u WHERE (:unitNames IS NULL OR :unitNames = '' OR u.name = :unitNames) AND s.statusPayment = 'Maintenance' "
                        + "AND ((:startDate IS NULL AND :endDate IS NULL AND s.scheduleStartDate IS NOT NULL) "
                        + "OR (:startDate IS NOT NULL AND :endDate IS NOT NULL AND "
                        + "(s.scheduleStartDate IS NOT NULL AND s.scheduleEndDate IS NOT NULL AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate)))")
        Double sumMaintenanceByType(@Param("unitNames") String unitNames,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);


        @Query("SELECT " +
                "    CASE " +
                "        WHEN r.category = 'Sewa Lahan' AND r.statusPayment = 'Paid' AND r.statusBooking = 'Processing' THEN 'Sewa Lahan' " +
                "        WHEN r.games = 'Umum' AND r.statusPayment = 'Paid' AND r.statusBooking = 'Processing' THEN 'Games Umum' " +
                "        WHEN r.games = 'Timnas' AND r.statusPayment = 'Paid' AND r.statusBooking = 'Processing' THEN 'Games Timnas' " +
                "        WHEN r.statusPayment = 'Maintenance' THEN 'Maintenance' " +
                "        WHEN r.category = 'Olahraga' AND r.statusPayment = 'Paid' AND r.statusBooking = 'Processing' THEN 'Events Olahraga' " +
                "        WHEN r.category IS NOT NULL AND r.category != '' AND r.category != 'Sewa Lahan' AND r.category != 'Olahraga' AND r.statusPayment = 'Paid' AND r.statusBooking = 'Processing' THEN 'Events Non-Olahraga' " +
                "        WHEN r.category = 'Sewa Lahan' AND r.statusPayment != 'Paid' AND r.statusBooking = 'Processing' THEN 'Sewa Lahan Proyeksi' " +
                "        WHEN r.games = 'Umum' AND r.statusPayment != 'Paid' AND r.statusBooking = 'Processing' THEN 'Games Umum Proyeksi' " +
                "        WHEN r.games = 'Timnas' AND r.statusPayment != 'Paid' AND r.statusBooking = 'Processing' THEN 'Games Timnas Proyeksi' " +
                "        WHEN r.statusPayment = 'Maintenance' THEN 'Maintenance' " +
                "        WHEN r.category = 'Olahraga' AND r.statusPayment != 'Paid' AND r.statusBooking = 'Processing' THEN 'Events Olahraga Proyeksi' " +
                "        WHEN r.category IS NOT NULL AND r.category != '' AND r.category != 'Sewa Lahan' AND r.category != 'Olahraga' AND r.statusPayment != 'Paid' AND r.statusBooking = 'Processing' THEN 'Events Non-Olahraga Proyeksi' " +
                "    END AS kategori, " +
                "    SUM(r.totalPaid) + SUM(r.totalSF) AS total " +
                "FROM Schedule r JOIN r.venues v " +
                "WHERE r.scheduleStartDate >= :startDate AND r.scheduleEndDate <= :endDate " +
                "AND (:venues IS NULL OR :venues = '' OR v.venue = :venues) " +
                "GROUP BY kategori")
        List<Object> sumSewaLahanByStatusPayment(@Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate, @Param("venues") String venues);



        @Query("SELECT s FROM Schedule s " +
                        "JOIN s.venues v " +
                        "WHERE (:units IS NULL OR :units = ''OR v.unit.name IN :units) " +
                        "AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate")
        List<Schedule> findSchedules(@Param("units") String units, @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        @Query("SELECT s FROM Schedule s JOIN s.venues v WHERE (:unit IS NULL OR :unit = '' OR v.venue = :unit) AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate")
        List<Schedule> findSingleSchedules(@Param("unit") String unit, @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);


        @Query("SELECT s FROM Schedule s JOIN s.venues v WHERE(:unit IS NULL OR  :unit = '' OR v.venue = :unit )AND s.scheduleStartDate >= :startDate AND s.scheduleEndDate <= :endDate AND s.statusPayment = 'Maintenance'")
        List<Schedule> findSingleSchedulesMaintenance(@Param("unit") String unit, @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

        @Query(value = "SELECT "
            + "(SUM(CASE WHEN s.games = 'Timnas' THEN 1 ELSE 0 END)) AS totalPercentageTimnas, "
            + "(SUM(CASE WHEN s.status_payment = 'Paid' AND s.games != 'Timnas' THEN 1 ELSE 0 END)) AS totalPercentage, "
            + "(COALESCE(SUM(CASE WHEN s.status_payment = 'Maintenance' THEN 1 ELSE 0 END), 0)) AS totalMaintenance "
            + "FROM schedules s inner JOIN "
            + "schedules_venues sv "
            + "on s.id= sv.schedule_id join "
            + "schedule_times st ON s.id = st.schedule_id "
            + "JOIN venues v2  on sv.venue_id=v2.id "
            + "join units u on v2.unit_id =u.id "
            + "WHERE v2.venue = :unit AND s.start_date >= :startDate AND s.end_date <= :endDate GROUP by s.start_date ;",
            nativeQuery = true)
        List<Object[]> findSumOfSchedulesPerDay(@Param("unit") String unit, @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

        @Query(value = "SELECT s.start_date "
            + "FROM schedules s "
            + "INNER JOIN schedules_venues sv ON s.id = sv.schedule_id "
            + "JOIN schedule_times st ON s.id = st.schedule_id "
            + "JOIN venues v2 ON sv.venue_id = v2.id "
            + "JOIN units u ON v2.unit_id = u.id "
            + "WHERE (:unit IS NULL OR :unit = '' OR v2.venue = :unit)"
            + "AND s.start_date >= :startDate "
            + "AND s.end_date <= :endDate "
            + "GROUP BY s.start_date "
            + "HAVING SUM(CASE WHEN s.status_payment = 'Paid' THEN 1 ELSE 0 END) > 0", nativeQuery = true)
        List<String> findScheduleWithPaidStatus(@Param("unit") String unit,
            @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        @Query(value = "SELECT COUNT(s.start_date) " +
                "FROM schedules s " +
                "INNER JOIN schedules_venues sv ON s.id = sv.schedule_id " +
                "JOIN schedule_times st ON s.id = st.schedule_id " +
                "JOIN venues v2 ON sv.venue_id = v2.id " +
                "WHERE s.category = 'Sewa Lahan' " +
                "AND s.status_payment = 'Paid' " +
                "AND s.status_booking = 'Processing' " +
                "AND (:unit IS NULL OR :unit = '' OR v2.venue = :unit) " +
                "AND s.start_date >= :startDate " +
                "AND s.end_date <= :endDate " +  // Added space before this line
                "GROUP BY s.start_date", nativeQuery = true)
        Long getOverallPercentage(@Param("unit") String unit,
                                  @Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate);

        @Query(name = "getSewaLahanCardData", nativeQuery = true)
        List<CardSewaLahanDTO> getSewaLahanCardData(@Param("unit") String unit,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);
}
