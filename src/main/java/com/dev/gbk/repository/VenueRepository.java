package com.dev.gbk.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dev.gbk.model.Venue;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

public interface VenueRepository extends JpaRepository<Venue, Long>, JpaSpecificationExecutor<Venue> {
    Optional<Venue> findByVenue(String venue);

    boolean existsByVenue(String venue);

    boolean existsByVenueAndIdNot(String venue, Long id);

    @Query("SELECT v FROM Venue v WHERE " +
            "v.unit LIKE %:search% OR v.venue LIKE %:search%")
    Page<Venue> searchVenues(String search, Pageable pageable);
}
