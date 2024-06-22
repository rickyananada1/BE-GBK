package com.dev.gbk.dto;

import java.util.Date;

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
public class ScheduleRequest {
    private String type;
    private String profileEvent;
    private String descriptionEvent;
    private String games;
    private String category;
    private Date scheduleDate;
    private Date scheduleDateFrom;
    private Date scheduleDateTo;
    private String status;
    private Integer total;
    private Long venueId;
}
