/**
 * @(#)TimeUtil.java, 2014年10月14日. Copyright 2012 Yodao, Inc. All rights
 *                    reserved. YODAO PROPRIETARY/CONFIDENTIAL. Use is subject
 *                    to license terms.
 */
package com.example.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author yodao
 */
public class TimeUtil {

    public static String getLeftTime(int interval) {
        int hour = interval / 3600000;
        interval = interval / 1000;
        int seconds = interval % 60;
        interval = interval / 60;
        interval = interval % 60;
        int minutes = interval;

        if (hour > 0) {
            return "0" + hour + ":" + (minutes < 10 ? "0" + minutes : minutes)
                    + ":" + (seconds < 10 ? "0" + seconds : seconds);
        }
        return (minutes < 10 ? "0" + minutes : minutes) + ":"
                + (seconds < 10 ? "0" + seconds : seconds);
    }

    public static String getDateStr(long time) {
        String timeText = "";
        Date d = new Date(time);
        Date today = new Date();
        if (TimeUtil.isTheDay(d, today)) {
            return "今天 " + getDayTimeMinute(time);
        } else if (TimeUtil.isTheDay(time + 86400000, today)) {
            timeText = "昨天 " + getDayTimeMinute(time);
        } else {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.ENGLISH);
            timeText = format.format(d) + " " + getDayTimeMinute(time);
        }
        return timeText;
    }

    /*
     * 获取时间段长度
     */
    public static String getTimestr(long sec, long usec) {
        if (usec < 0) {// 除0的情况
            sec -= 1;
            usec += (1000000);
        }
        if (sec < 60) {
            return sec + " 秒" + usec + "微秒";
        } else if (sec < 3600) {
            return (sec / 60) + "分" + getTimestr(sec % 60, usec);
        } else if (sec < 86400) {
            return (sec / 3600) + "时" + getTimestr(sec % 3600, usec);
        } else {
            return (sec / 86400) + "天" + getTimestr(sec % 86400, usec);
        }
    }

    public static String getTimestr(Integer interval) {
        if (interval < 60) {
            return interval + " 秒";
        } else {
            return (interval / 60) + "分" + getTimestr(interval % 60);
        }
    }

    /*
     * 将数据包的时间转化为实体时间 sec,usec
     */
    public static String getTime(long sec) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String r = "";
        Date d = new Date();
        d.setTime(sec);
        r = sdf.format(d);
        return r;
    }

    public static int getHourSeconds(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            Date d = sdf.parse(date);
            Calendar cl = Calendar.getInstance();
            cl.setTime(d);
            int r = 0;
            r += cl.get(Calendar.MINUTE) * 60;
            r += cl.get(Calendar.SECOND);
            r += cl.get(Calendar.HOUR) * 3600;
            return r * 1000;
        } catch (ParseException e) {
            return 0;
        }
    }

    public static String getTime(long sec, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        String r = "";
        Date d = new Date();
        d.setTime(sec);
        r = sdf.format(d);
        return r;
    }

    public static long getDateTime(String time, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        String r = "";
        Date d = null;
        try {
            d = sdf.parse(time);
        } catch (ParseException e) {
            return Long.parseLong(time);
        }
        return d.getTime();
    }

    public static long getTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String r = "";
        Date d = null;
        try {
            d = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d.getTime();
    }

    public static long getTime(String time, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        String r = "";
        Date d = null;
        try {
            d = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d.getTime();
    }

    /*
     * 日期获取 按照毫秒
     */
    public static String getDate(long time) {
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
        return sfd.format(time);
    }

    public static String getDateMD(long time) {
        SimpleDateFormat sfd = new SimpleDateFormat("MM-dd");
        return sfd.format(time);
    }

    /*
     * 日期获取 按照毫秒
     */
    public static long getDate(String time) {
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sfd.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static String lastWeekYYMMDD(String time, int backDate)
            throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        date = sfd.parse(time);
        calendar.setTime(date);
        calendar.set(Calendar.WEEK_OF_YEAR, backDate);
        date = calendar.getTime();
        return sfd.format(date);
    }

    public static String lastMonthYYMMDD(String time, int backDate)
            throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sfd = new SimpleDateFormat("yyyyMM");
        Date date;
        date = sfd.parse(time);
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, backDate);
        date = calendar.getTime();
        return sfd.format(date);
    }

    public static String lastYearYYMMDD(String time, int backDate)
            throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sfd = new SimpleDateFormat("yyyy");
        Date date;
        date = sfd.parse(time);
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, backDate);
        date = calendar.getTime();
        return sfd.format(date);
    }

    public static String lastDayYYMMDD(String time, int backDate)
            throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sfd = new SimpleDateFormat("yyyyMMdd");
        Date date;
        date = sfd.parse(time);
        calendar.setTime(date);
        calendar.add(Calendar.DATE, backDate);
        date = calendar.getTime();
        return sfd.format(date);
    }

    public static String getDayTimeMinute(long time) {
        SimpleDateFormat sfd = new SimpleDateFormat("HH:mm");
        return sfd.format(time);
    }

    public static String getDuractionTime(long starttime, long nowtime) {
        long dure = nowtime - starttime;
        return getChinatime(dure / 1000);
    }

    public static String getChinatime(long time) {
        if (time < 60) {
            return time + "秒";
        } else if (time < 3600) {
            return time / 60 + "分" + getChinatime(time % 60);
        } else if (time < 86400) {
            return time / 3600 + "小时" + getChinatime(time % 3600);
        } else {
            return time / 86400 + "天" + getChinatime(time % 86400);
        }
    }

    public static String getSimpleIntervalTime(long interval, long time) {
        /*
         * if (interval < 3600) { return interval / 60 + "minute ago"; } else if
         * (interval < 86400) { return interval / 3600 + "hour ago"; } else if
         * (interval < 30*86400) { return interval / 86400 + "day ago"; }else{
         * return getDate(time); }
         */
        return getDate(time);
    }

    public static boolean isTheDay(final Date date, final Date day) {
        return date.getTime() >= dayBegin(day).getTime()
                && date.getTime() <= dayEnd(day).getTime();
    }

    public static boolean isTheDay(long time, final Date day) {
        return time >= dayBegin(day).getTime() && time <= dayEnd(day).getTime();
    }

    public static Date dayBegin(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取指定时间的那天 23:59:59.999 的时间
     * 
     * @param date
     * @return
     */
    public static Date dayEnd(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }
}
