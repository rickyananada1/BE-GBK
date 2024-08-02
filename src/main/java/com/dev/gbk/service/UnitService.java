package com.dev.gbk.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dev.gbk.dto.UnitRequest;
import com.dev.gbk.model.Unit;
import com.dev.gbk.repository.UnitRepository;
import com.dev.gbk.spesification.SpecificationBuilder;
import com.dev.gbk.spesification.SpecificationBuilderImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UnitService {
    private UnitRepository unitRepository;
    private final SpecificationBuilder<Unit> specificationBuilder = new SpecificationBuilderImpl<>(
            new ObjectMapper(), Unit.class);

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public List<Unit> findAll() {
        return unitRepository.findAll();
    }

    public Page<Unit> findAll(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Optional<Specification<Unit>> specification = specificationBuilder.parseAndBuild(search);
        if (specification.isPresent()) {
            return unitRepository.findAll(specification.get(), pageable);
        }
        return unitRepository.findAll(pageable);
    }

    public Unit findById(Long id) {
        return unitRepository.findById(id).orElse(null);
    }

    public Unit save(UnitRequest unitRequest) {
        Unit unit = Unit.builder().name(unitRequest.getName()).build();
        return unitRepository.save(unit);
    }

    public Unit update(Long id, UnitRequest unitRequest) {
        if (!unitRepository.existsById(id)) {
            throw new IllegalArgumentException("Unit not found");
        }
        Unit unit = unitRepository.findById(id).orElse(null);
        unit.setName(unitRequest.getName());
        return unitRepository.save(unit);
    }

    public void delete(Long id) {
        if (!unitRepository.existsById(id)) {
            throw new IllegalArgumentException("Unit not found");
        }
        unitRepository.deleteById(id);
    }

}
