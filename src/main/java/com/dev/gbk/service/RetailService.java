package com.dev.gbk.service;

import java.util.List;
import java.util.Optional;

import com.dev.gbk.exception.GBKAPIException;
import com.dev.gbk.exception.ResourceNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dev.gbk.dto.RetailRequest;
import com.dev.gbk.model.Retail;
import com.dev.gbk.repository.RetailRepository;
import com.dev.gbk.spesification.SpecificationBuilderImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RetailService {
    private final RetailRepository retailRepository;

    private final SpecificationBuilderImpl<Retail> specificationBuilder = new SpecificationBuilderImpl<>(
            new ObjectMapper(), Retail.class);

    public RetailService(RetailRepository retailRepository) {
        this.retailRepository = retailRepository;
    }

    public Page<Retail> findAll(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Optional<Specification<Retail>> specification = specificationBuilder.parseAndBuild(search);
        return specification.isPresent() ? retailRepository.findAll(specification.get(), pageable)
                : retailRepository.findAll(pageable);
    }

    public List<Retail> findAll() {
        return retailRepository.findAll();
    }

    public Retail findById(Long id) {
        return retailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Retail not found"));
    }

    public void save(RetailRequest retailRequest) {
        if (retailRepository.findByTenantName(retailRequest.getTenant_name()).isPresent()) {
            throw new GBKAPIException("Retail already exists");
        }

        if (retailRepository.findByTenantNumber(retailRequest.getTenant_number()).isPresent()) {
            throw new GBKAPIException("Tenant number already exists");
        }
        Retail retail = Retail.builder().tenant_name(retailRequest.getTenant_name())
                .tenant_number(retailRequest.getTenant_number()).area(retailRequest.getArea())
                .size(retailRequest.getSize()).price(retailRequest.getPrice()).year(retailRequest.getYear())
                .status(retailRequest.getStatus()).build();
        retailRepository.save(retail);
    }

    public void update(Long id, RetailRequest retailRequest) {
        Optional<Retail> existingRetail = retailRepository.findByTenantName(retailRequest.getTenant_name());

        if (existingRetail.isPresent() && !existingRetail.get().getId().equals(id)) {
            throw new GBKAPIException("Tenant name already exists");
        }
        existingRetail = retailRepository.findByTenantNumber(retailRequest.getTenant_number());

        if (existingRetail.isPresent() && !existingRetail.get().getId().equals(id)) {
            throw new GBKAPIException("Tenant number already exists");
        }

        Retail retail = this.findById(id);
        retail.setTenant_name(retailRequest.getTenant_name());
        retail.setTenant_number(retailRequest.getTenant_number());
        retail.setArea(retailRequest.getArea());
        retail.setSize(retailRequest.getSize());
        retail.setPrice(retailRequest.getPrice());
        retail.setYear(retailRequest.getYear());
        retail.setStatus(retailRequest.getStatus());
        retailRepository.save(retail);

    }

    public void deleteById(Long id) {
        retailRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Retail not found"));
        retailRepository.deleteById(id);
    }
}
