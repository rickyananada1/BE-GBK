
package com.dev.gbk.controller;

import com.dev.gbk.dto.VenueRequest;

import org.springframework.web.bind.annotation.RestController;

import com.dev.gbk.model.Venue;
import com.dev.gbk.service.VenueService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping
    public List<Venue> findAll() {
        return venueService.findAll();
    }

    @PostMapping("/post")
    public String store(@RequestBody VenueRequest venueRequest) {
        venueService.save(venueRequest);
        return new ResponseEntity<>(HttpStatus.CREATED).toString();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venue> findById(@PathVariable Long id) {
        return new ResponseEntity<>(venueService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id, @RequestBody VenueRequest venueRequest) {
        venueService.update(id, venueRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        venueService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}