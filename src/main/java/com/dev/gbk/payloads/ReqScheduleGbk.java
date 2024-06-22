package com.dev.gbk.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReqScheduleGbk {
    @JsonProperty("PartnerID")
    private int partnerId;
    @JsonProperty("VenueID")
    private int venueId;
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