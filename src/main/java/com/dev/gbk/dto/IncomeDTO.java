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
    @JsonProperty("retailIncome")
    private BigDecimal retailIncome;

    @JsonProperty("retailProyeksi")
    private BigDecimal retailProyeksi;

    @JsonProperty("retailOccupied")
    private BigDecimal retailOccupied;

    @JsonProperty("retailNonOccupied")
    private BigDecimal retailNonOccupied;

    @JsonProperty("maintenanceLapangan")
    private Integer maintenanceLapangan;

    @JsonProperty("maintenanceVenue")
    private Integer maintenanceVenue;

    @JsonProperty("maintenanceParkir")
    private BigDecimal totalMaintenanceParkir;

    @JsonProperty("totalpendapatansewalahan")
    private BigDecimal totalPendapatanSewaLahan;

    @JsonProperty("totalpendapatansewalahanproyeksi")
    private BigDecimal totalpendapatanselahanproyeksi;

    @JsonProperty("totalpendapatangamesumum")
    private BigDecimal totalPendapatanGamesUmum;

    @JsonProperty("totalpendapatangamesumumproyeksi")
    private BigDecimal totalPendapatanGamesUmumProyeksi;

    @JsonProperty("totalpendapatangamestimnas")
    private BigDecimal totalPendapatanGamestimnas;

    @JsonProperty("totalpendapatangamestimnasproyeksi")
    private BigDecimal totalPendapatanGamestimnasProyeksi;

    @JsonProperty("totalpendapatanmaintenancelapangan")
    private BigDecimal totalPendapatanMaintenancelapangan;

    @JsonProperty("totalpendapatanmaintenancevenue")
    private BigDecimal totalPendapatanMaintenanceVenue;

    @JsonProperty("totalpendapataneventsolahraga")
    private BigDecimal totalPendapatanEventsOlahraga;

    @JsonProperty("totalpendapataneventsolahragaproyeksi")
    private BigDecimal totalPendapatanEventsOlahragaProyeksi;

    @JsonProperty("totalpendapataneventsnonolahraga")
    private BigDecimal totalPendapatanEventsNonOlahraga;

    @JsonProperty("totalpendapataneventsnonolahragaproyeksi")
    private BigDecimal totalPendapatanEventsNonOlahragaProyeksi;

}