package com.neil.cashbook.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.neil.cashbook.bo.EditQuotaBo;
import com.neil.cashbook.dao.entity.CashHeader;
import com.neil.cashbook.dao.entity.Setting;
import com.neil.cashbook.dao.repository.CashHeaderRepository;
import com.neil.cashbook.dao.repository.SettingRepository;
import com.neil.cashbook.enums.SettingKey;
import com.neil.cashbook.exception.BizException;
import com.neil.cashbook.util.BigDecimalUtil;
import com.neil.cashbook.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class QuotaServiceImpl implements QuotaService {

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private CashHeaderRepository cashHeaderRepository;

    @Override
    @Transactional
    public BigDecimal getQuota() {
        Setting setting = settingRepository.findByKey(SettingKey.QUOTA_PER_DAY);
        BigDecimal quota;
        if (setting == null) {
            setting = new Setting();
            setting.setKey(SettingKey.QUOTA_PER_DAY);
            setting.setValue("50");
            settingRepository.save(setting);
        }
        quota = BigDecimalUtil.toBigDecimal(setting.getValue());
        if (quota == null) {
            log.error("配额配置有误，恢复至默认50元。");
            setting.setValue("50");
            settingRepository.save(setting);
        }
        return quota;
    }

    @Override
    @Transactional
    public BigDecimal setQuota(EditQuotaBo quotaBo) {
        BigDecimal quota = BigDecimalUtil.toBigDecimal(quotaBo.getQuota());
        if (quota == null) {
            throw new BizException("每日配额不正确，只接受数字");
        }
        Setting setting = settingRepository.findByKey(SettingKey.QUOTA_PER_DAY);
        if (setting == null) {
            setting = new Setting();
            setting.setKey(SettingKey.QUOTA_PER_DAY);
        }
        setting.setValue(quota.toString());
        settingRepository.save(setting);
        CashHeader cashHeader = cashHeaderRepository.findByCashDate(DateUtil.toDate(LocalDate.now()));
        if (cashHeader != null) {
            cashHeader.setQuota(quota);
            cashHeaderRepository.save(cashHeader);
        }
        return quota;
    }
}
