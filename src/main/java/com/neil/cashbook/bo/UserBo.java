package com.neil.cashbook.bo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBo {
    private String name;
    private String avatar;
    private String openId;
    private boolean allowEntry;
}
