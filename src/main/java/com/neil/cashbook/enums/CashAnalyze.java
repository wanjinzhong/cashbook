package com.neil.cashbook.enums;
import lombok.Getter;

@Getter
public enum CashAnalyze {
    OVER_COST("超支"),
    UNDER_COST("节省");
    String display;
    CashAnalyze(String display) {
        this.display = display;
    }
}
