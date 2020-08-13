package com.neil.cashbook.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "dream_pic")
@Entity
@Getter
@Setter
public class DreamPic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cos_key")
    private String cosKey;

    @Column(name = "cos_key_small")
    private String cosKeySmall;
}
