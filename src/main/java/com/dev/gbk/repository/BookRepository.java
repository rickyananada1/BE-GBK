package com.dev.gbk.repository;

import com.dev.gbk.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Booking, Long> {

}
