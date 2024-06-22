package com.dev.gbk.controller;

import com.dev.gbk.dto.ScheduleRequest;
import com.dev.gbk.model.TimeSlot;
import com.dev.gbk.model.Venue;
import com.dev.gbk.service.ScheduleService;
import com.dev.gbk.service.VenueService;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final VenueService venueService;

    public ScheduleController(ScheduleService scheduleService, VenueService venueService) {
        this.scheduleService = scheduleService;
        this.venueService = venueService;
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return ResponseEntity.ok(scheduleService.findAll(search, page, size));
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableTimeSlots(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        List<TimeSlot> availableTimeSlots = scheduleService.getAvailableTimeSlots(startDate, endDate);
        return ResponseEntity.ok(availableTimeSlots);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> store(@RequestBody ScheduleRequest scheduleRequest) {
        Venue venue = venueService.findById(scheduleRequest.getVenueId());
        scheduleService.store(scheduleRequest, venue);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ScheduleRequest scheduleRequest) {
        Venue venue = venueService.findById(scheduleRequest.getVenueId());
        scheduleService.update(id, scheduleRequest, venue);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.ok().build();
    }
}