
package com.dev.gbk.controller;

import com.dev.gbk.dto.VenueRequest;

import org.springframework.web.bind.annotation.RestController;

import com.dev.gbk.service.VenueService;
import com.dev.gbk.utils.ResponseHandler;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/venues")
@SecurityRequirement(name = "bearerAuth")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @PreAuthorize("hasAuthority('VIEW_DATA_VENUE')")
    @GetMapping
    public ResponseEntity<Object> findAll(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return ResponseHandler.generateResponse("Success get all venues", HttpStatus.OK,
                venueService.findAll(search, page, size));
    }

    @PreAuthorize("hasAuthority('CREATE_DATA_VENUE')")
    @PostMapping
    public ResponseEntity<Object> store(@RequestBody VenueRequest venueRequest) {
        return ResponseHandler.generateResponse("Success create venue", HttpStatus.CREATED,
                venueService.save(venueRequest));
    }

    @PreAuthorize("hasAuthority('VIEW_DATA_VENUE')")
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        return ResponseHandler.generateResponse("Success get venue by id", HttpStatus.OK,
                venueService.findById(id));
    }

    @PreAuthorize("hasAuthority('UPDATE_DATA_VENUE')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody VenueRequest venueRequest) {
        return ResponseHandler.generateResponse("Success update venue", HttpStatus.OK,
                venueService.update(id, venueRequest));
    }

    @PreAuthorize("hasAuthority('DELETE_DATA_VENUE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        venueService.deleteById(id);
        return ResponseHandler.generateResponse("Success delete venue", HttpStatus.OK, null);
    }
}