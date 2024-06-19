package com.dev.gbk.dto;

import java.util.List;

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
public class UserRequest {
    @NotEmpty
    private String name;

    @NotEmpty
    private String username;

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String contact_person;

    @NotEmpty
    private String division;

    @NotEmpty
    private String unit;

    @NotEmpty
    private String status;

    @NotEmpty
    private List<String> roles;
}