package com.dev.gbk.security;

import com.dev.gbk.model.Permission;
import com.dev.gbk.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

public interface UserPrincipalDetails extends UserDetails {
    Set<String> getPermissions();

    Long asUser();

    boolean can(String permission);
}
