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
public class VenueRequest {

    @NotEmpty
    private String unit;

    @NotEmpty
    private String venue;

    @NotEmpty
    private Integer capacity;

    @NotEmpty
    private String size;

    @NotEmpty
    private String contact;

    @NotEmpty
    private String type;

    @NotEmpty
    private String status;
    
}
