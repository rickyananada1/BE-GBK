package com.dev.gbk.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.gbk.dto.UnitRequest;
import com.dev.gbk.service.UnitService;
import com.dev.gbk.utils.ResponseHandler;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/units")
@SecurityRequirement(name = "bearerAuth")
public class UnitController {
    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        if (page == null && size == null) {
            return ResponseHandler.generateResponse("Success get all retails", HttpStatus.OK,
                    unitService.findAll(search));
        }

        if (page == null)
            page = 0;
        if (size == null)
            size = 10;
        return ResponseHandler.generateResponse("Success get all retails", HttpStatus.OK,
                unitService.findAll(search, page, size));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody UnitRequest unitRequest) {
        return ResponseHandler.generateResponse("Success create unit", HttpStatus.OK,
                unitService.save(unitRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody UnitRequest unitRequest) {
        return ResponseHandler.generateResponse("Success update unit", HttpStatus.OK,
                unitService.update(id, unitRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        return ResponseHandler.generateResponse("Success get unit by id", HttpStatus.OK,
                unitService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        unitService.delete(id);
        return ResponseHandler.generateResponse("Success delete unit", HttpStatus.OK, null);
    }
}
