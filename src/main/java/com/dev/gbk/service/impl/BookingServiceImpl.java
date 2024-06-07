package com.dev.gbk.service.impl;

import com.dev.gbk.model.Booking;
import com.dev.gbk.model.Time;
import com.dev.gbk.repo.BookRepository;
import com.dev.gbk.repo.TimeRepository;
import com.dev.gbk.repo.UserRepository;
import com.dev.gbk.repo.VenueRepository;
import com.dev.gbk.security.UserPrincipalDetails;
import com.dev.gbk.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import payloads.ReqVenueBookDto;

import java.util.HashSet;
import java.util.Set;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    TimeRepository timeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VenueRepository venueRepository;

    @Override
    public Booking addNewBooking(UserPrincipalDetails userPrincipal, ReqVenueBookDto reqVenueBookDto) {
        Booking booking = new Booking();
        booking.setBookingDate(reqVenueBookDto.getStartDate());
        booking.setVenue(venueRepository.findById(reqVenueBookDto.getVenueId()).get());
        Set<Time> times= new HashSet<>();
        reqVenueBookDto.getTime().forEach(time ->times.add(timeRepository.findById(time).get()));
        booking.setTimes(times);
        booking.setUserId(userRepository.findById(userPrincipal.asUser()).get());
        booking = bookRepository.save(booking);

        return booking;
    }
}
