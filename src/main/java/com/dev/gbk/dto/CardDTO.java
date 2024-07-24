package com.dev.gbk.dto;

import java.time.LocalDate;

import com.dev.gbk.model.Retail;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CardDTO {
    private String venue;
    private LocalDate date;
    private long sumTerpakai;
    private long sumMaintenance;
    private double price;
    private double occupiedSize;
    private double nonOccupiedSize;
    private double occupiedPercentage;
    private Retail retail;

    public CardDTO(String venue, LocalDate date, long sumTerpakai, long sumMaintenance) {
        this.venue = venue;
        this.date = date;
        this.sumTerpakai = sumTerpakai;
        this.sumMaintenance = sumMaintenance;
    }

    public CardDTO(double price, double occupiedSize, double nonOccupiedSize, double occupiedPercentage,
            Retail retail) {
        this.price = price;
        this.occupiedSize = occupiedSize;
        this.nonOccupiedSize = nonOccupiedSize;
        this.occupiedPercentage = occupiedPercentage;
        this.retail = retail;
    }
}
