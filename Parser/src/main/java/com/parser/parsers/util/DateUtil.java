package com.parser.parsers.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static String getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return String.valueOf(cal.get(Calendar.YEAR));
    }
}
