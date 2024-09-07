
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "unit", required = false) String unitString) {
        List<Integer> unitList = convertUnitStringToList(unitString);
        if (page == null && size == null) {
            return ResponseHandler.generateResponse("Success get all venues", HttpStatus.OK,
                    venueService.findAll(search,type,unitList));
        }

        if (page == null)
            page = 0;
        if (size == null)
            size = 10;
        return ResponseHandler.generateResponse("Success get all venues", HttpStatus.OK,
                venueService.findAll(search, page, size,type,unitList));
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

    private List<Integer> convertUnitStringToList(String unitString) {
        List<Integer> unitList = new ArrayList<>();
        if (unitString != null && !unitString.isEmpty()) {
            String[] units = unitString.split(",");
            for (String unit : units) {
                try {
                    unitList.add(Integer.parseInt(unit.trim()));
                } catch (NumberFormatException e) {
                    System.err.println("Invalid unit value: " + unit);
                }
            }
        }
        return unitList;
    }

}