package com.dev.gbk.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleRequest {
    @Nullable
    @JsonProperty("BookingNumber")
    private String bookingNumber;

    @Nullable
    @JsonProperty("Type")
    private String type;

    @Nullable
    @JsonProperty("ProfileEvent")
    private String profileEvent;

    @Nullable
    @JsonProperty("DescriptionEvent")
    private String descriptionEvent;

    @Nullable
    @JsonProperty("Games")
    private String games;

    @Nullable
    @JsonProperty("Category")
    private String category;

    @JsonProperty("ScheduleStartInLoad")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String scheduleStartInLoad;

    @JsonProperty("ScheduleEndInLoad")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String scheduleEndInLoad;

    @JsonProperty("ScheduleStartDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String scheduleStartDate;

    @JsonProperty("ScheduleEndDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String scheduleEndDate;

    @JsonProperty("ScheduleStartOutLoad")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String scheduleStartOutLoad;

    @JsonProperty("ScheduleEndOutLoad")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String scheduleEndOutLoad;

    @JsonProperty("ScheduleTime")
    private List<String> scheduleTime; // List to hold multiple times

    @JsonProperty("Session")
    private List<String> session; // List to hold multiple sessions

    @JsonProperty("StatusPayment")
    private String statusPayment;

    @JsonProperty("StatusBooking")
    private String statusBooking;

    @JsonProperty("TotalSF")
    private String totalSF;

    @JsonProperty("TotalPaid")
    private String totalPaid;

    @JsonProperty("CustomerName")
    private String customerName;

    @JsonProperty("CustomerEmail")
    private String customerEmail;

    @JsonProperty("CustomerPhone")
    private String customerPhone;

    @JsonProperty("VenueID")
    private List<Long> venueID;

    @JsonProperty("UnitId")
    private Long unitId;
}