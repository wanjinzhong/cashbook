package com.neil.cashbook.service;
import java.util.List;

import com.neil.cashbook.bo.ComeTrueBo;
import com.neil.cashbook.bo.DreamBo;
import com.neil.cashbook.bo.EditDreamBo;

public interface DreamService {
    void createNewDream(EditDreamBo dreamBo);

    void updateDream(Integer id, EditDreamBo dreamBo);

    void comeTrue(Integer id, ComeTrueBo comeTrueBo);

    void unComeTrue(Integer id);

    void deleteDream(Integer id);

    List<DreamBo> getDreams();
}
