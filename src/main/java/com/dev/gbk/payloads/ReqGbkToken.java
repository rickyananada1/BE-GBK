package com.dev.gbk.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqGbkToken {
    @JsonProperty("PartnerID")
    private int PartnerID;
    @JsonProperty("PartnerKey")
    private String PartnerKey;

}
