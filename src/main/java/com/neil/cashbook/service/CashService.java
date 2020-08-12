package com.neil.cashbook.service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.neil.cashbook.bo.CashBo;
import com.neil.cashbook.bo.CashDetailBo;
import com.neil.cashbook.bo.EditCashBo;
import com.neil.cashbook.dao.entity.CashHeader;
import org.springframework.transaction.annotation.Transactional;

public interface CashService {
    void createNewCash(EditCashBo cashBo);

    CashHeader getOrCreateHeader(LocalDate date);

    void deleteCashDetail(Integer detailId);

    CashBo getCashByDay(LocalDate date);

    List<CashBo> getCashHeaderByMonth(LocalDate date);

    BigDecimal getRemain();

    CashBo getCashHeaderToday();
}
