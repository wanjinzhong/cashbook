package com.neil.cashbook.dao.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

    @Column(name = "dream_owner")
    private Integer owner;

    @Column(name = "notes")
    private String notes;

    @Column(name = "exp_cost")
    private BigDecimal expCost;

    @Column(name = "act_cost")
    private BigDecimal actCost;

    @Column(name = "come_true_date")
    private LocalDate comeTrueDate;

    @Column
    private LocalDate deadline;

    @OneToMany(mappedBy = "dream")
    private List<DreamPic> pics;

    @ManyToOne
    @JoinColumn(name = "entry_id")
    private User entryUser;

    @Column(name = "entry_datetime")
    private LocalDateTime entryDatetime;
}
