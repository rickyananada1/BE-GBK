package com.dev.gbk.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class VenueRequest {

    @NotEmpty
    @JsonProperty("unit")
    private String unit;

    @NotEmpty
    @JsonProperty("venue")
    private String venue;

    @NotEmpty
    @JsonProperty("capacity")
    private Integer capacity;

    @NotEmpty
    @JsonProperty("size")
    private String size;

    @NotEmpty
    @JsonProperty("contact")
    private String contact;

    @NotEmpty
    @JsonProperty("type")
    private String type;

    @NotEmpty
    @JsonProperty("status")
    private String status;

    @NotEmpty
    @JsonProperty("total_orders")
    private String total_orders;

    @NotEmpty
    @JsonProperty("weekend")
    private String weekend;

    @NotEmpty
    @JsonProperty("morning_weekdays")
    private String morning_weekdays;

    @NotEmpty
    @JsonProperty("afternoof_weekdays")
    private String afternoof_weekdays;

    @NotEmpty
    @JsonProperty("evening_weekdays")
    private String evening_weekdays;
}
