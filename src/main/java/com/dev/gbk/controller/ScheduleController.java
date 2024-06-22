package com.dev.gbk.controller;

import com.dev.gbk.dto.ScheduleRequest;
import com.dev.gbk.model.Schedule;
import com.dev.gbk.model.TimeSlot;
import com.dev.gbk.model.Venue;
import com.dev.gbk.service.ScheduleService;
import com.dev.gbk.service.VenueService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@SecurityRequirement(name = "bearerAuth")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final VenueService venueService;

    public ScheduleController(ScheduleService scheduleService, VenueService venueService) {
        this.scheduleService = scheduleService;
        this.venueService = venueService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DATA_SCHEDULE')")
    public ResponseEntity<Page<Schedule>> findAll(@RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return ResponseEntity.ok(scheduleService.findAll(search, page, size));
    }

    @GetMapping("/available")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DATA_SCHEDULE')")
    public ResponseEntity<List<TimeSlot>> getAvailableTimeSlots(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        List<TimeSlot> availableTimeSlots = scheduleService.getAvailableTimeSlots(startDate, endDate);
        return ResponseEntity.ok(availableTimeSlots);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DATA_SCHEDULE')")
    public ResponseEntity<Schedule> findById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CREATE_SCHEDULE')")
    public ResponseEntity<HttpStatus> store(@RequestBody ScheduleRequest scheduleRequest) {
        Venue venue = venueService.findById(scheduleRequest.getVenueId());
        scheduleService.store(scheduleRequest, venue);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('UPDATE_SCHEDULE')")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id, @RequestBody ScheduleRequest scheduleRequest) {
        Venue venue = venueService.findById(scheduleRequest.getVenueId());
        scheduleService.update(id, scheduleRequest, venue);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('DELETE_SCHEDULE')")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.ok().build();
    }
}