package com.neil.cashbook.dao.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
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

@Table(name = "dream")
@Entity
@Getter
@Setter
public class Dream {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String title;

    @Column
    private String desc;

    @Column
    private BigDecimal cost;

    @Column(name = "come_true")
    private LocalDate comeTrue;

    @Column
    private LocalDate deadline;

    @ManyToOne
    @JoinColumn(name = "entry_id")
    private User entryUser;

    @Column(name = "entry_datetime")
    private LocalDateTime entryDatetime;
}
