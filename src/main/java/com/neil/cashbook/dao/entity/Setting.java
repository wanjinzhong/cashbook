package com.neil.cashbook.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.neil.cashbook.enums.SettingKey;
import lombok.Getter;
import lombok.Setter;

@Table(name = "setting")
@Entity
@Getter
@Setter
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "setting_key")
    @Enumerated(EnumType.STRING)
    private SettingKey key;

    @Column(name = "setting_value")
    private String value;
}
