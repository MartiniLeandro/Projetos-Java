package com.money_track.demo.entities.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface LaunchInterface {
    Long getId();
    String getDescription();
    BigDecimal getValue();
    LocalDate getDate();
    String getCategory();
    String getTypeValue();
}
