package com.dev.gbk.controller;

import com.dev.gbk.model.Booking;
import com.dev.gbk.repo.BookRepo;
import com.dev.gbk.service.GbkFeignClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import payloads.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api")
public class BookingController {
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    GbkFeignClient gbkFeignClient;

    @Autowired
    Environment env;

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

    @PostMapping("/info/venue")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getVenueInfo(@RequestAttribute String gbkToken ,@Valid @RequestBody ReqVenueInfoUi reqVenueInfoUi){
        ObjectMapper objectMapper = new ObjectMapper();
        String tokenGbk = "Bearer " + gbkToken;
        ReqVenueInfoGbk reqVenueInfoGbk = new ReqVenueInfoGbk(Integer.parseInt(env.getProperty("gbk.partner.id")), reqVenueInfoUi);
        RespVenueInfoGbk respVenueInfoGbk = gbkFeignClient.getVenueInfoGbk(tokenGbk, reqVenueInfoGbk).getBody();

        try{
            logger.info("Request to GBK [{}]", objectMapper.writeValueAsString(reqVenueInfoGbk));
            logger.info("Response from GBK [{}]", objectMapper.writeValueAsString(respVenueInfoGbk));
        }catch (Exception e){
            logger.info("Exception object mapper [{}]", e.getMessage());
        }
        return ResponseEntity.ok(respVenueInfoGbk != null ? respVenueInfoGbk.getData() : new ListDataVenueInfoGbk());
    }

    @PostMapping("/info/schedule")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getSchedule(@RequestAttribute String gbkToken, @Valid @RequestBody ReqScheduleUi reqScheduleUi){
        ObjectMapper objectMapper = new ObjectMapper();
        String tokenGbk = "Bearer " + gbkToken;
        ReqScheduleGbk reqScheduleGbk = new ReqScheduleGbk(Integer.parseInt(env.getProperty("gbk.partner.id")), reqScheduleUi);
        RespScheduleGbk respScheduleGbk = gbkFeignClient.getScheduleGbk(tokenGbk, reqScheduleGbk).getBody();

        try{
            logger.info("Request to GBK [{}]", objectMapper.writeValueAsString(reqScheduleGbk));
            logger.info("Response from GBK [{}]", objectMapper.writeValueAsString(respScheduleGbk));
        }catch (Exception e){
            logger.info("Exception object mapper [{}]", e.getMessage());
        }
        return ResponseEntity.ok(respScheduleGbk != null ? respScheduleGbk.getData() : new ListScheduleGbk());
    }
}
