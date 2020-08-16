package com.neil.cashbook.service;
import java.util.List;

import com.neil.cashbook.bo.DreamBo;
import com.neil.cashbook.bo.DreamPicBo;
import com.neil.cashbook.bo.EditDreamBo;
import com.neil.cashbook.enums.DreamType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface DreamService {
    void createNewDream(EditDreamBo dreamBo);

    void updateDream(Integer id, EditDreamBo dreamBo);

    @Transactional
    Integer uploadPic(Integer id, MultipartFile file);

    void unComeTrue(Integer id);

    void deleteDream(Integer id);

    List<DreamBo> getDreams(DreamType type);

    void deletePic(Integer picId);

    DreamBo getDreamById(Integer dreamId);

    List<DreamPicBo> getDreamPic(Integer dreamId);
}
