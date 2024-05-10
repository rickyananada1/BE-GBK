/**
 *
 */
package com.dev.gbk.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String venue;

    @NotBlank
    @Size(max = 15)
    private Integer rangeDate;

    @NotBlank
    @Size(max = 40)
    private Date waktuMulai;

    @NotBlank
    @Size(max = 100)
    private Date waktuSelesai;

    @NotBlank
    @Size(max = 40)
    private Long user_id;

    public Booking() {

    }

    public Booking(String venue, Integer rangeDate, Date waktuMulai, Date waktuSelesai, Long user_id) {
        this.venue = venue;
        this.rangeDate = rangeDate;
        this.waktuMulai = waktuMulai;
        this.waktuSelesai = waktuSelesai;
        this.user_id = user_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Integer getRangeDate() {
        return rangeDate;
    }

    public void setRangeDate(Integer rangeDate) {
        this.rangeDate = rangeDate;
    }

    public Date getWaktuMulai() {
        return waktuMulai;
    }

    public void setWaktuMulai(Date waktuMulai) {
        this.waktuMulai = waktuMulai;
    }

    public Date getWaktuSelesai() {
        return waktuSelesai;
    }

    public void setWaktuSelesai(Date waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

}
