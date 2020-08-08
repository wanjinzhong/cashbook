package com.neil.cashbook.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.neil.cashbook.bo.CashBo;
import com.neil.cashbook.bo.CashDetailBo;
import com.neil.cashbook.bo.GlobalResult;
import com.neil.cashbook.bo.EditCashBo;
import com.neil.cashbook.enums.CommonStatus;
import com.neil.cashbook.service.CashService;
import com.neil.cashbook.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/api")
public class CashApi {

    @Autowired
    private CashService cashService;

    @PostMapping("cash")
    public GlobalResult<CommonStatus> createNewCash(@RequestBody EditCashBo cashBo) {
        cashService.createNewCash(cashBo);
        return GlobalResult.of(CommonStatus.SUCCESS);
    }

    @GetMapping("cash")
    public GlobalResult<List<CashDetailBo>> getCashDetailByDay(@RequestParam String date) {
        return GlobalResult.of(cashService.getCashDetailByDay(DateUtil.toDate(date)));
    }

    @GetMapping("cashHeader")
    public GlobalResult<List<CashBo>> getCashHeaderByMonth(@RequestParam String date) {
        return GlobalResult.of(cashService.getCashHeaderByMonth(DateUtil.toDate(date)));
    }

    @GetMapping("cashHeader/today")
    public GlobalResult<CashBo> getCashHeaderToday() {
        return GlobalResult.of(cashService.getCashHeaderToday());
    }

    @GetMapping("remain")
    public GlobalResult<BigDecimal> getRemain() {
        return GlobalResult.of(cashService.getRemain());
    }
}
