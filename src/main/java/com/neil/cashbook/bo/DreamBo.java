package com.neil.cashbook.bo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DreamBo {
    private Integer id;
    private Integer owner;
    private String title;
    private String notes;
    private BigDecimal expCost;
    private BigDecimal actCost;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate comeTrueDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;
    private String entryUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime entryDatetime;
    private List<DreamPicBo> pics = new ArrayList<>();

}
