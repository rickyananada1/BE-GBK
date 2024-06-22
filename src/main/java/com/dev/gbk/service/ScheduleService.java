package com.dev.gbk.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.dev.gbk.dto.ScheduleRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dev.gbk.exception.ResourceNotFoundException;
import com.dev.gbk.model.Schedule;
import com.dev.gbk.model.TimeSlot;
import com.dev.gbk.model.Venue;
import com.dev.gbk.repository.ScheduleRepository;
import com.dev.gbk.spesification.SpecificationBuilderImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ScheduleService {

    private ScheduleRepository scheduleRepository;

    private final SpecificationBuilderImpl<Schedule> specificationBuilder = new SpecificationBuilderImpl<>(
            new ObjectMapper(), Schedule.class);

    public ScheduleService(
            ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<TimeSlot> getAvailableTimeSlots(Date startDate, Date endDate) {
        // Format waktu dan tanggal untuk perbandingan menjadi localDateTime
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localStartDate = LocalDateTime.parse(dateTimeFormat.format(startDate));
        if (endDate == null) {
            endDate = new Date();
        }
        LocalDateTime localEndDate = LocalDateTime.parse(dateTimeFormat.format(endDate));
        // List untuk menyimpan slot waktu
        List<TimeSlot> timeSlots = new ArrayList<>();

        // Mendapatkan slot waktu untuk setiap hari dalam rentang tanggal
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!calendar.getTime().after(endDate)) {
            Date currentDate = calendar.getTime();

            // Mendapatkan slot waktu dari jam 6 pagi sampai 10 malam
            for (int hour = 6; hour < 22; hour += 2) {
                try {
                    Calendar fromTimeCal = Calendar.getInstance();
                    fromTimeCal.setTime(currentDate);
                    fromTimeCal.set(Calendar.HOUR_OF_DAY, hour);
                    fromTimeCal.set(Calendar.MINUTE, 0);

                    Calendar toTimeCal = Calendar.getInstance();
                    toTimeCal.setTime(currentDate);
                    toTimeCal.set(Calendar.HOUR_OF_DAY, hour + 2);
                    toTimeCal.set(Calendar.MINUTE, 0);

                    Date slotStartDate = fromTimeCal.getTime();
                    Date slotEndDate = toTimeCal.getTime();

                    // Membuat objek TimeSlot
                    TimeSlot slot = new TimeSlot(slotStartDate, slotEndDate, "Available");
                    timeSlots.add(slot);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Menambah 1 hari
            calendar.add(Calendar.DATE, 1);
        }

        if (timeSlots.isEmpty()) {
            return timeSlots;
        }
        // Mendapatkan schedule yang sudah terbooking dalam rentang tanggal tersebut
        List<Schedule> bookedSchedules = endDate == null ? scheduleRepository.findByScheduleDate(localStartDate)
                : scheduleRepository.findByScheduleDateBetween(
                        localStartDate, localEndDate);

        // Cek ketersediaan untuk setiap slot waktu
        for (Schedule schedule : bookedSchedules) {
            for (TimeSlot slot : timeSlots) {
                if ((slot.getFromTime().after(schedule.getScheduleDateFrom())
                        || slot.getFromTime().equals(schedule.getScheduleDateFrom())) &&
                        (slot.getToTime().before(schedule.getScheduleDateTo())
                                || slot.getToTime().equals(schedule.getScheduleDateTo()))) {
                    slot.setStatus("Booked");
                }
            }
        }

        return timeSlots;
    }

    public Page<Schedule> findAll(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Optional<Specification<Schedule>> specification = specificationBuilder.parseAndBuild(search);
        return specification.isPresent() ? scheduleRepository.findAll(specification.get(), pageable)
                : scheduleRepository.findAll(pageable);
    }

    public Schedule findById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
    }

    public void store(ScheduleRequest scheduleRequest, Venue venue) {
        Schedule schedule = Schedule
                .builder().type(scheduleRequest.getType())
                .profileEvent(scheduleRequest.getProfileEvent())
                .descriptionEvent(scheduleRequest.getDescriptionEvent())
                .games(scheduleRequest.getGames())
                .category(scheduleRequest.getCategory())
                .scheduleDate(scheduleRequest.getScheduleDate())
                .scheduleDateFrom(scheduleRequest.getScheduleDateFrom())
                .scheduleDateTo(scheduleRequest.getScheduleDateTo())
                .session(scheduleRequest.getSession())
                .status(scheduleRequest.getStatus())
                .total(scheduleRequest.getTotal())
                .customerName(scheduleRequest.getCustomerName())
                .customerEmail(scheduleRequest.getCustomerEmail())
                .customerPhone(scheduleRequest.getCustomerPhone())
                .venue(venue)
                .build();

        scheduleRepository.save(schedule);
    }

    public void update(Long id, ScheduleRequest scheduleRequest, Venue venue) {
        Schedule schedule = findById(id);
        schedule.setType(scheduleRequest.getType());
        schedule.setProfileEvent(scheduleRequest.getProfileEvent());
        schedule.setDescriptionEvent(scheduleRequest.getDescriptionEvent());
        schedule.setGames(scheduleRequest.getGames());
        schedule.setCategory(scheduleRequest.getCategory());
        schedule.setScheduleDate(scheduleRequest.getScheduleDate());
        schedule.setScheduleDateFrom(scheduleRequest.getScheduleDateFrom());
        schedule.setScheduleDateTo(scheduleRequest.getScheduleDateTo());
        schedule.setSession(scheduleRequest.getSession());
        schedule.setStatus(scheduleRequest.getStatus());
        schedule.setTotal(scheduleRequest.getTotal());
        schedule.setCustomerName(scheduleRequest.getCustomerName());
        schedule.setCustomerEmail(scheduleRequest.getCustomerEmail());
        schedule.setCustomerPhone(scheduleRequest.getCustomerPhone());
        schedule.setVenue(venue);
        scheduleRepository.save(schedule);
    }

    public void delete(Long id) {
        findById(id);
        scheduleRepository.deleteById(id);
    }
}
