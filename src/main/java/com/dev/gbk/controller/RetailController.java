
package com.dev.gbk.controller;

import com.dev.gbk.dto.RetailRequest;
import org.springframework.web.bind.annotation.RestController;

import com.dev.gbk.model.Retail;
import com.dev.gbk.service.RetailService;

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
@RequestMapping("/api/retails")
@SecurityRequirement(name = "bearerAuth")
public class RetailController {

    private final RetailService retailService;

    public RetailController(RetailService retailService) {
        this.retailService = retailService;
    }

    @GetMapping
    public List<Retail> findAll() {
        return retailService.findAll();
    }

    @PostMapping("/post")
    public String store(@RequestBody RetailRequest retailRequest) {
        retailService.save(retailRequest);
        return new ResponseEntity<>(HttpStatus.CREATED).toString();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Retail> findById(@PathVariable Long id) {
        return new ResponseEntity<>(retailService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/area/{area}")
    public ResponseEntity<List<Retail>> findAllByArea(@PathVariable String area) {
        return new ResponseEntity<>(retailService.findAllByArea(area), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable Long id, @RequestBody RetailRequest retailRequest) {
        retailService.update(id, retailRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        retailService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}