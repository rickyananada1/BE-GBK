package com.dev.gbk.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "venues")
public class Venue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 60)
    private String unit;

    @Column(length = 60)
    private String venue;

    @Column(length = 60)
    private Integer capacity;

    @Column(length = 60)
    private String size;

    @Column(length = 60)
    private String contact;

    @Column(length = 60)
    private String type;

    @Column(length = 60)
    private String status;

    @Column(length = 60)
    private String total_orders;

    @Column(length = 60)
    private String weekend;

    @Column(length = 60)
    private String morning_weekdays;

    @Column(length = 60)
    private String afternoof_weekdays;

    @Column(length = 60)
    private String evening_weekdays;
}