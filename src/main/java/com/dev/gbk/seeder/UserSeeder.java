package com.dev.gbk.seeder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dev.gbk.dto.UserRequest;
import com.dev.gbk.service.RoleService;
import com.dev.gbk.service.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class UserSeeder implements ApplicationRunner {
    private final RoleService roleService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserSeeder.class);

    @Override
    public void run(ApplicationArguments args) {
        if (args.getOptionValues("seeder") != null) {
            List<String> seeder = Arrays.asList(args.getOptionValues("seeder").get(0).split(","));
            if (seeder.contains("user")) {
                seedUser();
            }
            log.info("Success run user seeder");
        } else {
            log.info("User seeder skipped");
        }
    }

    private void seedUser() {
        List<String> roleAdminList = new ArrayList<>();
        roleAdminList.add(roleService.findByName("ROLE_ADMIN").getName());
        // map to string

        UserRequest admin = UserRequest.builder()
                .name("Admin")
                .username("admin")
                .email("admin@gmail")
                .password(passwordEncoder.encode("password"))
                .roles(roleAdminList)
                .build();
        this.userService.save(admin);

        List<String> roleUserList = new ArrayList<>();
        roleUserList.add(roleService.findByName("ROLE_USER").getName());

        UserRequest user = UserRequest.builder()
                .name("User")
                .username("user")
                .email("user@gmail")
                .password(passwordEncoder.encode("password"))
                .roles(roleUserList)
                .build();
        this.userService.save(user);
        log.info("Success run UserSeeder");
    }
}
