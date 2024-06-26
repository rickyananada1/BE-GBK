package com.dev.gbk.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReqScheduleUi {
    @NotNull
    private Long venueId;
    @NotNull
    @NotBlank
    private String dateFrom;
    @NotNull
    @NotBlank
    private String dateTo;
}