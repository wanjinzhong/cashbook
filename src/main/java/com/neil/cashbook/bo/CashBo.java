package com.neil.cashbook.bo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.neil.cashbook.enums.CashAnalyze;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CashBo {
    private Integer headerId;
    private BigDecimal quota;
    private BigDecimal cost;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String date;
    List<CashDetailBo> cashDetail = new ArrayList<>();

    public String getCashAnalyze() {
        return cost.compareTo(quota) > 0 ? CashAnalyze.OVER_COST.getDisplay() : CashAnalyze.UNDER_COST.getDisplay();
    }

    public BigDecimal getAnalyzeCost() {
        return cost.subtract(quota).abs();
    }
}
