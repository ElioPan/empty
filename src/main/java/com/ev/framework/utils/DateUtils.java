package com.ev.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理
 */
public class DateUtils {
    private final static Logger logger = LoggerFactory.getLogger(DateUtils.class);
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String format(Date date) {

        return format(date, DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }


    /**
     * 计算距离现在多久，非精确
     *
     * @param date
     * @return
     */
    public static String getTimeBefore(Date date) {
        Date now = new Date();
        long l = now.getTime() - date.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        String r = "";
        if (day > 0) {
            r += day + "天";
        } else if (hour > 0) {
            r += hour + "小时";
        } else if (min > 0) {
            r += min + "分";
        } else if (s > 0) {
            r += s + "秒";
        }
        r += "前";
        return r;
    }

    /**
     * 计算距离现在多久，精确
     *
     * @param date
     * @return
     */
    public static String getTimeBeforeAccurate(Date date) {
        Date now = new Date();
        long l = now.getTime() - date.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        String r = "";
        if (day > 0) {
            r += day + "天";
        }
        if (hour > 0) {
            r += hour + "小时";
        }
        if (min > 0) {
            r += min + "分";
        }
        if (s > 0) {
            r += s + "秒";
        }
        r += "前";
        return r;
    }


    /**
     * 计算任意2个日期内的工作日（没有考虑到国定假日）
     *
     * @author user 主要思路： 对于任意2个日期比如：date_start=2006-10-1、date_end=2006-10-14
     *         ，首先计算这连个日期之间的时间间隔（天数）， 然后分别对date_start 和date_end 取得它们下一个星期一的日期，
     *         这样就可以得 到一个新的可以整除7的完整日期间隔（这个新的日期间隔已经把星期几的问题剔出掉了），
     *         换一种说法就是我们可以得到，这两个新的日期之间的周数，拿这个周数乘以5就是工作日期了（tmpWorkingDays）。
     *         但是这个日期并不是我们所要的日期，接下来我们要做的就是计算
     *         date_start,date_end这两个日期对于根据它们所产生的新的日期之间的时间偏移量， date_start的偏
     *         移量（date_start_change）是需要加的，而date_end的这个偏移量（date_end_change）是需要减去的 。
     *         最后我们只要用tmpWorkingDays
     *         +date_start_change-date_end_change就是我们所要求的实际工作日了。
     *         以下是所有实现代码（两个日期跨年也没有问题）。
     */
    public static int getWorkingDay(String startTime,String endTime) {
        Date dateStart = DateFormatUtil.getDateByParttern(startTime, "yyyy-MM-dd");
        Date dateEnd = DateFormatUtil.getDateByParttern(endTime, "yyyy-MM-dd");
        if (dateEnd == null || dateStart == null) {
            return -1;
        }
        Calendar d1 = Calendar.getInstance();
        d1.setTime(dateStart);
        d1.set(Calendar.DAY_OF_MONTH, d1.get(Calendar.DAY_OF_MONTH) - 1);
        Calendar d2 = Calendar.getInstance();
        d2.setTime(dateEnd);
        return getWorkingDay(d1, d2);
    }

    public static int getWorkingDay(Calendar d1, Calendar d2) {
        if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int chargeStartDate = 0;// 开始日期的日期偏移量
        int chargeEndDate = 0;// 结束日期的日期偏移量
        // 日期不在同一个日期内
        int stmp;
        int etmp;
        stmp = 7 - d1.get(Calendar.DAY_OF_WEEK);
        etmp = 7 - d2.get(Calendar.DAY_OF_WEEK);
        if (stmp != 0 && stmp != 6) {// 开始日期为星期六和星期日时偏移量为0
            chargeStartDate = stmp - 1;
        }
        if (etmp != 0 && etmp != 6) {// 结束日期为星期六和星期日时偏移量为0
            chargeEndDate = etmp - 1;
        }
        return (getDaysBetween(getNextMonday(d1), getNextMonday(d2)) / 7)
                * 5 + chargeStartDate - chargeEndDate;
    }

    public static int getDaysBetween(Calendar d1, Calendar d2) {
        if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int days = d2.get(Calendar.DAY_OF_YEAR)
                - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone(); //
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    /**
     * 获得日期的下一个星期一的日期
     */
    public static Calendar getNextMonday(Calendar date) {
        Calendar result;
        result = date;
        do {
            result = (Calendar) result.clone(); // 可能是在while中需要保持值的不变clone
            result.add(Calendar.DATE, 1);
        } while (result.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY);
        return result;
    }

    /**
     * 获取休息日
     */
    public static int getHolidays(Calendar d1, Calendar d2) {
        return getDaysBetween(d1, d2) - getWorkingDay(d1, d2);
    }

    public static String getChineseWeek(Calendar date) {
        final String[] dayNames = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
                "星期六" };
        int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
        // System.out.println(dayNames[dayOfWeek - 1]);
        return dayNames[dayOfWeek - 1];
    }

    /**
     * 获取两个日期的自然周
     * @param d1 开始时间
     * @param d2 结束时间
     * @return 自然周数量
     */
    public static int getWeekNum(Calendar d1, Calendar d2) {
        int week = 1;
        int days = getDaysBetween(d1, d2) + 1;
        switch (d1.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                days = days - 1;
                break;
            case Calendar.MONDAY:
                week = 0;
                break;
            case Calendar.TUESDAY:
                days = days - 6;
                break;
            case Calendar.WEDNESDAY:
                days = days - 5;
                break;
            case Calendar.THURSDAY:
                days = days - 4;
                break;
            case Calendar.FRIDAY:
                days = days - 3;
                break;
            case Calendar.SATURDAY:
                days = days - 2;
                break;
        }
        int weekFirst = days / 7;
        int weekSecond = days % 7;
        week += weekFirst;
        if (weekSecond != 0) {
            week++;
        }
        return week;
    }

    public static int getMonthNum(Calendar d1, Calendar d2) {
        int startYear = d1.get(Calendar.YEAR);
        int startMonth = d1.get(Calendar.MONTH);
        int endYear = d2.get(Calendar.YEAR);
        int endMonth = d2.get(Calendar.MONTH);
        return  (endYear - startYear) * 12 + endMonth - startMonth + 1;
    }
}
