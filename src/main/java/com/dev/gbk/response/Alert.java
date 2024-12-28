package com.dev.gbk.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Alert {
  private String unit;
  private String venue;
  private String tanggal;
}
