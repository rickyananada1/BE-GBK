package com.dev.gbk.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;

@Data
@Component("systemProperties")
@ConfigurationProperties(prefix = "schedule")
public class SystemProperties {
  private HashMap<String, BigDecimal> maintenance;
}
