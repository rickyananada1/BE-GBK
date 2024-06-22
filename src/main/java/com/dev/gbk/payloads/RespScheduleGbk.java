package com.dev.gbk.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RespScheduleGbk {
    @JsonProperty("Code")
    private String code;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("Data")
    private List<ListScheduleGbk> data;

}
