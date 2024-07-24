package com.dev.gbk.dto;

import java.time.LocalDate;

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
    private String venueName;
    private LocalDate scheduleDate;
    private long usedCount;
    private long maintenanceCount;
}
