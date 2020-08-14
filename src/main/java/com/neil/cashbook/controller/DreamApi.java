package com.neil.cashbook.controller;

import java.util.List;

import com.neil.cashbook.auth.AuthRequired;
import com.neil.cashbook.bo.DreamBo;
import com.neil.cashbook.bo.EditDreamBo;
import com.neil.cashbook.bo.GlobalResult;
import com.neil.cashbook.enums.CommonStatus;
import com.neil.cashbook.enums.DreamType;
import com.neil.cashbook.service.DreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("public/api")
@AuthRequired
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

    @PutMapping("dream/{dreamId}/unComeTure")
    public GlobalResult<CommonStatus> unComeTrue(@PathVariable("dreamId") Integer id) {
        dreamService.unComeTrue(id);
        return GlobalResult.of(CommonStatus.SUCCESS);
    }

    @DeleteMapping("dream/{dreamId}")
    public GlobalResult<CommonStatus> deleteDream(@PathVariable("dreamId") Integer id) {
        dreamService.deleteDream(id);
        return GlobalResult.of(CommonStatus.SUCCESS);
    }

    @GetMapping("dream")
    public GlobalResult<List<DreamBo>> getDreams(@RequestParam("type") DreamType type) {
        return GlobalResult.of(dreamService.getDreams(type));
    }

    @PostMapping("dream/{dreamId}/pic")
    public GlobalResult<Integer> uploadDreamPic(MultipartFile file, @PathVariable(name = "dreamId") Integer dreamId) {
        return GlobalResult.of(dreamService.uploadPic(dreamId, file));
    }

    @DeleteMapping("dream/pic/{picId}")
    public void deleteDreamPic(@PathVariable(name = "picId") Integer picId) {
        dreamService.deletePic(picId);
    }
}
