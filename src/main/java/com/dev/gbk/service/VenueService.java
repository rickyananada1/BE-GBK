package com.dev.gbk.service;

import java.util.List;

import com.dev.gbk.model.Venue;
import org.springframework.stereotype.Service;

import com.dev.gbk.dto.VenueRequest;
import com.dev.gbk.exception.ResourceNotFoundException;
import com.dev.gbk.repository.VenueRepository;

@Service
public class VenueService {
    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    // crud operation
    public List<Venue> findAllByUnit(String area) {
        return venueRepository.findAllByUnit(area);
    }

    public List<Venue> findAll() {
        return venueRepository.findAll();
    }

    public Venue findById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
    }

    public Venue save(VenueRequest venueRequest) {
        if (venueRepository.findByVenue(venueRequest.getVenue()).isPresent()) {
            throw new ResourceNotFoundException("Venue already exists");
        }
        Venue v = Venue.builder().unit(venueRequest.getUnit())
                .unit(venueRequest.getUnit()).capacity(venueRequest.getCapacity())
                .size(venueRequest.getSize()).contact(venueRequest.getContact()).type(venueRequest.getType())
                .status(venueRequest.getStatus()).build();
        return venueRepository.save(v);
    }

    public void update(Long id, VenueRequest venueRequest) {
        Venue venue = this.findById(id);
        if (venueRepository.findByVenue(venueRequest.getVenue()).isPresent()) {
            throw new ResourceNotFoundException("Venue already exists");
        }
        venue.setUnit(venueRequest.getUnit());
        venue.setCapacity(venueRequest.getCapacity());
        venue.setSize(venueRequest.getSize());
        venue.setContact(venueRequest.getContact());
        venue.setType(venueRequest.getType());
        venue.setStatus(venueRequest.getStatus());
        venueRepository.save(venue);
    }

    public void deleteById(Long id) {
        this.findById(id);
        venueRepository.deleteById(id);
    }
}
