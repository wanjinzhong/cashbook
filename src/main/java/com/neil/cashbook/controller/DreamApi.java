package com.neil.cashbook.controller;

import java.util.List;

import com.neil.cashbook.bo.ComeTrueBo;
import com.neil.cashbook.bo.DreamBo;
import com.neil.cashbook.bo.EditDreamBo;
import com.neil.cashbook.bo.GlobalResult;
import com.neil.cashbook.enums.CommonStatus;
import com.neil.cashbook.service.DreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/api")
public class DreamApi {

    @Autowired
    private DreamService dreamService;

    @PostMapping("dream")
    public GlobalResult<CommonStatus> createNewDream(@RequestBody EditDreamBo dreamBo) {
        dreamService.createNewDream(dreamBo);
        return GlobalResult.of(CommonStatus.SUCCESS);
    }

    @PutMapping("dream/{dreamId}")
    public GlobalResult<CommonStatus> updateDream(@PathVariable("dreamId") Integer id, @RequestBody EditDreamBo dreamBo) {
        dreamService.updateDream(id, dreamBo);
        return GlobalResult.of(CommonStatus.SUCCESS);
    }

    @PutMapping("dream/{dreamId}/comeTure")
    public GlobalResult<CommonStatus> comeTrue(@PathVariable("dreamId") Integer id, @RequestBody ComeTrueBo comeTrueBo) {
        dreamService.comeTrue(id, comeTrueBo);
        return GlobalResult.of(CommonStatus.SUCCESS);
    }

    @PutMapping("dream/{dreamId}/unComeTure")
    public GlobalResult<CommonStatus> unComeTrue(Integer id) {
        dreamService.unComeTrue(id);
        return GlobalResult.of(CommonStatus.SUCCESS);
    }

    @DeleteMapping("dream/{dreamId}")
    public GlobalResult<CommonStatus> deleteDream(Integer id) {
        dreamService.deleteDream(id);
        return GlobalResult.of(CommonStatus.SUCCESS);
    }

    @GetMapping("dream")
    public GlobalResult<List<DreamBo>> getDreams() {
        return GlobalResult.of(dreamService.getDreams());
    }
}
