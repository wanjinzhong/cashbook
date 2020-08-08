package com.neil.cashbook.bo;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.neil.cashbook.enums.CashType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CashDetailBo {
    private Integer headerId;
    private Integer detailId;
    private CashType type;
    private String cost;
    private String note;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate cashDate;
    private String userName;
    private String avatar;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalDateTime entryDatetime;


}
