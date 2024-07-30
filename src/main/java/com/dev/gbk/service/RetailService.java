package com.dev.gbk.service;

import java.util.List;
import java.util.Optional;

import com.dev.gbk.exception.ResourceNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dev.gbk.dto.RetailRequest;
import com.dev.gbk.model.MasterRetail;
import com.dev.gbk.model.Retail;
import com.dev.gbk.repository.MasterRetailRepository;
import com.dev.gbk.repository.RetailRepository;
import com.dev.gbk.spesification.SpecificationBuilderImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RetailService {
    private final MasterRetailRepository masterRetailRepository;
    private final RetailRepository retailRepository;

    private final SpecificationBuilderImpl<Retail> specificationBuilder = new SpecificationBuilderImpl<>(
            new ObjectMapper(), Retail.class);

    public RetailService(MasterRetailRepository masterRetailRepository, RetailRepository retailRepository) {
        this.masterRetailRepository = masterRetailRepository;
        this.retailRepository = retailRepository;
    }

    public Page<Retail> findAll(String search, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt").and(Sort.by(Sort.Direction.DESC, "createdAt"));
        Pageable pageable = PageRequest.of(page, size, sort);
        Optional<Specification<Retail>> specification = specificationBuilder.parseAndBuild(search);
        return specification.map(retailSpecification -> retailRepository.findAll(retailSpecification, pageable))
                .orElseGet(() -> retailRepository.findAll(pageable));
    }

    public List<Retail> findAll() {
        return retailRepository.findAll();
    }

    public Retail findById(Long id) {
        return retailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Retail not found"));
    }

    public Retail save(RetailRequest retailRequest) {
        MasterRetail masterRetail = masterRetailRepository.findById(retailRequest.getMaster_retail_id()).get();

        if (masterRetail == null) {
            throw new ResourceNotFoundException("Master Retail not found");
        }

        Retail retail = Retail.builder().masterRetail(masterRetail)
                .size(retailRequest.getSize()).price(retailRequest.getPrice()).month(retailRequest.getMonth())
                .status(retailRequest.getStatus()).build();
        return retailRepository.save(retail);
    }

    public Retail update(Long id, RetailRequest retailRequest) {
        // check if master retail exists
        MasterRetail masterRetail = masterRetailRepository.findById(retailRequest.getMaster_retail_id()).get();
        if (masterRetail == null) {
            throw new ResourceNotFoundException("Master Retail not found");
        }

        // check if retail exists
        Retail retail = this.findById(id);
        retail.setMasterRetail(masterRetail);
        retail.setSize(retailRequest.getSize());
        retail.setPrice(retailRequest.getPrice());
        retail.setMonth(retailRequest.getMonth());
        retail.setStatus(retailRequest.getStatus());
        return retailRepository.save(retail);
    }

    public void deleteById(Long id) {
        retailRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Retail not found"));
        retailRepository.deleteById(id);
    }
}
