package com.dev.gbk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dev.gbk.model.Venue;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    // find all by area
    List<Venue> findAllByUnit(String area);

    Optional<Venue> findByVenue(String name);
}
