package com.neil.cashbook.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.neil.cashbook.bo.AnalyzeBo;
import com.neil.cashbook.bo.CashBo;
import com.neil.cashbook.bo.CashDetailBo;
import com.neil.cashbook.bo.EditCashBo;
import com.neil.cashbook.dao.entity.CashDetail;
import com.neil.cashbook.dao.entity.CashHeader;
import com.neil.cashbook.dao.repository.CashDetailRepository;
import com.neil.cashbook.dao.repository.CashHeaderRepository;
import com.neil.cashbook.dao.repository.DreamRepository;
import com.neil.cashbook.enums.CashRange;
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
        LocalDate date = DateUtil.toLocalDate(cashBo.getDate());
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
    public CashBo getCashByDay(LocalDate date) {
        if (date == null) {
            throw new BizException("请指定日期");
        }
        CashHeader cashHeader = getOrCreateHeader(date);
        CashBo cashBo = new CashBo();
        cashBo.setHeaderId(cashHeader.getId());
        cashBo.setQuota(cashHeader.getQuota());
        cashBo.setCost(cashHeader.getCost());
        cashBo.setDate(DateUtil.toString(cashHeader.getDate()));
        cashBo.setCashDetail(cashHeader.getDetails().stream().map(detail -> {
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
        }).collect(Collectors.toList()));
        cashBo.getCashDetail().addAll(dreamRepository.findByComeTrueDate(date).stream().map(dream -> {
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
        return cashBo;
    }

    @Override
    public AnalyzeBo getCashHeaderByRange(CashRange range, Integer weekAgo, LocalDate date) {
        LocalDate from;
        LocalDate to;
        switch (range) {
            case BY_WEEK:
                LocalDate now = LocalDate.now();
                from = now.with(DayOfWeek.MONDAY).minusDays(7 * weekAgo);
                to = now.with(DayOfWeek.SUNDAY).minusDays(7 * weekAgo);
                return getCashHeaderGroupByDay(from, to);
            case BY_MONTH:
                from = date.with(TemporalAdjusters.firstDayOfMonth());
                to = date.with(TemporalAdjusters.lastDayOfMonth());
                return getCashHeaderGroupByDay(from, to);
            case BY_YEAR:
                from = date.with(TemporalAdjusters.firstDayOfYear());
                to = date.with(TemporalAdjusters.lastDayOfYear());
                return getCashHeaderGroupByMonth(from, to);
            default:
                return new AnalyzeBo();
        }
    }

    public AnalyzeBo getCashHeaderGroupByDay(LocalDate from, LocalDate to) {
        List<CashHeader> headers = cashHeaderRepository.findByDateRange(DateUtil.toDate(from), DateUtil.toDate(to));
        AnalyzeBo analyzeBo = new AnalyzeBo();
        analyzeBo.setDateRange(DateUtil.toString(from) + " ~ " + DateUtil.toString(to));
        analyzeBo.setCost(headers.stream().map(CashHeader::getCost).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
        analyzeBo.setQuota(headers.stream().map(CashHeader::getQuota).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
        analyzeBo.setCashes(headers.stream().map(this::toCashBo).collect(Collectors.toList()));
        return analyzeBo;
    }

    public AnalyzeBo getCashHeaderGroupByMonth(LocalDate from, LocalDate to) {
        List<CashHeader> headers = cashHeaderRepository.findByDateRange(DateUtil.toDate(from), DateUtil.toDate(to));
        AnalyzeBo analyzeBo = new AnalyzeBo();
        analyzeBo.setDateRange(DateUtil.toString(from) + " ~ " + DateUtil.toString(to));
        analyzeBo.setQuota(headers.stream().map(CashHeader::getQuota).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
        analyzeBo.setCost(headers.stream().map(CashHeader::getCost).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
        Map<LocalDate, List<CashHeader>> grouped = headers.stream().collect(Collectors.groupingBy(h -> h.getDate().with(TemporalAdjusters.firstDayOfMonth())));
        List<CashBo> cashBos = new ArrayList<>();
        grouped.forEach((k, v) -> {
            CashBo cashBo = new CashBo();
            cashBo.setDate(
                DateUtil.toStringWithoutDay(k));
            cashBo.setCost(v.stream().map(CashHeader::getCost).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
            cashBo.setQuota(v.stream().map(CashHeader::getQuota).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
            cashBos.add(cashBo);
        });
        analyzeBo.setCashes(cashBos);
        return analyzeBo;
    }

    private CashBo toCashBo(CashHeader header) {
        CashBo cashBo = new CashBo();
        cashBo.setHeaderId(header.getId());
        cashBo.setQuota(header.getQuota());
        cashBo.setCost(header.getCost());
        cashBo.setDate(DateUtil.toString(header.getDate()));
        return cashBo;
    }

    @Override
    public BigDecimal getRemain() {
        return cashHeaderRepository.findRemain(new Date());
    }

    @Override
    public CashBo getCashHeaderToday() {
        LocalDate today = LocalDate.now();
        CashHeader header = getOrCreateHeader(today);
        return toCashBo(header);
    }

    @Override
    @Transactional
    public void updateCost(LocalDate date, BigDecimal cost) {
        CashHeader header = getOrCreateHeader(date);
        header.setCost(header.getCost().add(cost));
        cashHeaderRepository.save(header);
    }
}
