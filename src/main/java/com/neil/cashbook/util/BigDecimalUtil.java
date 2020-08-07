package com.neil.cashbook.util;
import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

public class BigDecimalUtil {
    public static BigDecimal toBigDecimal(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        str = str.trim();
        try {
            return new BigDecimal(str);
        } catch (Exception e) {
            return null;
        }
    }
}
