package com.neil.cashbook.util;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.neil.cashbook.exception.BizException;

public class DateUtil {
    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
    public static LocalDate toDate(String date) {
        try {
            return LocalDate.parse(date, FORMATTER);
        }catch (Exception e) {
            throw new BizException("时间格式不正确");
        }
    }
}
