package com.dev.gbk.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dev.gbk.dto.ScheduleRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dev.gbk.exception.ResourceNotFoundException;
import com.dev.gbk.model.Schedule;
import com.dev.gbk.model.Venue;
import com.dev.gbk.payloads.ListScheduleGbk;
import com.dev.gbk.repository.ScheduleRepository;
import com.dev.gbk.repository.VenueRepository;
import com.dev.gbk.spesification.SpecificationBuilderImpl;
import com.dev.gbk.utils.Utils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final VenueRepository venueRepository;

    // logger
    private final Logger log = LoggerFactory.getLogger(ScheduleService.class);
    private final SpecificationBuilderImpl<Schedule> specificationBuilder = new SpecificationBuilderImpl<>(
            new ObjectMapper(), Schedule.class);

    public ScheduleService(
            ScheduleRepository scheduleRepository, VenueRepository venueRepository) {
        this.scheduleRepository = scheduleRepository;
        this.venueRepository = venueRepository;
    }

    // public List<TimeSlot> getAvailableTimeSlots(LocalDate startDate, LocalDate
    // endDate) {
    // // List to store time slots
    // List<TimeSlot> timeSlots = new ArrayList<>();

    // // Generate time slots for each day within the date range
    // LocalDate currentDate = startDate;

    // while (!currentDate.isAfter(endDate)) {
    // // Generate time slots from 6 AM to 10 PM
    // for (int hour = 6; hour < 22; hour += 2) {
    // try {
    // LocalTime fromTime = LocalTime.of(hour, 0);
    // LocalTime toTime = LocalTime.of(hour + 2, 0);

    // // Create TimeSlot object
    // TimeSlot slot = new TimeSlot(fromTime, toTime, "Available");
    // timeSlots.add(slot);
    // } catch (Exception e) {
    // log.error(e.getMessage());
    // }
    // }

    // // Add one day
    // currentDate = currentDate.plusDays(1);
    // }

    // if (timeSlots.isEmpty()) {
    // return timeSlots;
    // }

    // // Get booked schedules within the date range
    // List<Schedule> bookedSchedules =
    // scheduleRepository.findByScheduleDateBetween(startDate, endDate);

    // // Check availability for each time slot
    // for (Schedule schedule : bookedSchedules) {
    // for (TimeSlot slot : timeSlots) {
    // if ((slot.getFromTime().equals(schedule.getScheduleTimeFrom()) ||
    // slot.getFromTime().isAfter(schedule.getScheduleTimeFrom())) &&
    // (slot.getToTime().equals(schedule.getScheduleTimeTo()) ||
    // slot.getToTime().isBefore(schedule.getScheduleTimeTo()))) {
    // slot.setStatus("Booked");
    // }
    // }
    // }

    // return timeSlots;
    // }

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
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt").and(Sort.by(Sort.Direction.DESC, "createdAt"));
        Optional<Specification<Schedule>> specification = specificationBuilder.parseAndBuild(search);
        return specification.map(scheduleSpecification -> scheduleRepository.findAll(scheduleSpecification, sort))
                .orElseGet(() -> scheduleRepository.findAll(sort));
    }

    public List<Schedule> findPendingSchedulesCreatedBefore(Long venue) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (venue != null) {
            return scheduleRepository.findByStatusBookingAndVenuesIdAndCreatedAtBefore("Soft Booking", venue,
                    currentDateTime.minusDays(3));
        } else {
            return scheduleRepository.findByStatusBookingAndCreatedAtBefore("Soft Booking",
                    currentDateTime.minusDays(3));
        }
    }

    public Schedule findById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
    }

    public Schedule store(ScheduleRequest scheduleRequest) {
        // get all venue in schedulerequest by venue id
        List<Venue> venues = new ArrayList<>();
        for (Long id : scheduleRequest.getVenueID()) {
            Venue venue = venueRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
            venues.add(venue);
        }

        // get last id from schedule
        Schedule latestSchedule = scheduleRepository.findTopByOrderByIdDesc();
        Long lastId = latestSchedule == null ? 0L : latestSchedule.getId();

        Schedule schedule = Schedule.builder()
                .bookingNumber(Utils.generateBookingNumber(lastId, scheduleRequest.getStatusBooking()))
                .type(scheduleRequest.getType())
                .profileEvent(scheduleRequest.getProfileEvent())
                .descriptionEvent(scheduleRequest.getDescriptionEvent())
                .games(scheduleRequest.getGames())
                .category(scheduleRequest.getCategory())
                .session(scheduleRequest.getSession())
                .statusBooking(scheduleRequest.getStatusBooking())
                .statusPayment(scheduleRequest.getStatusPayment())
                .totalPaid(scheduleRequest.getTotalPaid())
                .totalSF(scheduleRequest.getTotalSF())
                .customerName(scheduleRequest.getCustomerName())
                .customerEmail(scheduleRequest.getCustomerEmail())
                .customerPhone(scheduleRequest.getCustomerPhone())
                .venues(venues)
                .build();
        if (scheduleRequest.getScheduleStartDate() != null && scheduleRequest.getScheduleEndDate() != null) {
            schedule.setScheduleStartDate(Utils.convertStringToLocalDate(scheduleRequest.getScheduleStartDate()));
            schedule.setScheduleEndDate(Utils.convertStringToLocalDate(scheduleRequest.getScheduleEndDate()));
        }

        if (scheduleRequest.getScheduleStartInLoad() != null && scheduleRequest.getScheduleEndInLoad() != null) {
            schedule.setScheduleStartInLoad(Utils.convertStringToLocalDate(scheduleRequest.getScheduleStartInLoad()));
            schedule.setScheduleEndInLoad(Utils.convertStringToLocalDate(scheduleRequest.getScheduleEndInLoad()));
        }

        if (scheduleRequest.getScheduleStartInLoad() != null && scheduleRequest.getScheduleEndInLoad() != null) {
            schedule.setScheduleStartInLoad(Utils.convertStringToLocalDate(scheduleRequest.getScheduleStartInLoad()));
            schedule.setScheduleEndInLoad(Utils.convertStringToLocalDate(scheduleRequest.getScheduleEndInLoad()));
        }

        return scheduleRepository.save(schedule);
    }

    public Schedule update(Long id, ScheduleRequest scheduleRequest) {
        Schedule schedule = findById(id);

        // get all venue in schedulerequest by venue id
        List<Venue> venues = new ArrayList<>();
        for (Long id2 : scheduleRequest.getVenueID()) {
            Venue venue = venueRepository.findById(id2)
                    .orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
            venues.add(venue);
        }

        schedule.setBookingNumber(scheduleRequest.getBookingNumber());
        schedule.setType(scheduleRequest.getType());
        schedule.setProfileEvent(scheduleRequest.getProfileEvent());
        schedule.setDescriptionEvent(scheduleRequest.getDescriptionEvent());
        schedule.setGames(scheduleRequest.getGames());
        schedule.setCategory(scheduleRequest.getCategory());
        schedule.setSession(scheduleRequest.getSession());
        schedule.setStatusBooking(scheduleRequest.getStatusBooking());
        schedule.setStatusPayment(scheduleRequest.getStatusPayment());
        schedule.setTotalPaid(scheduleRequest.getTotalPaid());
        schedule.setTotalSF(scheduleRequest.getTotalSF());
        schedule.setCustomerName(scheduleRequest.getCustomerName());
        schedule.setCustomerEmail(scheduleRequest.getCustomerEmail());
        schedule.setCustomerPhone(scheduleRequest.getCustomerPhone());
        schedule.setVenues(venues);

        if (scheduleRequest.getScheduleStartDate() != null && scheduleRequest.getScheduleEndDate() != null) {
            schedule.setScheduleStartDate(Utils.convertStringToLocalDate(scheduleRequest.getScheduleStartDate()));
            schedule.setScheduleEndDate(Utils.convertStringToLocalDate(scheduleRequest.getScheduleEndDate()));
        }

        if (scheduleRequest.getScheduleStartInLoad() != null && scheduleRequest.getScheduleEndInLoad() != null) {
            schedule.setScheduleStartInLoad(Utils.convertStringToLocalDate(scheduleRequest.getScheduleStartInLoad()));
            schedule.setScheduleEndInLoad(Utils.convertStringToLocalDate(scheduleRequest.getScheduleEndInLoad()));
        }

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
            List<Long> venues = new ArrayList<>();
            venues.add(venue.getId());
            scheduleRequest.setVenueID(venues);
            store(scheduleRequest);
        }
    }

}
