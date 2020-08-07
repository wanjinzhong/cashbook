package com.neil.cashbook.bo;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CashBo {
    private Integer headerId;
    private BigDecimal quota;
    private BigDecimal cost;
    private LocalDate date;
}
