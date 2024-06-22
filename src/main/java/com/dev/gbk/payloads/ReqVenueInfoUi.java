package com.dev.gbk.payloads;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReqVenueInfoUi {
    private String name;
    @NotNull
    private int isActive;
    @NotNull
    private int status;

}