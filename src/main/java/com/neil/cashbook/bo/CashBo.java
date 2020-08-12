package com.neil.cashbook.bo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CashBo {
    private Integer headerId;
    private BigDecimal quota;
    private BigDecimal cost;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    List<CashDetailBo> cashDetail = new ArrayList<>();
}
