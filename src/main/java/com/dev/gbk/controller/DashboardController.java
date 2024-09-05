package com.dev.gbk.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dev.gbk.response.Occupancy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.gbk.dto.CardEventDTO;
import com.dev.gbk.dto.CardGamesDTO;
import com.dev.gbk.dto.CardRetailDTO;
import com.dev.gbk.dto.IncomeDTO;
import com.dev.gbk.service.DashboardService;
import com.dev.gbk.utils.ResponseHandler;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/usage-by-category")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DASHBOARD')")
    public ResponseEntity<Object> getUsageByCategory(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "unit", required = false) String unitName) {

        LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : LocalDate.now().withDayOfYear(1);
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : LocalDate.now();

        if (start != null && end != null && end.isBefore(start)) {
            LocalDate temp = start;
            start = end;
            end = temp;
        }

        Map<String, BigDecimal> result = dashboardService.getUsageByCategory(start, end, unitName);
        return ResponseHandler.generateResponse("Success get usage by category",
                HttpStatus.OK, result);
    }

    @GetMapping("/usage-by-profile-event")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DASHBOARD')")
    public ResponseEntity<Object> getUsageByProfileEvent(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "unit", required = false) String unitName) {

        LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : LocalDate.now().withDayOfYear(1);
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : LocalDate.now();

        if (start != null && end != null && end.isBefore(start)) {
            LocalDate temp = start;
            start = end;
            end = temp;
        }

        Map<String, BigDecimal> result = dashboardService.getUsageByProfileEvent(start, end, unitName);
        return ResponseHandler.generateResponse("Success get usage by profile event",
                HttpStatus.OK, result);
    }

    @GetMapping("/total-paid-profile-event")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DASHBOARD')")
    public ResponseEntity<Object> getTotalPaidForProfileEvent(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam(value = "unitName", required = false) String unitName) {

        LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : LocalDate.now().withDayOfYear(1);
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : LocalDate.now();

        if (start != null && end != null && end.isBefore(start)) {
            LocalDate temp = start;
            start = end;
            end = temp;
        }
        Map<String, Integer> totalPaidByProfileEvent = dashboardService.getTotalPaidGroupedByProfileEvent(start, end,
                unitName);
        return ResponseHandler.generateResponse("Data fetched successfully", HttpStatus.OK, totalPaidByProfileEvent);
    }

    @GetMapping("/total-paid-games")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DASHBOARD')")
    public ResponseEntity<Object> getTotalPaidForGames(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam(value = "unitName", required = false) String unitName) {

        LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : LocalDate.now().withDayOfYear(1);
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : LocalDate.now();

        if (start != null && end != null && end.isBefore(start)) {
            LocalDate temp = start;
            start = end;
            end = temp;
        }
        Map<String, Integer> totalPaidByGames = dashboardService.getTotalPaidGroupedByGames(start, end,
                unitName);
        return ResponseHandler.generateResponse("Data fetched successfully", HttpStatus.OK, totalPaidByGames);
    }

    @GetMapping("/income")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DASHBOARD')")
    public IncomeDTO getIncome(@RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "unitName", required = false) String unitName) {

        LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : LocalDate.now().withDayOfYear(1);
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : LocalDate.now();

        if (start != null && end != null && end.isBefore(start)) {
            LocalDate temp = start;
            start = end;
            end = temp;
        }

        return dashboardService.getTotalIncome(start, end, unitName);
    }

    @GetMapping("/projection/income")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DASHBOARD')")
    public Map<String, Integer> getProjectionIncome(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "type", required = true) String type,
            @RequestParam(value = "unitName", required = false) String unitName) {
        LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : LocalDate.now().withDayOfYear(1);
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : LocalDate.now();

        if (end.isBefore(start)) {
            LocalDate temp = start;
            start = end;
            end = temp;
        }

        if ("profileEvent".equalsIgnoreCase(type)) {
            return dashboardService.getProjectionTotalPaidGroupedByProfileEvent(start, end, unitName);
        } else if ("games".equalsIgnoreCase(type)) {
            return dashboardService.getProjectionTotalPaidGroupedByGames(start, end, unitName);
        } else {
            throw new IllegalArgumentException("Invalid type parameter. Use 'profileEvent' or 'games'.");
        }
    }

    @GetMapping("/get-occupancy")
    public ResponseEntity<Occupancy> getOccupancy(
        @RequestParam(value = "venue", required = false, defaultValue = "ALL") String venue,
        @RequestParam(value = "startDate", required = false) String startDate,
        @RequestParam(value = "endDate", required = false) String endDate) {

        LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : LocalDate.now().withDayOfYear(1);
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : LocalDate.now();

        if (start != null && end != null && end.isBefore(start)) {
            LocalDate temp = start;
            start = end;
            end = temp;
        }
        if ("ALL".equals(venue)) {
            return ResponseEntity.ok(new Occupancy());
        }
        Occupancy occupancy = dashboardService.getOccupancy(start, end, venue);
        return ResponseEntity.ok(occupancy);
    }

    @GetMapping("/type-total")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DASHBOARD')")
    public ResponseEntity<Object> getTotalByType(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "unitName", required = false) String unitName) {

        LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : LocalDate.now().withDayOfYear(1);
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : LocalDate.now();

        if (start != null && end != null && end.isBefore(start)) {
            LocalDate temp = start;
            start = end;
            end = temp;
        }
        Map<String, Integer> total = dashboardService.getTotalPaidGroupedByProfileEvent(start, end, unitName);

        return ResponseEntity.ok(total);
    }

    @GetMapping("/game-total")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DASHBOARD')")
    public ResponseEntity<Object> getTotalByGame(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "unitName", required = false) String unitName) {

        LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : LocalDate.now().withDayOfYear(1);
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : LocalDate.now();

        if (start != null && end != null && end.isBefore(start)) {
            LocalDate temp = start;
            start = end;
            end = temp;
        }

        Map<String, Integer> total = dashboardService.getTotalPaidGroupedByGames(start, end, unitName);

        return ResponseEntity.ok(total);
    }

    @GetMapping("/games-card")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DASHBOARD')")
    public ResponseEntity<Object> getGamesCardData(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "unit", required = false) String unit) {

        LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : LocalDate.now().withDayOfYear(1);
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : LocalDate.now();

        if (start != null && end != null && end.isBefore(start)) {
            LocalDate temp = start;
            start = end;
            end = temp;
        }

        List<CardGamesDTO> gamesCardData = dashboardService.getGamesCardData(start, end, unit);
        return ResponseHandler.generateResponse("Success get games card data",
                HttpStatus.OK, gamesCardData);
    }

    @GetMapping("/event-card")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DASHBOARD')")
    public ResponseEntity<Object> getEventCardData(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "unit", required = false) String unit) {

        LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : LocalDate.now().withDayOfYear(1);
        LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : LocalDate.now();

        if (start != null && end != null && end.isBefore(start)) {
            LocalDate temp = start;
            start = end;
            end = temp;
        }

        List<CardEventDTO> eventCardData = dashboardService.getEventCardData(start, end, unit);
        return ResponseHandler.generateResponse("Success get event card data",
                HttpStatus.OK, eventCardData);
    }

    @GetMapping("/retail-card")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DASHBOARD')")
    public ResponseEntity<Map<String, Object>> getRetailCardData(@RequestParam(value = "unit", required = false) String unit) {
        Map<String, Object> result = dashboardService.getRetailCardData(unit);
        return ResponseEntity.ok().body(result);
    }

}
