package com.neil.cashbook.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

import com.neil.cashbook.bo.CashBo;
import com.neil.cashbook.bo.CashDetailBo;
import com.neil.cashbook.bo.EditCashBo;
import com.neil.cashbook.dao.entity.CashDetail;
import com.neil.cashbook.dao.entity.CashHeader;
import com.neil.cashbook.dao.repository.CashDetailRepository;
import com.neil.cashbook.dao.repository.CashHeaderRepository;
import com.neil.cashbook.exception.BizException;
import com.neil.cashbook.util.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CashServiceImpl implements CashService {

    @Autowired
    private CashHeaderRepository cashHeaderRepository;

    @Autowired
    private QuotaService quotaService;

    @Autowired
    private UserService userService;

    @Autowired
    private CashDetailRepository cashDetailRepository;

    @Override
    public void createNewCash(EditCashBo cashBo) {
        BigDecimal cost = BigDecimalUtil.toBigDecimal(cashBo.getCost());
        if (cost == null) {
            throw new BizException("消费金额不正确，只接受数字");
        }
        LocalDate today = LocalDate.now();
        CashHeader cashHeader = getCashHeader(today);
        BigDecimal originCost = cashHeader.getCost() == null ? BigDecimal.ZERO : cashHeader.getCost();
        cashHeader.setCost(originCost.add(cost));
        cashHeaderRepository.save(cashHeader);
        CashDetail cashDetail = new CashDetail();
        cashDetail.setHeader(cashHeader);
        cashDetail.setCost(cost);
        cashDetail.setNotes(cashBo.getNotes());
        cashDetail.setEntryUser(userService.getCurrentUser());
        cashDetail.setEntryDatetime(LocalDateTime.now());
        cashDetailRepository.save(cashDetail);
    }

    @Override
    public CashHeader getCashHeader(LocalDate today) {
        CashHeader cashHeader = cashHeaderRepository.findByCashDate(today);
        if (cashHeader == null) {
            cashHeader = new CashHeader();
            cashHeader.setCashDate(Date.from(today.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            cashHeader.setQuota(quotaService.getQuota());
        }
        cashHeaderRepository.save(cashHeader);
        return cashHeader;
    }

    @Override
    public void deleteCashDetail(Integer detailId) {
        CashDetail cashDetail = cashDetailRepository.findById(detailId).orElseThrow(() -> new BizException("账单不存在"));
        cashDetailRepository.delete(cashDetail);
    }

    @Override
    public List<CashDetailBo> getCashDetailByDay(LocalDate date) {
        if (date == null) {
            throw new BizException("请指定日期");
        }
        CashHeader cashHeader = cashHeaderRepository.findByCashDate(date);
        return cashHeader.getDetails().stream().map(detail -> {
            CashDetailBo cashDetailBo = new CashDetailBo();
            cashDetailBo.setCashDate(cashHeader.getDate());
            cashDetailBo.setCost(detail.getCost().toString());
            cashDetailBo.setDetailId(detail.getId());
            cashDetailBo.setEntryDatetime(detail.getEntryDatetime());
            cashDetailBo.setHeaderId(cashHeader.getId());
            cashDetailBo.setNote(detail.getNotes());
            cashDetailBo.setUserName(detail.getEntryUser().getName());
            return cashDetailBo;
        }).collect(Collectors.toList());

    }

    @Override
    public List<CashBo> getCashHeaderByMonth(LocalDate date) {
        LocalDate from = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate to = date.with(TemporalAdjusters.lastDayOfMonth());
        List<CashHeader> headers = cashHeaderRepository.findByDateRange(from, to);
        return headers.stream().map(this::toCashBo).collect(Collectors.toList());
    }

    private CashBo toCashBo(CashHeader header) {
        CashBo cashBo = new CashBo();
        cashBo.setHeaderId(header.getId());
        cashBo.setQuota(header.getQuota());
        cashBo.setCost(header.getCost());
        cashBo.setDate(header.getDate());
        return cashBo;
    }

    @Override
    public BigDecimal getRemain() {
        return cashHeaderRepository.findRemain();
    }

    @Override
    public CashBo getCashHeaderToday() {
        LocalDate today = LocalDate.now();
        CashHeader header = cashHeaderRepository.findByCashDate(today);
        if (header == null) {
            header = new CashHeader();
            header.setCashDate(Date.from(today.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            header.setQuota(quotaService.getQuota());
            header.setCost(BigDecimal.ZERO);
            cashHeaderRepository.save(header);
        }
        return toCashBo(header);
    }
}
