package com.dev.gbk.service;

import com.dev.gbk.model.Venue;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dev.gbk.dto.VenueRequest;
import com.dev.gbk.exception.GBKAPIException;
import com.dev.gbk.exception.ResourceNotFoundException;
import com.dev.gbk.repository.VenueRepository;
import com.dev.gbk.spesification.SpecificationBuilderImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VenueService {

    private final VenueRepository venueRepository;
    private final SpecificationBuilderImpl<Venue> specificationBuilder = new SpecificationBuilderImpl<>(
            new ObjectMapper(), Venue.class);

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;

    }

    public Page<Venue> findAll(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Optional<Specification<Venue>> specification = specificationBuilder.parseAndBuild(search);
        return specification.isPresent() ? venueRepository.findAll(specification.get(), pageable)
                : venueRepository.findAll(pageable);
    }

    public Venue findById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
    }

    public Venue save(VenueRequest venueRequest) {
        if (venueRepository.existsByVenue(venueRequest.getVenue())) {
            throw new GBKAPIException("Venue already exists");
        }
        Venue v = Venue.builder().unit(venueRequest.getUnit())
                .unit(venueRequest.getUnit()).capacity(venueRequest.getCapacity())
                .size(venueRequest.getSize()).contact(venueRequest.getContact()).type(venueRequest.getType())
                .status(venueRequest.getStatus()).weekend(venueRequest.getWeekend())
                .total_orders(venueRequest.getTotal_orders())
                .morning_weekdays(venueRequest.getAfternoof_weekdays())
                .evening_weekdays(venueRequest.getEvening_weekdays())
                .venue(venueRequest.getVenue())
                .build();
        return venueRepository.save(v);
    }

    public void update(Long id, VenueRequest venueRequest) {
        // check if venue exists with different id
        if (venueRepository.existsByVenueAndIdNot(venueRequest.getVenue(), id)) {
            throw new GBKAPIException("Venue already exists");
        }
        Venue venue = findById(id);
        venue.setUnit(venueRequest.getUnit());
        venue.setVenue(venueRequest.getVenue());
        venue.setCapacity(venueRequest.getCapacity());
        venue.setSize(venueRequest.getSize());
        venue.setContact(venueRequest.getContact());
        venue.setType(venueRequest.getType());
        venue.setStatus(venueRequest.getStatus());
        venue.setTotal_orders(venueRequest.getTotal_orders());
        venue.setWeekend(venueRequest.getWeekend());
        venue.setMorning_weekdays(venue.getMorning_weekdays());
        venue.setAfternoof_weekdays(venue.getAfternoof_weekdays());
        venue.setEvening_weekdays(venue.getEvening_weekdays());
        venueRepository.save(venue);
    }

    public void deleteById(Long id) {
        findById(id);
        venueRepository.deleteById(id);
    }
}
