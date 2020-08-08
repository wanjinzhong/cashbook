package com.neil.cashbook.bo;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComeTrueBo {
    private String note;
    private String cost;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
