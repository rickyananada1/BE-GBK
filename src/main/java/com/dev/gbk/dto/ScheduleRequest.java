package com.dev.gbk.dto;

import jakarta.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ScheduleRequest {
    @Nullable
    @JsonProperty("BookingNumber")
    private String bookingNumber;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("ProfileEvent")
    private String profileEvent;
    @JsonProperty("DescriptionEvent")
    private String descriptionEvent;
    @Nullable
    @JsonProperty("Games")
    private String games;
    @Nullable
    @JsonProperty("Category")
    private String category;
    @JsonProperty("ScheduleDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String scheduleDate;
    @Nullable
    @JsonProperty("ScheduleTimeFrom")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private String scheduleTimeFrom;
    @Nullable
    @JsonProperty("ScheduleTimeTo")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private String scheduleTimeTo;
    @JsonProperty("Session")
    private String session;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Total")
    private Integer total;
    @JsonProperty("CustomerName")
    private String customerName;
    @JsonProperty("CustomerEmail")
    private String customerEmail;
    @JsonProperty("CustomerPhone")
    private String customerPhone;
    @JsonProperty("VenueID")
    private Long venueId;
}
