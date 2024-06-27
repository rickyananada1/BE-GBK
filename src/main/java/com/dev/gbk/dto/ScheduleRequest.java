package com.dev.gbk.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

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
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate scheduleDate;
    @JsonProperty("ScheduleTimeFrom")
    @JsonFormat(pattern = "HH:mm:ss")
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime scheduleTimeFrom;
    @JsonProperty("ScheduleTimeTo")
    @JsonFormat(pattern = "HH:mm:ss")
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime scheduleTimeTo;
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
