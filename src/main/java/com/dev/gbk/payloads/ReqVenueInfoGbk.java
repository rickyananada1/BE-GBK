package com.dev.gbk.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReqVenueInfoGbk {
    @JsonProperty("PartnerID")
    private int partnerId;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("IsActive")
    private int isActive;
    @JsonProperty("Status")
    private int status;

    public ReqVenueInfoGbk(int partnerId, ReqVenueInfoUi reqVenueInfoUi) {
        this.partnerId = partnerId;
        this.name = reqVenueInfoUi.getName();
        this.isActive = reqVenueInfoUi.getIsActive();
        this.status = reqVenueInfoUi.getStatus();
    }

}