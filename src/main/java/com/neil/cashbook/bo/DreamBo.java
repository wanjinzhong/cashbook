package com.neil.cashbook.bo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DreamBo {
    private Integer id;
    private String title;
    private String desc;
    private BigDecimal expCost;
    private BigDecimal actCost;
    private LocalDate comeTrue;
    private LocalDate deadline;
    private String entryUser;
    private LocalDateTime entryDatetime;

}
