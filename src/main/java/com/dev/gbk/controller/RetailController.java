
package com.dev.gbk.controller;

import com.dev.gbk.dto.RetailRequest;
import org.springframework.web.bind.annotation.RestController;

import com.dev.gbk.model.Retail;
import com.dev.gbk.service.RetailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.data.domain.Page;
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
@RequestMapping("/api/retails")
@SecurityRequirement(name = "bearerAuth")
public class RetailController {

    private final RetailService retailService;

    public RetailController(RetailService retailService) {
        this.retailService = retailService;
    }

    @PreAuthorize("hasAuthority('VIEW_DATA_RETAIL')")
    @GetMapping
    public Page<Retail> findAll(@RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return ResponseEntity.ok(retailService.findAll(search, page, size)).getBody();
    }

    @PreAuthorize("hasAuthority('CREATE_DATA_RETAIL')")
    @PostMapping
    public String store(@RequestBody RetailRequest retailRequest) {
        retailService.save(retailRequest);
        return new ResponseEntity<>(HttpStatus.CREATED).toString();
    }

    @PreAuthorize("hasAuthority('VIEW_DATA_RETAIL')")
    @GetMapping("/{id}")
    public ResponseEntity<Retail> findById(@PathVariable Long id) {
        return new ResponseEntity<>(retailService.findById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('UPDATE_DATA_RETAIL')")
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id, @RequestBody RetailRequest retailRequest) {
        retailService.update(id, retailRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DELETE_DATA_RETAIL')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        retailService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}