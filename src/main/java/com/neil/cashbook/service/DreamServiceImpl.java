package com.neil.cashbook.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neil.cashbook.bo.DreamBo;
import com.neil.cashbook.bo.DreamPicBo;
import com.neil.cashbook.bo.EditDreamBo;
import com.neil.cashbook.dao.entity.CashHeader;
import com.neil.cashbook.dao.entity.Dream;
import com.neil.cashbook.dao.entity.DreamPic;
import com.neil.cashbook.dao.repository.CashHeaderRepository;
import com.neil.cashbook.dao.repository.DreamPicRepository;
import com.neil.cashbook.dao.repository.DreamRepository;
import com.neil.cashbook.enums.DreamType;
import com.neil.cashbook.exception.BizException;
import com.neil.cashbook.util.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private CosService cosService;

    @Autowired
    private DreamPicRepository dreamPicRepository;

    @Override
    @Transactional
    public void createNewDream(EditDreamBo dreamBo) {
        BigDecimal cost = BigDecimalUtil.toBigDecimal(dreamBo.getExpCost());
        Assert.notNull(cost, "预估花费不正确");
        if (dreamBo.getDeadline() != null && dreamBo.getDeadline().isBefore(LocalDate.now())) {
            throw new BizException("截止时间不正确");
        }
        Dream dream = new Dream();
        dream.setOwner(dreamBo.getOwnerId());
        dream.setDeadline(dreamBo.getDeadline());
        dream.setEntryDatetime(LocalDateTime.now());
        dream.setEntryUser(userService.getCurrentUser());
        dream.setExpCost(cost);
        dream.setOwner(Integer.valueOf(0).equals(dreamBo.getOwnerId()) ? null : dreamBo.getOwnerId());
        dream.setTitle(dreamBo.getTitle());
        dreamRepository.save(dream);
    }

    @Override
    @Transactional
    public void updateDream(Integer id, EditDreamBo dreamBo) {
        Dream dream = dreamRepository.findById(id).orElseThrow(() -> new BizException("心愿不存在"));
        BigDecimal cost = BigDecimalUtil.toBigDecimal(dreamBo.getExpCost());
        if (cost == null) {
            throw new BizException("预估花费不正确");
        }
        if (dreamBo.getDeadline() != null && dreamBo.getDeadline().isBefore(LocalDate.now())) {
            throw new BizException("截止时间不正确");
        }
        dream.setDeadline(dreamBo.getDeadline());
        if (dreamBo.isComeTrue()) {
            Assert.notNull(dreamBo.getComeTrueDate(), "心愿实现日期不能为空");
            dream.setComeTrueDate(dreamBo.getComeTrueDate());
            dream.setNotes(dreamBo.getNotes());
        }
        dream.setEntryDatetime(LocalDateTime.now());
        dream.setEntryUser(userService.getCurrentUser());
        dream.setExpCost(cost);
        dream.setTitle(dreamBo.getTitle());
    }

    @Override
    @Transactional
    public Integer uploadPic(Integer id, MultipartFile file) {
        Dream dream = dreamRepository.findById(id).orElseThrow(() -> new BizException("心愿不存在"));
        String prefix = "dream/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String key = cosService.putObject(file, prefix);
        DreamPic dreamPic = new DreamPic();
        dreamPic.setDream(dream);
        dreamPic.setCosKey(key);
        dreamPic.setCosKeySmall(key);
        dreamPicRepository.save(dreamPic);
        return dreamPic.getId();
    }

    @Override
    @Transactional
    public void unComeTrue(Integer id) {
        Dream dream = dreamRepository.findById(id).orElseThrow(() -> new BizException("心愿不存在"));
        LocalDate date = dream.getComeTrueDate();
        if (date == null) {
            throw new BizException("心愿未实现");
        }
        BigDecimal actCost = dream.getActCost();
        dream.setComeTrueDate(null);
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
    public List<DreamBo> getDreams(DreamType type) {
        List<Dream> dreams;
        switch (type) {
            case ALL:
                dreams = dreamRepository.findAll();
                break;
            case COMPLETED:
                dreams = dreamRepository.findByComeTrueDateIsNotNull();
                break;
            case UNCOMPLETED:
                dreams = dreamRepository.findByComeTrueDateIsNull();
                break;
            default:
                dreams = new ArrayList<>();
        }
        return dreams.stream().map(this::toDreamBo).collect(Collectors.toList());
    }

    @Override
    public void deletePic(Integer picId) {
        DreamPic dreamPic = dreamPicRepository.findById(picId).orElseThrow(() -> new BizException("照片不存在"));
        cosService.deleteObject(dreamPic.getCosKey());
        if (!Objects.equals(dreamPic.getCosKey(), dreamPic.getCosKeySmall())) {
            cosService.deleteObject(dreamPic.getCosKeySmall());
        }
    }

    private DreamBo toDreamBo(Dream dream) {
        DreamBo dreamBo = new DreamBo();
        dreamBo.setId(dream.getId());
        dreamBo.setOwner(dream.getOwner());
        dreamBo.setActCost(dream.getActCost());
        dreamBo.setComeTrueDate(dream.getComeTrueDate());
        dreamBo.setDeadline(dream.getDeadline());
        dreamBo.setNotes(dream.getNotes());
        dreamBo.setEntryDatetime(dream.getEntryDatetime());
        dreamBo.setEntryUser(dream.getEntryUser().getName());
        dreamBo.setExpCost(dream.getExpCost());
        dreamBo.setTitle(dream.getTitle());
        dreamBo.setPics(dream.getPics().stream().map(pic -> {
            DreamPicBo picBo = new DreamPicBo();
            picBo.setDreamId(dream.getId());
            picBo.setPicId(pic.getId());
            picBo.setUrlSmall(cosService.generatePresignedUrl(pic.getCosKeySmall()));
            picBo.setUrlSmall(cosService.generatePresignedUrl(pic.getCosKey()));
            return picBo;
        }).collect(Collectors.toList()));
        return dreamBo;
    }
}
