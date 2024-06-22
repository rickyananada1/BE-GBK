package com.dev.gbk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dev.gbk.model.Venue;

public interface VenueRepository extends JpaRepository<Venue, Long>, JpaSpecificationExecutor<Venue> {
    Optional<Venue> findByVenue(String venue);

    boolean existsByVenue(String venue);

    boolean existsByVenueAndIdNot(String venue, Long id);
}
