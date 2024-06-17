package com.dev.gbk.dto;

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
public class RetailRequest {

    @NotEmpty
    private String tenant_number;

    @NotEmpty
    private String tenant_name;

    @NotEmpty
    private String area;

    @NotEmpty
    private String size;

    @NotEmpty
    private Double price;

    @NotEmpty
    private Integer year;

    @NotEmpty
    private String status;
}
