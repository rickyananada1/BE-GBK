package com.dev.gbk.controller;

import com.dev.gbk.model.Booking;
import com.dev.gbk.repo.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import payloads.ApiResponse;
import payloads.BookingRequest;

import javax.validation.Valid;


@RestController
@RequestMapping("/api")
public class BookingController {

    @Autowired
    private BookRepo bookRepo;

    @GetMapping("/bookings")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllBookings() {
        return ResponseEntity.ok(bookRepo.findAll());
    }

    @PostMapping("/bookings")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId = ((Booking) authentication.getPrincipal()).getId();
        System.out.println("userId: " + userId);
        Booking booking = new Booking(bookingRequest.getVenue(),
                bookingRequest.getRangeDate(),
                bookingRequest.getWaktuMulai(),
                bookingRequest.getWaktuSelesai(),
                userId);

        Booking result = bookRepo.save(booking);
        System.out.println(result);
        return ResponseEntity.ok(new ApiResponse(true, "Booking created successfully"));
    }
}
