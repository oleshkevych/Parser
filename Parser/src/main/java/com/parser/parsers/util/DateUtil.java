package com.parser.parsers.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

    public static String getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return String.valueOf(cal.get(Calendar.YEAR));
    }

    public static Date getDate(String stringDate) {
        Date datePublished = null;
        if (stringDate.contains("now") || stringDate.contains("minut") || stringDate.contains("hour")) {
            datePublished = new Date();
        } else if (stringDate.contains("yesterday") || stringDate.contains("1 day")) {
            datePublished = new Date(new Date().getTime() - 1 * 24 * 3600 * 1000);
        } else {
            if (stringDate.contains("day")) {
                String date = stringDate.replaceAll("[^0-9]", "");
                if ((date.length() != 0)) {
                    datePublished = new Date(new Date().getTime() - Integer.parseInt(date) * 24 * 3600 * 1000);
                }
            } else if (stringDate.contains("week")) {
                String date = stringDate.replaceAll("[^0-9]", "");
                if ((date.length() != 0)) {
                    datePublished = new Date(new Date().getTime() - Integer.parseInt(date) * 7 * 24 * 3600 * 1000);
                }
            } else if (stringDate.contains("month")) {
                String date = stringDate.replaceAll("[^0-9]", "");
                if ((date.length() != 0)) {
                    datePublished = new Date(new Date().getTime() - Integer.parseInt(date) * 30 * 24 * 3600 * 1000);
                }
            }
        }
        return datePublished;
    }

    public static Date getDateWithOneLetter(String stringDate) {
        Date datePublished = null;
        if (stringDate.contains("now") || stringDate.contains("minut") || stringDate.contains("h ago")) {
            datePublished = new Date();
        } else if (stringDate.contains("yesterday") || stringDate.contains("1 day")) {
            datePublished = new Date(new Date().getTime() - 1 * 24 * 3600 * 1000);
        } else {
            if (stringDate.contains("d ago")) {
                String date = stringDate.replaceAll("[^0-9]", "");
                if ((date.length() != 0)) {
                    datePublished = new Date(new Date().getTime() - Integer.parseInt(date) * 24 * 3600 * 1000);
                }
            } else if (stringDate.contains("w ago")) {
                String date = stringDate.replaceAll("[^0-9]", "");
                if ((date.length() != 0)) {
                    datePublished = new Date(new Date().getTime() - Integer.parseInt(date) * 7 * 24 * 3600 * 1000);
                }
            } else if (stringDate.contains("m ago")) {
                String date = stringDate.replaceAll("[^0-9]", "");
                if ((date.length() != 0)) {
                    datePublished = new Date(new Date().getTime() - Integer.parseInt(date) * 30 * 24 * 3600 * 1000);
                }
            }
        }
        return datePublished;
    }
}
