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

import com.dev.gbk.model.Role;
import com.dev.gbk.model.User;
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
            if (seeder.contains("role")) {
                seedUser();
                log.info("Success run role seeder");
            }
        } else {
            log.info("Role seeder skipped");
        }
    }

    private void seedUser() {
        List<Role> roleAdminList = new ArrayList<>();
        roleAdminList.add(roleService.findByName("Admin").get());
        User admin = User.builder()
                .name("Admin")
                .username("admin")
                .email("admin@gmail.com")
                .password(passwordEncoder.encode("password"))
                .roles(roleAdminList)
                .build();
        this.userService.save(admin);

        List<Role> roleUserList = new ArrayList<>();
        roleUserList.add(roleService.findByName("User").get());

        User user = User.builder()
                .name("User")
                .username("user")
                .email("user@gmail.com")
                .password(passwordEncoder.encode("password"))
                .roles(roleUserList)
                .build();
        this.userService.save(user);
        log.info("Success run UserSeeder");
    }
}