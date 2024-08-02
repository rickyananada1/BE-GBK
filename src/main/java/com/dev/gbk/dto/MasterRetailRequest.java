package com.dev.gbk.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
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
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MasterRetailRequest {

    @NotEmpty
    @JsonProperty("tenant_number")
    private String tenant_number;

    @NotEmpty
    @JsonProperty("tenant_name")
    private String tenant_name;

    @NotEmpty
    @JsonProperty("area")
    private String area;
}