package com.example.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具
 *
 * @author zhou.xy
 * @since 1.0
 */
public class DateUtil {
    public class DatePattern {
        public static final String HHMMSS = "HHmmss";
        public static final String HH_MM_SS = "HH:mm:ss";
        public static final String YYYYMMDD = "yyyMMdd";
        public static final String YYYY_MM_DD = "yyyy-MM-dd";
        public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
        public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
        public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
        public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

        private DatePattern() {
        }
    }

    /**
     * 格式化日期
     *
     * @param date    日期
     * @param pattern 格式
     * @return String
     */
    public static String format(Date date, String pattern) {
        if (null == date) {
            return null;
        }
        if (ValidateUtil.isEmpty(pattern)) {
            return format(date);
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 格式化日期
     *
     * @param date 日期
     * @return String 格式：yyyy-MM-dd
     */
    public static String format(Date date) {
        return format(date, DatePattern.YYYY_MM_DD);
    }

    /**
     * 获取当前时间
     *
     * @return String 格式：yyyy-MM-dd
     */
    public static String getDate() {
        return format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return String 格式：yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTime() {
        return format(new Date(), DatePattern.YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 按照指定格式返回当前时间
     *
     * @param pattern 格式
     * @return String
     */
    public static String getDateTime(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 日期计算
     *
     * @param date   日期
     * @param field  calendar field
     * @param amount the amount of date or time to be added to the field
     * @return Date
     */
    public static Date addDate(Date date, int field, int amount) {
        if (null == date) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * 间隔秒
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return Integer
     */
    public static Integer intervalSecond(Date startDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        return (int) ((end.getTimeInMillis() - start.getTimeInMillis()) / 1000L);
    }

    /**
     * 间隔天
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return Integer
     */
    public static Integer intervalDay(Date startDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);
        return (int) ((end.getTimeInMillis() - start.getTimeInMillis()) / (60 * 60 * 24 * 1000L));
    }

    /**
     * 根据日期获取星期
     *
     * @param date 日期
     * @return 星期
     */
    public static String getWeekByDate(Date date) {
        String[] weekDaysName = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }
}
