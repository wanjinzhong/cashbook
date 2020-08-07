package com.neil.cashbook.bo;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CashDetailBo {
    private Integer headerId;
    private Integer detailId;
    private String cost;
    private String note;
    private LocalDate cashDate;
    private String userName;
    private LocalDateTime entryDatetime;
}
