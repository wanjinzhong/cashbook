package com.neil.cashbook.bo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.neil.cashbook.enums.CashAnalyze;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalyzeBo {
    private String dateRange;
    private BigDecimal quota = BigDecimal.ZERO;
    private BigDecimal cost = BigDecimal.ZERO;
    private List<CashBo> cashes = new ArrayList<>();

    public String getCashAnalyze() {
        return cost.compareTo(quota) > 0 ? CashAnalyze.OVER_COST.getDisplay() : CashAnalyze.UNDER_COST.getDisplay();
    }

    public BigDecimal getAnalyzeCost() {
        return cost.subtract(quota).abs();
    }
}
