package com.neil.cashbook.controller;

import java.math.BigDecimal;
import java.util.List;

import com.neil.cashbook.auth.AuthRequired;
import com.neil.cashbook.bo.CashBo;
import com.neil.cashbook.bo.GlobalResult;
import com.neil.cashbook.bo.EditCashBo;
import com.neil.cashbook.enums.CommonStatus;
import com.neil.cashbook.service.CashService;
import com.neil.cashbook.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/api")
@AuthRequired
public class CashApi {

    @Autowired
    private CashService cashService;

    @PostMapping("cash")
    public GlobalResult<CommonStatus> createNewCash(@RequestBody EditCashBo cashBo) {
        cashService.createNewCash(cashBo);
        return GlobalResult.of(CommonStatus.SUCCESS);
    }

    @GetMapping("cash")
    public GlobalResult<CashBo> getCashDetailByDay(@RequestParam String date) {
        return GlobalResult.of(cashService.getCashByDay(DateUtil.toDate(date)));
    }

    @GetMapping("remain")
    public GlobalResult<BigDecimal> getRemain() {
        return GlobalResult.of(cashService.getRemain());
    }

    @DeleteMapping("cashDetail/{detailId}")
    public GlobalResult<CommonStatus> deleteCashDetail(@PathVariable("detailId") Integer detailId) {
        cashService.deleteCashDetail(detailId);
        return GlobalResult.of(CommonStatus.SUCCESS);
    }
}
