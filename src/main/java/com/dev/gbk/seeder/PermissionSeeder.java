package com.dev.gbk.seeder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import com.dev.gbk.model.Menu;
import com.dev.gbk.model.Permission;
import com.dev.gbk.service.MenuService;
import com.dev.gbk.service.PermissionService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class PermissionSeeder implements ApplicationRunner {
    private final MenuService menuService;
    private final PermissionService permissionService;

    private static final Logger log = LoggerFactory.getLogger(RoleSeeder.class);

    @Override
    public void run(ApplicationArguments args) {
        if (args.getOptionValues("seeder") != null) {
            List<String> seeder = Arrays.asList(args.getOptionValues("seeder").get(0).split(","));
            if (seeder.contains("permission")) {
                seedPermission();
            }
            log.info("Success run Permission seeder");
        } else {
            log.info("Permission seeder skipped");
        }
    }

    private void seedPermission() {
        // get all menu
        List<Menu> menus = new ArrayList<>();
        menuService.findAll().forEach(menus::add);

        try {
            for (var menu : menus) {
                // change menu space to _ and uppercase
                Permission view = Permission.builder()
                        .name("VIEW_" + menu.getName().toUpperCase().replace(" ", "_"))
                        .build();

                // create
                Permission create = Permission.builder()
                        .name("CREATE_" + menu.getName().toUpperCase().replace(" ", "_"))
                        .build();

                // update
                Permission update = Permission.builder()
                        .name("UPDATE_" + menu.getName().toUpperCase().replace(" ", "_"))
                        .build();

                // delete
                Permission delete = Permission.builder()
                        .name("DELETE_" + menu.getName().toUpperCase().replace(" ", "_"))
                        .build();

                // save permission
                permissionService.save(view);
                permissionService.save(create);
                permissionService.save(update);
                permissionService.save(delete);

                // menu.setPermissions(List.of(view, create, update, delete));
                // menuService.update(menu);

                log.info("Success run PermissionSeeder {}", menu.getName());
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
