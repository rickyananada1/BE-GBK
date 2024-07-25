package com.dev.gbk.dto;

import java.math.BigDecimal;

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
    private BigDecimal eventIncome;

    @JsonProperty("gamesIncome")
    private BigDecimal gamesIncome;

    @JsonProperty("retailIncome")
    private BigDecimal retailIncome;

    @JsonProperty("retailOccupied")
    private BigDecimal retailOccupied;

    @JsonProperty("retailNonOccupied")
    private BigDecimal retailNonOccupied;

    @JsonProperty("maintenanceLapangan")
    private BigDecimal maintenanceLapangan;

    @JsonProperty("maintenanceParkir")
    private BigDecimal totalMaintenanceParkir;
}