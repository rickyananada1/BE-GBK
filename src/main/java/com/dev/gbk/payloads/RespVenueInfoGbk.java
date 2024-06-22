package com.dev.gbk.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RespVenueInfoGbk {
    @JsonProperty("Code")
    private String code;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("Data")
    private List<ListDataVenueInfoGbk> data;

}