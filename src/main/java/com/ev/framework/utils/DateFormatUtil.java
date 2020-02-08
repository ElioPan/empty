package com.ev.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DateFormatUtil {
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String getFormateDate(Date date) {
        SimpleDateFormat dateFormat = setFormat(DATE_PATTERN);
        String timeString = dateFormat.format(date);
        return timeString;
    }

    public static String getFormateDate(Date date, String pattern) {
        SimpleDateFormat dateFormat = setFormat(pattern);
        String timeString = dateFormat.format(date);
        return timeString;
    }

    private static SimpleDateFormat setFormat(String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat;
    }

    public static Date getDateByParttern(String date, String pattern) {
        SimpleDateFormat dateFormat = setFormat(pattern);
        try {
            Date resultDate = dateFormat.parse(date);
            return resultDate;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDateByParttern(String date) {
        SimpleDateFormat dateFormat = setFormat(DATE_PATTERN);
        try {
            Date resultDate = dateFormat.parse(date);
            return resultDate;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String getMonthTimeQuantum() {
        //获取当前日期前一个月日期到当前日期的时间段，格式:2016/02/12-2016/03/12
        Date date = new Date();//当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化对象
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);//设置当前日期
        calendar.add(Calendar.MONTH, -1);//月份减一
        String startTimeString = sdf.format(calendar.getTime());
        String endTimeString = sdf.format(date);
        String timeQuantum = startTimeString + "~" + endTimeString;
        return timeQuantum;
    }

    public static Date getBeforeMonthTime(Date date) {
        //获取当前日期前一个月日期
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);//设置当前日期
        calendar.add(Calendar.MONTH, -1);//月份减一
        Date startTimeString = calendar.getTime();
        return startTimeString;
    }

    public static Date getBeforeWeekTime(Date date) {
        //获取当前日期前一个周日期
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);//设置当前日期
        calendar.add(Calendar.WEDNESDAY, -1);//
        Date startTimeString = calendar.getTime();
        return startTimeString;
    }

    public static Date getBeforeWeekStartTime(Date date) {
        //获取当前日期前一个周最早时间
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);//设置当前日期
        calendar.add(Calendar.DATE, -6);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startTimeString = calendar.getTime();
        return startTimeString;
    }

    public static Date getYesterdayTime(Date date) {
        //获取当前日期前一个月日期
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);//设置当前日期
        calendar.add(Calendar.DATE, -1);//
        Date startTimeString = calendar.getTime();
        return startTimeString;
    }

    public static Date getYesterdayEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);//设置当前日期
        calendar.add(Calendar.DATE, -1);//
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getCurrentDateEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);//设置当前日期
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    public static Date getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    public static List<String> getFourWeekTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> dateList = new ArrayList<String>();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);//设置当前日期
        calendar.add(Calendar.WEDNESDAY, -1);//
        calendar.set(Calendar.DAY_OF_WEEK, 2);
        Date startTimeString = calendar.getTime();
        dateList.add(sdf.format(startTimeString));
        for (int i = 0; i < 3; i++) {
            calendar.add(Calendar.DATE, -7);
            String dateStrEnd = sdf.format(calendar.getTime());
            String dateStr = dateStrEnd;
            dateList.add(dateStr);
        }
        Collections.reverse(dateList);
        return dateList;
    }

    public static List<String> getLastFourWeekTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        List<String> dateList = new ArrayList<String>();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);//设置当前日期
//		calendar.add(Calendar.WEDNESDAY, -1);//
//		calendar.set(Calendar.DAY_OF_WEEK, 2);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startTimeString = calendar.getTime();
        dateList.add(sdf.format(startTimeString));
        for (int i = 0; i < 3; i++) {
            calendar.add(Calendar.DATE, -7);
            String dateStrEnd = sdf.format(calendar.getTime());
            String dateStr = dateStrEnd;
            dateList.add(dateStr);
        }
        Collections.reverse(dateList);
        return dateList;
    }

    public static int getWeekTime(Date date) {
        //获取当前日期是一年中的第几周
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        return c1.get(Calendar.WEEK_OF_YEAR);
    }

    public static Date getLastWeekTime(Date date) {
        //当前时间的前一个星期 开始时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -6);
//		String start = format.format(c.getTime()) + " 00:00:00";
        return c.getTime();
    }

    public static Date getLastMonthTime(Date date) {
        //当天日期前一个月
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);    //得到前一个月
//		String start = format.format(c.getTime()) + " 00:00:00";
        return c.getTime();
    }

    public static Date getLastYearTime(Date date) {
        //当前时间的前一年开始时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1); //年份减1
//		String start = format.format(c.getTime()) + " 00:00:00";
        return c.getTime();
    }

    public static Date getStratTimeByTimeCycle(String timeCycle, Date endTime) {
        Date startTime = null;
        if (timeCycle.equals("week")) {
            startTime = getLastWeekTime(endTime);
        } else if (timeCycle.equals("month")) {
            startTime = getLastMonthTime(endTime);
        } else if (timeCycle.equals("year")) {
            startTime = getLastYearTime(endTime);
        }
        return startTime;
    }


    /**
     * 获取某个时间段内所有日期 * @param begin * @param end * @return
     */
    public static List<String> getDayBetweenDates(Date dBegin, Date dEnd) {
        List<String> lDate = new ArrayList<String>();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        lDate.add(sd.format(dBegin));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间 calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(sd.format(calBegin.getTime()));
        }
        return lDate;
    }

    public static String getWorkOrderno() {
        //17位时间戳工单号
        String workOrderno = DateFormatUtil.getFormateDate(new Date(), "yyyyMMddHHmmssSSS");
        return workOrderno;
    }

    /**
     * 获取12位工单号的前缀
     */
    public static String getWorkOrderno(String value) {
        //12位工单号的前缀
        return value + DateFormatUtil.getFormateDate(new Date(), "yyyyMMdd");
    }

    /**
     * 获取12位工单号的前缀
     */
    public static String getWorkOrderno(String value, Date date) {
        //12位工单号的前缀
        return value + DateFormatUtil.getFormateDate(date, "yyyyMMdd");
    }

    /**
     * 获取16位工单号
     */
    public static String getWorkOrderno(String prefix, String suffix) {
        //16位工单号的前缀
        String sortNo = "0000";
        if (suffix != null) {
            sortNo = suffix.substring(12);
        }
        sortNo = StringUtils.autoGenericCode(sortNo, 4);
        return prefix + sortNo;
    }

    /**
     * 生成没有时间戳的编号
     */
    public static String getWorkOrderno(String prefix, String suffix, Integer zeroNum) {
        if (zeroNum <= 0) {
            return null;
        }
        //16位工单号的前缀
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < zeroNum; i++) {
            if (i == 10) {
                break;
            }
            stringBuilder.append(0);
        }
        String sortNo = stringBuilder.toString();
        if (suffix != null) {
            String[] split = suffix.split("\\D");
            if (split.length > 0) {
                List<String> collect = Arrays.stream(split).filter(s -> StringUtils.isNoneEmpty(s)).collect(Collectors.toList());
                if (collect.size() == 1) {
                    String s = collect.get(0);
                    if (s.length() >= 10) {
                        return prefix + StringUtils.autoGenericCode(sortNo, zeroNum);
                    }
                    return prefix + StringUtils.autoGenericCode(s, zeroNum);
                }
            }
        }
        return prefix + StringUtils.autoGenericCode(sortNo, zeroNum);
    }

    // 生成带有时间的单据号
    public static String getWorkOrderNo(String prefix, String suffix, Integer zeroNum) {
        if (zeroNum <= 0) {
            zeroNum = 4;
        }

        suffix = suffix == null ? "" : suffix;

        StringBuilder stringBuilder = new StringBuilder(StringUtils.isEmpty(prefix) ? "DJ" : prefix);
        stringBuilder.append(getFormateDate(new Date(), "yyyyMMdd"));
        if (suffix.contains(stringBuilder)) {
            String[] strings = suffix.split(stringBuilder.toString());
            System.err.println(strings.length);
            if (strings.length >= 2) {
                System.err.println(strings[1]);
                return stringBuilder.append(StringUtils.autoGenericCode(strings[1], zeroNum)).toString();
            }
        }
        for (int i = 0; i < zeroNum; i++) {
            if (i == 10) {
                break;
            }
            if (i == zeroNum - 1) {
                stringBuilder.append(1);
            } else {
                stringBuilder.append(0);
            }
        }
        return stringBuilder.toString();
    }
}
