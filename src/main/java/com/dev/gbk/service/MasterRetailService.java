package com.dev.gbk.service;

import java.util.List;
import java.util.Optional;

import com.dev.gbk.model.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;

import com.dev.gbk.dto.MasterRetailRequest;
import com.dev.gbk.exception.GBKAPIException;
import com.dev.gbk.exception.ResourceNotFoundException;
import com.dev.gbk.model.MasterRetail;
import com.dev.gbk.model.Retail;
import com.dev.gbk.repository.MasterRetailRepository;
import com.dev.gbk.spesification.SpecificationBuilderImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MasterRetailService {
    private final MasterRetailRepository masterRetailRepository;

    private final SpecificationBuilderImpl<MasterRetail> specificationBuilder = new SpecificationBuilderImpl<>(
            new ObjectMapper(), MasterRetail.class);

    public MasterRetailService(MasterRetailRepository masterRetailRepository) {
        this.masterRetailRepository = masterRetailRepository;
    }

    public Page<MasterRetail> findAll(String search, int page, int size,List<String> unit, String unitName) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<MasterRetail> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (search != null && !search.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("tenant_name")),
                                "%" + search.toLowerCase() + "%"));
            }

            if (unit != null && !unit.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        root.get("area").in(unit));
            }

            if (unitName != null && !unitName.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("area")),
                                "%" + unitName.toLowerCase() + "%"));
            }

            return predicate;
        };

        return masterRetailRepository.findAll(specification, pageable);
    }

    public List<MasterRetail> findAll(String search,List<String> unit, String unitName) {
        Specification<MasterRetail> searchSpec = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (search != null && !search.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("tenant_name")),
                                "%" + search.toLowerCase() + "%"));
            }

            if (unit != null && !unit.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        root.get("area").in(unit));
            }

            if (unitName != null && !unitName.isEmpty()) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("area")),
                                "%" + unitName.toLowerCase() + "%"));
            }

            return predicate;
        };
        return masterRetailRepository.findAll(searchSpec);
    }

    public MasterRetail findById(Long id) {
        return masterRetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Master Retail not found"));
    }

    public MasterRetail save(MasterRetailRequest masterRetailRequest) {
        if (masterRetailRepository.findByTenantNumber(masterRetailRequest.getTenant_number()).isPresent()) {
            throw new GBKAPIException("Tenant number already exists");
        }
        MasterRetail masterRetail = MasterRetail.builder().tenant_number(masterRetailRequest.getTenant_number())
                .tenant_name(masterRetailRequest.getTenant_name()).area(masterRetailRequest.getArea())
                .build();
        return masterRetailRepository.save(masterRetail);
    }

    public MasterRetail update(Long id, MasterRetailRequest masterRetailRequest) {
        Optional<MasterRetail> existingMasterRetail = masterRetailRepository
                .findByTenantNumber(masterRetailRequest.getTenant_number());

        if (existingMasterRetail.isPresent() && !existingMasterRetail.get().getId().equals(id)) {
            throw new GBKAPIException("Tenant number already exists");
        }

        MasterRetail updatedMasterRetail = MasterRetail.builder().id(id)
                .tenant_number(masterRetailRequest.getTenant_number())
                .tenant_name(masterRetailRequest.getTenant_name()).area(masterRetailRequest.getArea())
                .build();

        return masterRetailRepository.save(updatedMasterRetail);
    }

    public void deleteById(Long id) {
        masterRetailRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Master Retail not found"));
        masterRetailRepository.deleteById(id);
    }
}
