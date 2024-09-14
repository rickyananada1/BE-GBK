package com.dev.gbk.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Occupancy {
  private Double occFisik;
  private Double occPKBLUHari;
  private Double occMaintenance;
  private Double occRetail;
  private Double occTimnas;

}
