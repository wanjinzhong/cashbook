package com.neil.cashbook.service;
import java.math.BigDecimal;

import com.neil.cashbook.bo.EditQuotaBo;

public interface QuotaService {
    BigDecimal getQuota();

    BigDecimal setQuota(EditQuotaBo quotaBo);
}
