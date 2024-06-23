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
import com.dev.gbk.service.MenuService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MenuSeeder implements ApplicationRunner {
    private final MenuService menuService;

    private static final Logger log = LoggerFactory.getLogger(RoleSeeder.class);

    @Override
    public void run(ApplicationArguments args) {
        if (args.getOptionValues("seeder") != null) {
            List<String> seeder = Arrays.asList(args.getOptionValues("seeder").get(0).split(","));
            if (seeder.contains("menu")) {
                seedMenu();
            }
            log.info("Success run MenuSeeder");
        } else {
            log.info("Menu seeder skipped");
        }
    }

    private void seedMenu() {
        List<String> menus = new ArrayList<>();
        menus.add("Dashboard");
        menus.add("Calendar Plan");
        menus.add("Maps Plan");
        menus.add("Data Venue");
        menus.add("Data Retail");
        menus.add("Booking Offline");
        menus.add("List Transaction");
        menus.add("Upload Transaction");
        menus.add("Role");
        menus.add("User");

        for (var menu : menus) {
            Menu m = Menu.builder()
                    .name(menu)
                    .build();

            this.menuService.save(m);

            log.info("Success run MenuSeeder {}", menus.get(menus.indexOf(menu)));
        }
    }
}
