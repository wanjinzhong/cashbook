package com.neil.cashbook.controller;


import java.util.List;

import com.neil.cashbook.auth.AuthRequired;
import com.neil.cashbook.bo.CashBo;
import com.neil.cashbook.bo.GlobalResult;
import com.neil.cashbook.enums.CashRange;
import com.neil.cashbook.service.CashService;
import com.neil.cashbook.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/api")
@AuthRequired
public class AnalyzeApi {

    @Autowired
    private CashService cashService;

    @GetMapping("cashHeader")
    public GlobalResult<List<CashBo>> getCashHeaderByRange(@RequestParam CashRange range, @RequestParam String date) {
        return GlobalResult.of(cashService.getCashHeaderByRange(range, DateUtil.toDate(date)));
    }

}
