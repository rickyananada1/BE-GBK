package com.dev.gbk.seeder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import com.dev.gbk.dto.RoleRequest;
import com.dev.gbk.service.PermissionService;
import com.dev.gbk.service.RoleService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RoleSeeder implements ApplicationRunner {
    private final RoleService roleService;
    private final PermissionService permissionService;

    private static final Logger log = LoggerFactory.getLogger(RoleSeeder.class);

    @Override
    public void run(ApplicationArguments args) {
        if (args.getOptionValues("seeder") != null) {
            List<String> seeder = Arrays.asList(args.getOptionValues("seeder").get(0).split(","));
            if (seeder.contains("role")) {
                seedRoles();
                log.info("Success run Role seeder");
            }
        } else

        {
            log.info("Role seeder skipped");
        }
    }

    private void seedRoles() {
        List<String> permissions = new ArrayList<>();
        permissionService.findAll().forEach(permission -> permissions.add(permission.getName()));

        RoleRequest admin = RoleRequest.builder()
                .name("ROLE_ADMIN")
                .build();

        admin.setPermissions(permissions);

        this.roleService.save(admin);

        RoleRequest user = RoleRequest.builder()
                .name("ROLE_USER")
                .build();

        this.roleService.save(user);

        log.info("Success run RoleSeeder");

    }
}