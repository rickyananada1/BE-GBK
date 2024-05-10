package com.dev.gbk.repo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.dev.gbk.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepo extends JpaRepository<Booking, Long> {

//    Optional<Booking> findBookingByRangeDate(Date startDate, Date endDate);
//
//    List<Booking> findByIdIn(List<Long> id);
}
