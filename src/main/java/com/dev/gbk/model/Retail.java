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
@Table(name = "retails")
public class Retail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 60)
    private String tenant_number;

    @Column(length = 60)
    private String tenant_name;

    @Column(length = 60)
    private String area;

    @Column(length = 60)
    private String size;

    @Column(length = 60)
    private Double price;

    @Column(length = 60)
    private Integer year;

    @Column(length = 60)
    private String status;
}
