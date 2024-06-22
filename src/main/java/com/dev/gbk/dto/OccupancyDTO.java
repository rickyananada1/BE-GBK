package com.dev.gbk.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OccupancyDTO {
    private String judul;
    private String occPercent;

    public OccupancyDTO(String judul, String occPercent) {
        this.judul = judul;
        this.occPercent = occPercent;
    }

}
