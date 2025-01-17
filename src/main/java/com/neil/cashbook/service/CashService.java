package com.neil.cashbook.service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.neil.cashbook.bo.AnalyzeBo;
import com.neil.cashbook.bo.CashBo;
import com.neil.cashbook.bo.CashDetailBo;
import com.neil.cashbook.bo.EditCashBo;
import com.neil.cashbook.dao.entity.CashHeader;
import com.neil.cashbook.enums.CashRange;
import org.springframework.transaction.annotation.Transactional;

public interface CashService {
    void createNewCash(EditCashBo cashBo);

    CashHeader getOrCreateHeader(LocalDate date);

    void deleteCashDetail(Integer detailId);

    CashBo getCashByDay(LocalDate date);

    AnalyzeBo getCashHeaderByRange(CashRange cashRange, Integer weekAgo, LocalDate date);

    BigDecimal getRemain();

    CashBo getCashHeaderToday();

    void updateCost(LocalDate date, BigDecimal cost);
}
