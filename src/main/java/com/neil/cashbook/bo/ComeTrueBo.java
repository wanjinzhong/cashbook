package com.neil.cashbook.bo;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComeTrueBo {
    private String note;
    private String cost;
    private LocalDate date;
}
