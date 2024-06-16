package com.dev.gbk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    private String name;

    private String username;

    private String email;

    private String password;

    private String roles;
}