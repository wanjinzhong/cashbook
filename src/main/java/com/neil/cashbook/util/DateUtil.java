package com.neil.cashbook.util;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.neil.cashbook.exception.BizException;
import org.apache.commons.lang.StringUtils;

public class DateUtil {
    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static DateTimeFormatter FORMATTER_NO_DAY = DateTimeFormatter.ofPattern("yyyy-MM");

    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate toLocalDate(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        try {
            return LocalDate.parse(date, FORMATTER);
        } catch (Exception e) {
            throw new BizException("时间格式不正确");
        }
    }

    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static String toString(LocalDate date) {
        return date.format(FORMATTER);
    }

    public static String toStringWithoutDay(LocalDate date) {
        return date.format(FORMATTER_NO_DAY);
    }

    public static LocalDate min(LocalDate d1, LocalDate d2) {
        return d1.isBefore(d2) ? d1 : d2;
    }
}
