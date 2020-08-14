package com.neil.cashbook.dao.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.neil.cashbook.util.DateUtil;
import lombok.Getter;
import lombok.Setter;

@Table(name = "cash_header")
@Entity
@Getter
@Setter
public class CashHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cash_date")
    private Date cashDate;

    @Column
    private BigDecimal quota;

    @Column
    private BigDecimal cost;

    @OneToMany(mappedBy = "header")
    private List<CashDetail> details = new ArrayList<>();

    public LocalDate getDate() {
        return DateUtil.toLocalDate(this.cashDate);
    }
}
