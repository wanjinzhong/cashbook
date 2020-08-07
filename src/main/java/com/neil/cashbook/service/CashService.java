package com.neil.cashbook.service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.neil.cashbook.bo.CashBo;
import com.neil.cashbook.bo.CashDetailBo;
import com.neil.cashbook.bo.EditCashBo;
import com.neil.cashbook.dao.entity.CashHeader;

public interface CashService {
    void createNewCash(EditCashBo cashBo);

    CashHeader getCashHeader(LocalDate today);

    void deleteCashDetail(Integer detailId);

    List<CashDetailBo> getCashDetailByDay(LocalDate date);

    List<CashBo> getCashHeaderByMonth(LocalDate date);

    BigDecimal getRemain();

    CashBo getCashHeaderToday();
}
