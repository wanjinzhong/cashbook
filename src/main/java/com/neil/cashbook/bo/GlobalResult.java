package com.neil.cashbook.bo;

import lombok.Data;

@Data
public class GlobalResult <T> {
    private Integer status;
    private String msg;
    private T data;

    public static <T> GlobalResult<T> build(Integer status, String msg, T data) {
        GlobalResult<T> result = new GlobalResult<>();
        result.setStatus(status);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> GlobalResult<T> of(T data) {
        GlobalResult<T> result = new GlobalResult<>();
        result.setStatus(200);
        result.setData(data);
        return result;
    }
}