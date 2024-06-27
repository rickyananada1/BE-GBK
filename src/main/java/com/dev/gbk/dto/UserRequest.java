package com.dev.gbk.dto;

import java.util.List;

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
public class UserRequest {
    @NotEmpty
    @JsonProperty("name")
    private String name;

    @NotEmpty
    @JsonProperty("username")
    private String username;

    @NotEmpty
    @JsonProperty("email")
    private String email;

    @NotEmpty
    @JsonProperty("password")
    private String password;

    @NotEmpty
    @JsonProperty("contact_person")
    private String contact_person;

    @NotEmpty
    @JsonProperty("division")

    private String division;

    @NotEmpty
    @JsonProperty("unit")
    private String unit;

    @NotEmpty
    @JsonProperty("status")
    private String status;

    @NotEmpty
    @JsonProperty("roles")
    private List<String> roles;
}