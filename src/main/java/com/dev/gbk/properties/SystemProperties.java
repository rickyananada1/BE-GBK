package com.dev.gbk.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component("systemProperties")
@ConfigurationProperties(prefix = "schedule")
public class SystemProperties {
  private HashMap<String, BigDecimal> maintenance;
  private HashMap<String, List<String>> venuesfOfUnitForNotCalculated = new HashMap<>();
  private Map<String, List<BigDecimal>> priceForVenueInWeekdays = new HashMap<>();
  private Map<String, List<BigDecimal>> priceForVenueInWeekend = new HashMap<>();
  private List<String> venues = new ArrayList<>();
  private Map<String, List<BigDecimal>> priceForLahanParkirInWeekend = new HashMap<>();
}
