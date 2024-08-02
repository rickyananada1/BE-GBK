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
public class RetailRequest {
    @NotEmpty
    @JsonProperty("size")
    private String size;

    @NotEmpty
    @JsonProperty("price")
    private Double price;

    @NotEmpty
    @JsonProperty("month")
    private String month;

    @NotEmpty
    @JsonProperty("statusBooking")
    private String statusBooking;

    @NotEmpty
    @JsonProperty("statusPayment")
    private String statusPayment;

    @JsonProperty("description")
    private String description;

    @NotEmpty
    @JsonProperty("master_retail_id")
    private Long master_retail_id;
}
