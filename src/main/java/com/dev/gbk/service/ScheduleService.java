package com.dev.gbk.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dev.gbk.dto.ScheduleRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dev.gbk.exception.ResourceNotFoundException;
import com.dev.gbk.model.Schedule;
import com.dev.gbk.model.TimeSlot;
import com.dev.gbk.model.Venue;
import com.dev.gbk.payloads.ListScheduleGbk;
import com.dev.gbk.repository.ScheduleRepository;
import com.dev.gbk.spesification.SpecificationBuilderImpl;
import com.dev.gbk.utils.Utils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    // logger
    private final Logger log = LoggerFactory.getLogger(ScheduleService.class);
    private final SpecificationBuilderImpl<Schedule> specificationBuilder = new SpecificationBuilderImpl<>(
            new ObjectMapper(), Schedule.class);

    public ScheduleService(
            ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<TimeSlot> getAvailableTimeSlots(LocalDate startDate, LocalDate endDate) {
        // List to store time slots
        List<TimeSlot> timeSlots = new ArrayList<>();

        // Generate time slots for each day within the date range
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            // Generate time slots from 6 AM to 10 PM
            for (int hour = 6; hour < 22; hour += 2) {
                try {
                    LocalTime fromTime = LocalTime.of(hour, 0);
                    LocalTime toTime = LocalTime.of(hour + 2, 0);

                    // Create TimeSlot object
                    TimeSlot slot = new TimeSlot(fromTime, toTime, "Available");
                    timeSlots.add(slot);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }

            // Add one day
            currentDate = currentDate.plusDays(1);
        }

        if (timeSlots.isEmpty()) {
            return timeSlots;
        }

        // Get booked schedules within the date range
        List<Schedule> bookedSchedules = scheduleRepository.findByScheduleDateBetween(startDate, endDate);

        // Check availability for each time slot
        for (Schedule schedule : bookedSchedules) {
            for (TimeSlot slot : timeSlots) {
                if ((slot.getFromTime().equals(schedule.getScheduleTimeFrom()) ||
                        slot.getFromTime().isAfter(schedule.getScheduleTimeFrom())) &&
                        (slot.getToTime().equals(schedule.getScheduleTimeTo()) ||
                                slot.getToTime().isBefore(schedule.getScheduleTimeTo()))) {
                    slot.setStatus("Booked");
                }
            }
        }

        return timeSlots;
    }

    public Page<Schedule> findAll(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Optional<Specification<Schedule>> specification = specificationBuilder.parseAndBuild(search);
        return specification.map(scheduleSpecification -> scheduleRepository.findAll(scheduleSpecification, pageable))
                .orElseGet(() -> scheduleRepository.findAll(pageable));
    }

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> findAll(String search) {
        Optional<Specification<Schedule>> specification = specificationBuilder.parseAndBuild(search);
        return specification.map(scheduleSpecification -> scheduleRepository.findAll(scheduleSpecification))
                .orElseGet(() -> scheduleRepository.findAll());
    }

    public List<Schedule> findPendingSchedulesCreatedBefore(Long venue) {
        LocalDate currentDate = LocalDate.now();
        if (venue == null) {
            return scheduleRepository.findByStatusAndVenueIdAndCreatedAtBefore("Soft Booking", venue,
                    LocalDate.now().minusDays(3));
        } else {
            return scheduleRepository.findByStatusAndCreatedAtBefore("Soft Booking", currentDate.minusDays(3));
        }
    }

    public Schedule findById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
    }

    public Schedule store(ScheduleRequest scheduleRequest, Venue venue) {
        Schedule schedule = Schedule.builder()
                .bookingNumber(scheduleRequest.getBookingNumber())
                .type(scheduleRequest.getType())
                .profileEvent(scheduleRequest.getProfileEvent())
                .descriptionEvent(scheduleRequest.getDescriptionEvent())
                .games(scheduleRequest.getGames())
                .category(scheduleRequest.getCategory())
                .session(scheduleRequest.getSession())
                .status(scheduleRequest.getStatus())
                .total(scheduleRequest.getTotal())
                .customerName(scheduleRequest.getCustomerName())
                .customerEmail(scheduleRequest.getCustomerEmail())
                .customerPhone(scheduleRequest.getCustomerPhone())
                .venue(venue)
                .build();
        if (scheduleRequest.getScheduleStartDate() != null && scheduleRequest.getScheduleEndDate() != null) {
            schedule.setScheduleDate(Utils.convertStringToLocalDate(scheduleRequest.getScheduleStartDate()));
            schedule.setScheduleDate(Utils.convertStringToLocalDate(scheduleRequest.getScheduleEndDate()));
        }
        if (scheduleRequest.getScheduleDate() != null) {
            schedule.setScheduleDate(Utils.convertStringToLocalDate(scheduleRequest.getScheduleStartDate()));
        }
        if (scheduleRequest.getScheduleTimeFrom() != null && scheduleRequest.getScheduleTimeTo() != null) {
            schedule.setScheduleTimeFrom(Utils.convertStringToLocalTime(scheduleRequest.getScheduleTimeFrom()));
            schedule.setScheduleTimeTo(Utils.convertStringToLocalTime(scheduleRequest.getScheduleTimeTo()));
        }
        return scheduleRepository.save(schedule);
    }

    public Schedule update(Long id, ScheduleRequest scheduleRequest, Venue venue) {
        Schedule schedule = findById(id);
        schedule.setBookingNumber(scheduleRequest.getBookingNumber());
        schedule.setType(scheduleRequest.getType());
        schedule.setProfileEvent(scheduleRequest.getProfileEvent());
        schedule.setDescriptionEvent(scheduleRequest.getDescriptionEvent());
        schedule.setGames(scheduleRequest.getGames());
        schedule.setCategory(scheduleRequest.getCategory());
        schedule.setScheduleDate(Utils.convertStringToLocalDate(scheduleRequest.getScheduleDate()));
        if (scheduleRequest.getScheduleTimeFrom() != null && scheduleRequest.getScheduleTimeTo() != null) {
            schedule.setScheduleTimeFrom(Utils.convertStringToLocalTime(scheduleRequest.getScheduleTimeFrom()));
            schedule.setScheduleTimeTo(Utils.convertStringToLocalTime(scheduleRequest.getScheduleTimeTo()));
        }
        schedule.setSession(scheduleRequest.getSession());
        schedule.setStatus(scheduleRequest.getStatus());
        schedule.setTotal(scheduleRequest.getTotal());
        schedule.setCustomerName(scheduleRequest.getCustomerName());
        schedule.setCustomerEmail(scheduleRequest.getCustomerEmail());
        schedule.setCustomerPhone(scheduleRequest.getCustomerPhone());
        schedule.setVenue(venue);
        return scheduleRepository.save(schedule);
    }

    public void delete(Long id) {
        findById(id);
        scheduleRepository.deleteById(id);
    }

    public void synchronizeSchedules(List<ListScheduleGbk> schedules, Venue venue) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        log.info("Synchronizing schedules");
        for (ListScheduleGbk schedule : schedules) {
            ScheduleRequest scheduleRequest = objectMapper.convertValue(schedule, ScheduleRequest.class);
            if (scheduleRepository.existsByScheduleDateAndScheduleTimeFromAndScheduleTimeTo(
                    Utils.convertStringToLocalDate(scheduleRequest.getScheduleDate()),
                    Utils.convertStringToLocalTime(scheduleRequest.getScheduleTimeFrom()),
                    Utils.convertStringToLocalTime(scheduleRequest.getScheduleTimeTo()))) {
                continue;
            }
            store(scheduleRequest, venue);
        }
    }

}
