package com.neil.cashbook.service;
import java.math.BigDecimal;

public interface QuotaService {
    BigDecimal getQuota();

    BigDecimal setQuota(String quotaStr);
}
