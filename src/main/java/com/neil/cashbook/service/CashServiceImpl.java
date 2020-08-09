package com.neil.cashbook.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.neil.cashbook.bo.CashBo;
import com.neil.cashbook.bo.CashDetailBo;
import com.neil.cashbook.bo.EditCashBo;
import com.neil.cashbook.dao.entity.CashDetail;
import com.neil.cashbook.dao.entity.CashHeader;
import com.neil.cashbook.dao.repository.CashDetailRepository;
import com.neil.cashbook.dao.repository.CashHeaderRepository;
import com.neil.cashbook.dao.repository.DreamRepository;
import com.neil.cashbook.enums.CashType;
import com.neil.cashbook.exception.BizException;
import com.neil.cashbook.util.BigDecimalUtil;
import com.neil.cashbook.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private DreamRepository dreamRepository;

    @Override
    public void createNewCash(EditCashBo cashBo) {
        BigDecimal cost = BigDecimalUtil.toBigDecimal(cashBo.getCost());
        if (cost == null) {
            throw new BizException("消费金额不正确，只接受数字");
        }
        LocalDate date = DateUtil.toDate(cashBo.getDate());
        CashHeader cashHeader = getOrCreateHeader(date);
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
    @Transactional
    public CashHeader getOrCreateHeader(LocalDate date) {
        CashHeader cashHeader = cashHeaderRepository.findByCashDate(DateUtil.toDate(date));
        if (cashHeader == null) {
            cashHeader = new CashHeader();
            cashHeader.setCashDate(Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            cashHeader.setQuota(quotaService.getQuota());
            cashHeader.setCost(BigDecimal.ZERO);
            cashHeaderRepository.save(cashHeader);
        }
        return cashHeader;
    }

    @Override
    @Transactional
    public void deleteCashDetail(Integer detailId) {
        CashDetail cashDetail = cashDetailRepository.findById(detailId).orElseThrow(() -> new BizException("账单不存在"));
        CashHeader cashHeader = cashDetail.getHeader();
        cashHeader.setCost(cashHeader.getCost().subtract(cashDetail.getCost()));
        cashHeaderRepository.save(cashHeader);
        cashDetailRepository.delete(cashDetail);
    }

    @Override
    public List<CashDetailBo> getCashDetailByDay(LocalDate date) {
        if (date == null) {
            throw new BizException("请指定日期");
        }
        CashHeader cashHeader = cashHeaderRepository.findByCashDate(DateUtil.toDate(date));
        if (cashHeader == null) {
            return new ArrayList<>();
        }
        List<CashDetailBo> cashDetails = cashHeader.getDetails().stream().map(detail -> {
            CashDetailBo cashDetailBo = new CashDetailBo();
            cashDetailBo.setType(CashType.CASH);
            cashDetailBo.setCashDate(cashHeader.getDate());
            cashDetailBo.setCost(detail.getCost().toString());
            cashDetailBo.setDetailId(detail.getId());
            cashDetailBo.setEntryDatetime(detail.getEntryDatetime());
            cashDetailBo.setHeaderId(cashHeader.getId());
            cashDetailBo.setNote(detail.getNotes());
            cashDetailBo.setAvatar(detail.getEntryUser().getAvatar());
            cashDetailBo.setUserName(detail.getEntryUser().getName());
            return cashDetailBo;
        }).collect(Collectors.toList());
        cashDetails.addAll(dreamRepository.findByCometrue(date).stream().map(dream -> {
            CashDetailBo cashDetailBo = new CashDetailBo();
            cashDetailBo.setType(CashType.DREAM);
            cashDetailBo.setCashDate(cashHeader.getDate());
            cashDetailBo.setCost(dream.getActCost().toString());
            cashDetailBo.setDetailId(dream.getId());
            cashDetailBo.setEntryDatetime(dream.getEntryDatetime());
            cashDetailBo.setHeaderId(cashHeader.getId());
            cashDetailBo.setNote(dream.getTitle());
            cashDetailBo.setAvatar(dream.getEntryUser().getAvatar());
            cashDetailBo.setUserName(dream.getEntryUser().getName());
            return cashDetailBo;
        }).collect(Collectors.toList()));
        return cashDetails;
    }

    @Override
    public List<CashBo> getCashHeaderByMonth(LocalDate date) {
        LocalDate from = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate to = date.with(TemporalAdjusters.lastDayOfMonth());
        List<CashHeader> headers = cashHeaderRepository.findByDateRange(DateUtil.toDate(from), DateUtil.toDate(to));
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
        CashHeader header = cashHeaderRepository.findByCashDate(DateUtil.toDate(today));
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
