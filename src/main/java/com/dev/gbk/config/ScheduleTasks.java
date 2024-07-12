package com.dev.gbk.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dev.gbk.model.Venue;
import com.dev.gbk.payloads.ReqGbkToken;
import com.dev.gbk.payloads.ReqScheduleGbk;
import com.dev.gbk.payloads.ReqVenueInfoGbk;
import com.dev.gbk.payloads.RespGbkToken;
import com.dev.gbk.payloads.RespScheduleGbk;
import com.dev.gbk.payloads.RespVenueInfoGbk;
import com.dev.gbk.service.GbkFeignClient;
import com.dev.gbk.service.ScheduleService;
import com.dev.gbk.service.VenueService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ScheduleTasks {

    @Value("${gbk.partner.key}")
    private String gbkApiKey;

    @Value("${gbk.partner.id}")
    private int gbkApiId;

    private static final Logger logger = LoggerFactory.getLogger(ScheduleTasks.class);

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final GbkFeignClient gbkFeignClient;

    private final VenueService venueService;

    private final ScheduleService scheduleService;

    public ScheduleTasks(GbkFeignClient gbkFeignClient, VenueService venueService, ScheduleService scheduleService) {
        this.gbkFeignClient = gbkFeignClient;
        this.venueService = venueService;
        this.scheduleService = scheduleService;
    }

    // every day at
    // @Scheduled(cron = "0 0 0 * * *")
    // @Scheduled(fixedRate = 300000)
    // public void synchronizeVenueData() {
    // logger.info("Synchronizing Venue Data :: Execution Time - {}",
    // dateTimeFormatter.format(LocalDateTime.now()));
    //
    // try {
    // ReqGbkToken reqGbkToken = new ReqGbkToken(gbkApiId, gbkApiKey);
    // ResponseEntity<RespGbkToken> respGbkToken =
    // gbkFeignClient.getTokenGbk(reqGbkToken);
    // if (respGbkToken.getStatusCode().is2xxSuccessful()) {
    // String gbkToken = respGbkToken.getBody().getToken();
    // ReqVenueInfoGbk reqVenueInfoGbk = new ReqVenueInfoGbk(gbkApiId, "", 1, 0);
    // ResponseEntity<RespVenueInfoGbk> respVenueInfoGbk =
    // gbkFeignClient.getVenueInfoGbk("Bearer " + gbkToken,
    // reqVenueInfoGbk);
    // if (respVenueInfoGbk.getStatusCode().is2xxSuccessful()) {
    // if (!respVenueInfoGbk.getBody().getData().isEmpty()) {
    // venueService.synchronizeVenues(respVenueInfoGbk.getBody().getData());
    // }
    // logger.info("Synchronizing Venue Data :: Success");
    // } else {
    // logger.error("Synchronizing Venue Data :: Failed");
    // }
    // }
    // } catch (Exception e) {
    // logger.error("Error during venue synchronization: {}", e.getMessage());
    // }
    // }

    // synchronize schedule data every 5 minutes
    @Scheduled(fixedRate = 300000)
    public void synchronizeScheduleData() {
        logger.info("Synchronizing Schedule Data :: Execution Time - {}",
                dateTimeFormatter.format(LocalDateTime.now()));
        try {
            ReqGbkToken reqGbkToken = new ReqGbkToken(gbkApiId, gbkApiKey);
            ResponseEntity<RespGbkToken> respGbkToken = gbkFeignClient.getTokenGbk(reqGbkToken);
            if (respGbkToken.getStatusCode().is2xxSuccessful()) {
                String gbkToken = respGbkToken.getBody().getToken();
                List<Venue> venues = venueService.findAll();
                for (Venue venue : venues) {
                    ReqScheduleGbk reqScheduleGbk = new ReqScheduleGbk(gbkApiId, venue.getId(), "2024-01-01",
                            "2025-01-01");
                    ResponseEntity<RespScheduleGbk> respScheduleGbk = gbkFeignClient.getScheduleGbk(
                            "Bearer " + gbkToken,
                            reqScheduleGbk);
                    if (respScheduleGbk.getStatusCode().is2xxSuccessful()) {
                        if (!respScheduleGbk.getBody().getData().isEmpty()) {
                            scheduleService.synchronizeSchedules(respScheduleGbk.getBody().getData(), venue);
                        }
                    }
                }
            }

            logger.info("Synchronizing Schedule Data :: Success");
        } catch (Exception e) {
            logger.error("Error during schedule synchronization: {}", e.getMessage());
        }
    }
}