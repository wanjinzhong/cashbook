package com.neil.cashbook.schedule;

import java.time.LocalDate;

import com.neil.cashbook.service.CashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CashSchedule {

    @Autowired
    private CashService cashService;

    @Scheduled(cron = "5 0 0 * * ?")
    public void initCashHeader() {
        LocalDate today = LocalDate.now();
        cashService.getOrCreateHeader(today);
        log.info("Init header for " + today);
    }
}
