package com.dev.gbk.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ListScheduleGbk {
    @JsonProperty("BookingNumber")
    private String bookingNumber;
    @JsonProperty("VenueID")
    private int venueId;
    @JsonProperty("PackageID")
    private String packageId;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("ProfileEvent")
    private String profileEvent;
    @JsonProperty("DescriptionEvent")
    private String DescriptionEvent;
    @JsonProperty("ScheduleDate")
    private String scheduleDate;
    @JsonProperty("ScheduleTimeFrom")
    private String ScheduleDateFrom;
    @JsonProperty("ScheduleTimeTo")
    private String ScheduleDateTo;
}