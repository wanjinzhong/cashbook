package com.neil.cashbook.dao.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "cashbook_detail")
@Entity
@Getter
@Setter
public class CashbookDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "header_id")
    private CashbookHeader header;

    @Column
    private BigDecimal cost;

    @Column
    private String notes;

    @ManyToOne
    @JoinColumn(name = "entry_id")
    private User entryUser;

    @Column(name = "entry_datetime")
    private LocalDateTime entryDatetime;
}
