package com.dev.gbk.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReqScheduleGbk {
    @JsonProperty("PartnerID")
    private int partnerId;
    @JsonProperty("VenueID")
    private Long venueId;
    @JsonProperty("DateFrom")
    private String dateFrom;
    @JsonProperty("DateTo")
    private String dateTo;

    public ReqScheduleGbk(int partnerId, ReqScheduleUi reqScheduleUi) {
        this.partnerId = partnerId;
        this.venueId = reqScheduleUi.getVenueId();
        this.dateFrom = reqScheduleUi.getDateFrom();
        this.dateTo = reqScheduleUi.getDateTo();
    }

}