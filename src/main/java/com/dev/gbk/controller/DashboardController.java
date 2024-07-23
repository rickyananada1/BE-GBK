package com.dev.gbk.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.gbk.dto.IncomeDTO;
import com.dev.gbk.dto.OccupancyDTO;
import com.dev.gbk.service.DashboardService;
import com.dev.gbk.utils.ResponseHandler;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {
    private DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/occupancies")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DASHBOARD')")
    public ResponseEntity<Object> occupancies() {
        List<OccupancyDTO> occupancyList = Arrays.asList(
                new OccupancyDTO("Occ Fisik", 60.4),
                new OccupancyDTO("Occ PKBLU", 40.2),
                new OccupancyDTO("Occ Retail", 80.4),
                new OccupancyDTO("Occ Maintenance", 55.2),
                new OccupancyDTO("Occ Fisik vs Pendapatan", 80.4),
                new OccupancyDTO("Occ Timnas", 40.4));

        return ResponseHandler.generateResponse("Success get dashboard", HttpStatus.OK, occupancyList);
    }

    @GetMapping("/events")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DASHBOARD')")
    public ResponseEntity<Object> events() {
        List<OccupancyDTO> events = Arrays.asList(
                new OccupancyDTO("Keagamaan", 10.0),
                new OccupancyDTO("Pendidikan", 10.0),
                new OccupancyDTO("Sewa Lahan", 10.0),
                new OccupancyDTO("Kenegaraan", 10.0),
                new OccupancyDTO("Expo", 10.0),
                new OccupancyDTO("Exhibition", 10.0),
                new OccupancyDTO("Internal Company", 10.0),
                new OccupancyDTO("Lain-Lain", 10.0),
                new OccupancyDTO("Olahraga", 10.0),
                new OccupancyDTO("Konser", 10.0));

        return ResponseHandler.generateResponse("Success get dashboard", HttpStatus.OK, events);
    }

    @GetMapping("/usage-by-category")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DASHBOARD')")
    public ResponseEntity<Object> getUsageByCategory(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "unit", required = false) String unit) {

        List<OccupancyDTO> usageByCategory = dashboardService.getUsageByCategory(startDate, endDate, unit);
        return ResponseHandler.generateResponse("Success get usage by category",
                HttpStatus.OK, usageByCategory);
    }

    @GetMapping("/usage-by-profile-event")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DASHBOARD')")
    public ResponseEntity<Object> getUsageByProfileEvent(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "unit", required = false) String unit) {

        List<OccupancyDTO> usageByProfileEvent = dashboardService.getUsageByProfileEvent(startDate, endDate, unit);
        return ResponseHandler.generateResponse("Success get usage by profile event",
                HttpStatus.OK,
                usageByProfileEvent);
    }

    @GetMapping("/income")
    public IncomeDTO getIncome(@RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        // get current year
        LocalDate now = LocalDate.now();
        if (startDate == null) {
            startDate = now.getYear() + "-01-01";
        }
        if (endDate == null) {
            // if month is < 10, add 0 to the beginning
            if (now.getMonthValue() < 10) {
                endDate = now.getYear() + "-0" + now.getMonthValue() + "-01";
            } else {
                endDate = now.getYear() + "-" + now.getMonthValue() + "-01";
            }
        }
        return dashboardService.getTotalIncome(startDate, endDate);
    }
}
