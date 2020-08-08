package com.neil.cashbook.bo;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditDreamBo {
    private String title;
    private String desc;
    private String expCost;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;
}
