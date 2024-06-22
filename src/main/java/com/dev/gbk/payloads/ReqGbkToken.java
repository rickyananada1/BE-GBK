package com.dev.gbk.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReqGbkToken {
    @JsonProperty("PartnerID")
    private int PartnerID;
    @JsonProperty("PartnerKey")
    private String PartnerKey;

    public ReqGbkToken(int partnerID, String partnerKey) {
        this.PartnerID = partnerID;
        this.PartnerKey = partnerKey;
    }
}
