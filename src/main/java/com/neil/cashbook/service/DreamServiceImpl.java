package com.neil.cashbook.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.neil.cashbook.bo.ComeTrueBo;
import com.neil.cashbook.bo.DreamBo;
import com.neil.cashbook.bo.EditDreamBo;
import com.neil.cashbook.dao.entity.CashHeader;
import com.neil.cashbook.dao.entity.Dream;
import com.neil.cashbook.dao.repository.CashHeaderRepository;
import com.neil.cashbook.dao.repository.DreamRepository;
import com.neil.cashbook.exception.BizException;
import com.neil.cashbook.util.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DreamServiceImpl implements DreamService {

    @Autowired
    private DreamRepository dreamRepository;

    @Autowired
    private CashService cashService;

    @Autowired
    private UserService userService;

    @Autowired
    private CashHeaderRepository cashHeaderRepository;

    @Override
    @Transactional
    public void createNewDream(EditDreamBo dreamBo) {
        BigDecimal cost = BigDecimalUtil.toBigDecimal(dreamBo.getExpCost());
        if (cost == null) {
            throw new BizException("预估花费不正确");
        }
        if (dreamBo.getDeadline() != null && dreamBo.getDeadline().isBefore(LocalDate.now())) {
            throw new BizException("截止时间不正确");
        }
        Dream dream = new Dream();
        dream.setDeadline(dreamBo.getDeadline());
        dream.setDesc(dreamBo.getDesc());
        dream.setEntryDatetime(LocalDateTime.now());
        dream.setEntryUser(userService.getCurrentUser());
        dream.setExpCost(cost);
        dream.setTitle(dreamBo.getTitle());
        dreamRepository.save(dream);
    }

    @Override
    @Transactional
    public void updateDream(Integer id, EditDreamBo dreamBo) {
        Dream dream = dreamRepository.findById(id).orElseThrow(() -> new BizException("心愿不存在"));
        if (dream.getCometrue() != null) {
            throw new BizException("心愿已实现，不能更改");
        }
        BigDecimal cost = BigDecimalUtil.toBigDecimal(dreamBo.getExpCost());
        if (cost == null) {
            throw new BizException("预估花费不正确");
        }
        if (dreamBo.getDeadline() != null && dreamBo.getDeadline().isBefore(LocalDate.now())) {
            throw new BizException("截止时间不正确");
        }
        dream.setDeadline(dreamBo.getDeadline());
        dream.setDesc(dreamBo.getDesc());
        dream.setEntryDatetime(LocalDateTime.now());
        dream.setEntryUser(userService.getCurrentUser());
        dream.setExpCost(cost);
        dream.setTitle(dreamBo.getTitle());
    }

    @Override
    @Transactional
    public void comeTrue(Integer id, ComeTrueBo comeTrueBo) {
        Dream dream = dreamRepository.findById(id).orElseThrow(() -> new BizException("心愿不存在"));
        if (dream.getCometrue() != null) {
            throw new BizException("心愿已实现");
        }
        BigDecimal cost = BigDecimalUtil.toBigDecimal(comeTrueBo.getCost());
        if (cost == null) {
            throw new BizException("预估花费不正确");
        }
        LocalDate date = comeTrueBo.getDate() == null ? LocalDate.now() : comeTrueBo.getDate();
        dream.setCometrue(date);
        dream.setComeTrueNote(comeTrueBo.getNote());
        dream.setActCost(cost);
        dreamRepository.save(dream);
        CashHeader cashHeader = cashService.getOrCreateHeader(date);
        BigDecimal originCost = cashHeader.getCost() == null ? BigDecimal.ZERO : cashHeader.getCost();
        cashHeader.setCost(originCost.add(cost));
        cashHeaderRepository.save(cashHeader);
    }

    @Override
    @Transactional
    public void unComeTrue(Integer id) {
        Dream dream = dreamRepository.findById(id).orElseThrow(() -> new BizException("心愿不存在"));
        LocalDate date = dream.getCometrue();
        if (date == null) {
            throw new BizException("心愿未实现");
        }
        BigDecimal actCost = dream.getActCost();
        dream.setActCost(null);
        dream.setComeTrueNote(null);
        dream.setCometrue(null);
        dreamRepository.save(dream);
        CashHeader cashHeader = cashService.getOrCreateHeader(date);
        BigDecimal originCost = cashHeader.getCost() == null ? BigDecimal.ZERO : cashHeader.getCost();
        cashHeader.setCost(originCost.subtract(actCost));
        cashHeaderRepository.save(cashHeader);
    }

    @Override
    @Transactional
    public void deleteDream(Integer id) {
        Dream dream = dreamRepository.findById(id).orElseThrow(() -> new BizException("心愿不存在"));
        dreamRepository.delete(dream);
    }

    @Override
    public List<DreamBo> getDreams() {
        List<Dream> dreams = dreamRepository.findAll();
        return dreams.stream().map(this::toDreamBo).collect(Collectors.toList());
    }

    private DreamBo toDreamBo(Dream dream) {
        DreamBo dreamBo = new DreamBo();
        dreamBo.setId(dream.getId());
        dreamBo.setOwner(dream.getOwner());
        dreamBo.setActCost(dream.getActCost());
        dreamBo.setComeTrue(dream.getCometrue());
        dreamBo.setDeadline(dream.getDeadline());
        dreamBo.setDesc(dream.getDesc());
        dreamBo.setEntryDatetime(dream.getEntryDatetime());
        dreamBo.setEntryUser(dream.getEntryUser().getName());
        dreamBo.setExpCost(dream.getExpCost());
        dreamBo.setTitle(dream.getTitle());
        return dreamBo;
    }
}
