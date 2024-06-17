package com.dev.gbk.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    @NotEmpty
    private String usernameOrEmail;

    @NotEmpty
    private String password;
}