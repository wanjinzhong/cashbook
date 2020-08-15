package com.neil.cashbook.schedule;

import java.time.LocalDate;

import com.neil.cashbook.dao.entity.CashHeader;
import com.neil.cashbook.dao.repository.CashHeaderRepository;
import com.neil.cashbook.service.CashService;
import com.neil.cashbook.service.QuotaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CashSchedule {

    @Autowired
    private CashService cashService;

    @Autowired
    private CashHeaderRepository cashHeaderRepository;

    @Autowired
    private QuotaService quotaService;

    @Scheduled(cron = "5 0 0 * * ?")
    public void initCashHeader() {
        LocalDate today = LocalDate.now();
        CashHeader cashHeader = cashService.getOrCreateHeader(today);
        cashHeader.setQuota(quotaService.getQuota());
        cashHeaderRepository.save(cashHeader);
        log.info("Init header for " + today);
    }
}
