package com.neil.cashbook.controller;

import java.math.BigDecimal;

import com.neil.cashbook.bo.GlobalResult;
import com.neil.cashbook.service.QuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuotaApi {

    @Autowired
    private QuotaService quotaService;

    @GetMapping("quota")
    public GlobalResult<BigDecimal> getQuota() {
        return GlobalResult.of(quotaService.getQuota());
    }

    @PutMapping("quota")
    public GlobalResult<BigDecimal> setQuota(@RequestBody String quota) {
        return GlobalResult.of(quotaService.setQuota(quota));
    }
}
