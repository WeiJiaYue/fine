package com.radarwin.framework.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by josh on 15/6/23.
 */
public class DateUtil {
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD_HH = "yyyy-MM-dd HH";
    public static final String HH_MM_SS = "HH:mm:ss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDDHHMMSS="yyyyMMddHHmmss";

    public static final long ONE_DAY_MILLS = 86400000;

    public static String convertToString(Date date, String format) {
        if (date == null) {
            return null;
        }
        String s = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            s = simpleDateFormat.format(date);
        } catch (Exception ex) {
        }
        return s;
    }

    public static Date convertToDate(String s, String format) {
        if (StringUtil.isBlank(s)) {
            return null;
        }
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            date = simpleDateFormat.parse(s);
        } catch (Exception ex) {
        }
        return date;
    }

    public static String getCurrentTime() {
        String dateString = null;
        try {
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateString = formatter.format(currentTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dateString;
    }

    public static String getCurrentTimeByFormat(String format) {
        String dateString = null;
        try {
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            dateString = formatter.format(currentTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dateString;
    }
}
