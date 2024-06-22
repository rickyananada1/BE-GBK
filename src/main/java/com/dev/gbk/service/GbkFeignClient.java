package com.dev.gbk.service;

import com.dev.gbk.payloads.*;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "gbkFeignClient", url = "${gbk.client.url}")
public interface GbkFeignClient {
    @PostMapping("/api/info/token")
    ResponseEntity<RespGbkToken> getTokenGbk(@RequestBody ReqGbkToken reqGbkToken);

    @PostMapping("/api/info/venue")
    ResponseEntity<RespVenueInfoGbk> getVenueInfoGbk(
            @RequestHeader(value = "Authorization") String authorizationHeader,
            @RequestBody ReqVenueInfoGbk reqVenueInfoGbk);

    @PostMapping("/api/info/schedule")
    ResponseEntity<RespScheduleGbk> getScheduleGbk(
            @RequestHeader(value = "Authorization") String authorizationHeader,
            @RequestBody ReqScheduleGbk reqScheduleGbk);

}
