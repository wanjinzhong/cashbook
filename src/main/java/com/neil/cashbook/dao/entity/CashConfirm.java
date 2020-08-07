package com.neil.cashbook.dao.entity;

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

@Table(name = "cash_confirm")
@Entity
@Getter
@Setter
public class CashConfirm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "header_id")
    private CashHeader header;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "entry_datetime")
    private LocalDateTime entryDatetime;
}
