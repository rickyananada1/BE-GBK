package com.dev.gbk.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LoginRequest {
    @NotEmpty
    @JsonProperty("usernameOrEmail")
    private String usernameOrEmail;

    @NotEmpty
    @JsonProperty("password")
    private String password;
}