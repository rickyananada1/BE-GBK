package com.dev.gbk.service;

import com.dev.gbk.model.User;
import com.dev.gbk.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

        @Autowired
        private UserRepository userRepository;

        private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findByUsernameOrEmail(username, username)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "User not found with username: " + username));

                // get roles
                Collection<GrantedAuthority> authorities = user.getRoles().stream()
                                .map(role -> new SimpleGrantedAuthority(role.getName()))
                                .collect(Collectors.toSet());

                // get permissions
                Collection<GrantedAuthority> permissions = user.getRoles().stream()
                                .flatMap(role -> role.getPermissions().stream())
                                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                                .collect(Collectors.toSet());

                authorities.addAll(permissions);

                log.info("User found with username: " + user.getUsername() + ", Authorities: " + authorities);

                return new org.springframework.security.core.userdetails.User(
                                user.getUsername(),
                                user.getPassword(),
                                authorities);
        }
}