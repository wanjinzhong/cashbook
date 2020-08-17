package com.neil.cashbook.controller;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.neil.cashbook.auth.AuthRequired;
import com.neil.cashbook.bo.AnalyzeBo;
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
    public GlobalResult<AnalyzeBo> getCashHeaderByRange(@RequestParam CashRange range, @RequestParam(required = false) Integer weekAgo, @RequestParam(required = false) String date) {
        date = date.replace(",", "");
        if (Pattern.compile("^\\d{4}$").matcher(date).find()) {
            date += "-01-01";
        } else if (Pattern.compile("^\\d{4}-\\d{2}$").matcher(date).find()) {
            date = date + "-01";
        }
        return GlobalResult.of(cashService.getCashHeaderByRange(range, weekAgo, DateUtil.toLocalDate(date)));
    }
}
