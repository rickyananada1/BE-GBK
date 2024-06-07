package com.dev.gbk.service;


import com.dev.gbk.model.Booking;
import com.dev.gbk.security.UserPrincipalDetails;
import payloads.ReqVenueBookDto;

public interface BookingService {
    Booking addNewBooking(UserPrincipalDetails userPrincipal, ReqVenueBookDto reqVenueBookDto);
}
