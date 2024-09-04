package com.dev.gbk;

import com.dev.gbk.properties.SystemProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableConfigurationProperties(SystemProperties.class)
public class GbkApplication {

	public static void main(String[] args) {
		SpringApplication.run(GbkApplication.class, args);
	}

}
