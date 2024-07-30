package com.dev.gbk.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.dev.gbk.dto.MasterRetailRequest;
import com.dev.gbk.service.MasterRetailService;
import com.dev.gbk.utils.ResponseHandler;

public class MasterRetailController {
    private final MasterRetailService masterRetailService;

    public MasterRetailController(MasterRetailService masterRetailService) {
        this.masterRetailService = masterRetailService;
    }

    @PreAuthorize("hasAuthority('VIEW_DATA_RETAIL')")
    @GetMapping
    public ResponseEntity<Object> findAll(@RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return ResponseHandler.generateResponse("Success get all retails", HttpStatus.OK,
                masterRetailService.findAll(search, page, size));
    }

    @PreAuthorize("hasAuthority('CREATE_DATA_RETAIL')")
    @PostMapping
    public ResponseEntity<Object> store(@RequestBody MasterRetailRequest masterRetailRequest) {
        return ResponseHandler.generateResponse("Success create retail", HttpStatus.CREATED,
                masterRetailService.save(masterRetailRequest));
    }

    @PreAuthorize("hasAuthority('VIEW_DATA_RETAIL')")
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        return ResponseHandler.generateResponse("Success get retail by id", HttpStatus.OK,
                masterRetailService.findById(id));
    }

    @PreAuthorize("hasAuthority('UPDATE_DATA_RETAIL')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody MasterRetailRequest masterRetailRequest) {
        return ResponseHandler.generateResponse("Success update retail", HttpStatus.OK,
                masterRetailService.update(id, masterRetailRequest));
    }

    @PreAuthorize("hasAuthority('DELETE_DATA_RETAIL')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        masterRetailService.deleteById(id);
        return ResponseHandler.generateResponse("Success delete retail", HttpStatus.OK,
                null);
    }
}
