package com.neil.cashbook.bo;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditDreamBo {
    private String title;
    private String desc;
    private String expCost;
    private LocalDate deadline;
}
