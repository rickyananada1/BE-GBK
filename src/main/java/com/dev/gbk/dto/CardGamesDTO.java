package com.dev.gbk.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CardGamesDTO {
    private int totalPaid;
    private int totalMaintenance;
    private List<ScheduleDTO> paidSchedules;
    private List<ScheduleDTO> maintenanceSchedules;

}
