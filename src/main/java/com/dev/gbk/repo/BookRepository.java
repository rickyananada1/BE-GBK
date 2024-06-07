package com.dev.gbk.repo;

import com.dev.gbk.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Booking, Long> {

//    Optional<Booking> findBookingByRangeDate(Date startDate, Date endDate);
//
//    List<Booking> findByIdIn(List<Long> id);
}
