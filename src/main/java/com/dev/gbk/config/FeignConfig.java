package com.dev.gbk.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableFeignClients(basePackages="com.dev.gbk")
public class FeignConfig {


}
