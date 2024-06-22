package com.dev.gbk.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.gbk.dto.OccupancyDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {
    @GetMapping("/occupancies")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DASHBOARD')")
    public ResponseEntity<List<OccupancyDTO>> occupancies() {
        List<OccupancyDTO> occupancyList = Arrays.asList(
                new OccupancyDTO("Occ Fisik", "60.4"),
                new OccupancyDTO("Occ PKBLU", "40.2"),
                new OccupancyDTO("Occ Retail", "80.4"),
                new OccupancyDTO("Occ Maintenance", "55.2"),
                new OccupancyDTO("Occ Fisik vs Pendapatan", "80.4"),
                new OccupancyDTO("Occ Timnas", "40.4"));

        return ResponseEntity.ok(occupancyList);
    }

    @GetMapping("/events")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_DASHBOARD')")
    public ResponseEntity<List<OccupancyDTO>> events() {
        List<OccupancyDTO> events = Arrays.asList(
                new OccupancyDTO("Keagamaan", "10.0"),
                new OccupancyDTO("Pendidikan", "10.0"),
                new OccupancyDTO("Sewa Lahan", "10.0"),
                new OccupancyDTO("Kenegaraan", "10.0"),
                new OccupancyDTO("Expo", "10.0"),
                new OccupancyDTO("Exhibition", "10.0"),
                new OccupancyDTO("Internal Company", "10.0"),
                new OccupancyDTO("Lain-Lain", "10.0"),
                new OccupancyDTO("Olahraga", "10.0"),
                new OccupancyDTO("Konser", "10.0"));

        return ResponseEntity.ok(events);
    }

    @GetMapping("/event-classification")
    public ResponseEntity<List<OccupancyDTO>> eventClassification() {
        List<OccupancyDTO> occupancyList = Arrays.asList(
                new OccupancyDTO("Pemerintah", "10.0"),
                new OccupancyDTO("Lain-lain", "40.0"),
                new OccupancyDTO("Nasional", "30.0"),
                new OccupancyDTO("Internasional", "20.0"));
        return ResponseEntity.ok(occupancyList);
    }

}
