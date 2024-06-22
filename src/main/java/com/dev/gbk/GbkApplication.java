package com.dev.gbk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GbkApplication {

	public static void main(String[] args) {
		SpringApplication.run(GbkApplication.class, args);
	}

}
