/**
 *
 */
package com.dev.gbk.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_date")
    private LocalDate bookingDate;

    @JoinColumn(name = "venue_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Venue venue;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User userId;

    @JoinTable(
            name = "booking_time",
            joinColumns = {@JoinColumn(name = "booking_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "time_id", referencedColumnName = "id")}
    )
    @ManyToMany
    private Set<Time> times = new HashSet<>();

    public Booking() {

    }

    public Booking(LocalDate bookingDate, Venue venue, User userId, Set<Time> times) {
        this.bookingDate = bookingDate;
        this.venue = venue;
        this.userId = userId;
        this.times = times;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Set<Time> getTimes() {
        return times;
    }

    public void setTimes(Set<Time> times) {
        this.times = times;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }
}
