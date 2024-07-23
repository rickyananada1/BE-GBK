package com.dev.gbk.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class IncomeDTO {

    @JsonProperty("eventIncome")
    private double eventIncome;

    @JsonProperty("gamesIncome")
    private double gamesIncome;

    @JsonProperty("retailIncome")
    private double retailIncome;

    @JsonProperty("retailOccupied")
    private double retailOccupied;

    @JsonProperty("retailNonOccupied")
    private double retailNonOccupied;

    @JsonProperty("maintenanceLapangan")
    private double maintenanceLapangan;

    @JsonProperty("maintenanceParkir")
    private double maintenanceParkir;
}
