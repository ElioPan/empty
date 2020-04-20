package com.ev.framework.utils;

import com.ev.common.domain.WeekDays;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * 由于为了以后使用方便,所有方法的返回类型都设为了 java.util.Date 请在使用时根据自己的需要进行日期格式化处理,如:
 * 
 * import java.text.SimpleDateFormat;SimpleDateFormat simpleDateFormat = new
 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss");String todayBegin =
 * simpleDateFormat.format
 * (DateUtils.getDayBegin());System.out.println(todayBegin );//输出结果为2017-10-26
 * 00:00:00
 */
/**
 * 日期工具类
 */
public class DatesUtil {

	// 获取当天的开始时间
	public static java.util.Date getDayBegin() {
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	// 获取当天的结束时间
	public static java.util.Date getDayEnd() {
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	// 获取昨天的开始时间
	public static Date getBeginDayOfYesterday() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(getDayBegin());
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}

	// 获取昨天的结束时间
	public static Date getEndDayOfYesterDay() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(getDayEnd());
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}

	// 获取明天的开始时间
	public static Date getBeginDayOfTomorrow() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(getDayBegin());
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	// 获取明天的结束时间
	public static Date getEndDayOfTomorrow() {
		Calendar cal = new GregorianCalendar();
		cal.setTime(getDayEnd());
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	// 获取本周的开始时间
	@SuppressWarnings("unused")
	public static Date getBeginDayOfWeek() {
		Date date = new Date();
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 1) {
			dayofweek += 7;
		}
		cal.add(Calendar.DATE, 2 - dayofweek);
		return getDayStartTime(cal.getTime());
	}

	// 获取本周的结束时间
	public static Date getEndDayOfWeek() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getBeginDayOfWeek());
		cal.add(Calendar.DAY_OF_WEEK, 6);
		Date weekEndSta = cal.getTime();
		return getDayEndTime(weekEndSta);
	}

	// 获取上周的开始时间
	@SuppressWarnings("unused")
	public static Date getBeginDayOfLastWeek() {
		Date date = new Date();
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 1) {
			dayofweek += 7;
		}
		cal.add(Calendar.DATE, 2 - dayofweek - 7);
		return getDayStartTime(cal.getTime());
	}

	// 获取上周的结束时间
	public static Date getEndDayOfLastWeek() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getBeginDayOfLastWeek());
		cal.add(Calendar.DAY_OF_WEEK, 6);
		Date weekEndSta = cal.getTime();
		return getDayEndTime(weekEndSta);
	}

	// 获取本月的开始时间
	public static Date getBeginDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(getNowYear(), getNowMonth() - 1, 1);
		return getDayStartTime(calendar.getTime());
	}

	// 获取本月的结束时间
	public static Date getEndDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(getNowYear(), getNowMonth() - 1, 1);
		int day = calendar.getActualMaximum(5);
		calendar.set(getNowYear(), getNowMonth() - 1, day);
		return getDayEndTime(calendar.getTime());
	}

	// 获取上月的开始时间
	public static Date getBeginDayOfLastMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(getNowYear(), getNowMonth() - 2, 1);
		return getDayStartTime(calendar.getTime());
	}

	// 获取上月的结束时间
	public static Date getEndDayOfLastMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(getNowYear(), getNowMonth() - 2, 1);
		int day = calendar.getActualMaximum(5);
		calendar.set(getNowYear(), getNowMonth() - 2, day);
		return getDayEndTime(calendar.getTime());
	}

	// 获取本年的开始时间
	public static Date getBeginDayOfYear() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, getNowYear());
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DATE, 1);
		return getDayStartTime(cal.getTime());
	}

	// 获取本年的结束时间
	public static java.util.Date getEndDayOfYear() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, getNowYear());
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DATE, 31);
		return getDayEndTime(cal.getTime());
	}

	// 获取某个日期的开始时间
	public static Timestamp getDayStartTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if (null != d) {
			calendar.setTime(d);
		}
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
				0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Timestamp(calendar.getTimeInMillis());
	}

	// 获取某个日期的结束时间
	public static Timestamp getDayEndTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if (null != d) {
			calendar.setTime(d);
		}
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23,
				59, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return new Timestamp(calendar.getTimeInMillis());
	}

	// 获取今年是哪一年
	public static Integer getNowYear() {
		Date date = new Date();
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		return Integer.valueOf(gc.get(1));
	}

	// 获取本月是哪一月
	public static int getNowMonth() {
		Date date = new Date();
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(date);
		return gc.get(2) + 1;
	}

	// 两个日期相减得到的天数
	public static int getDiffDays(Date beginDate, Date endDate) {
		if (beginDate == null || endDate == null) {
			throw new IllegalArgumentException("getDiffDays param is null!");
		}
		long diff = (endDate.getTime() - beginDate.getTime()) / (1000 * 60 * 60 * 24);
		int days = new Long(diff).intValue();
		return days;
	}

	// 两个日期相减得到的毫秒数
	public static long dateDiff(Date beginDate, Date endDate) {
		long date1ms = beginDate.getTime();
		long date2ms = endDate.getTime();
		return date2ms - date1ms;
	}

	// 两个日期相减得到的小时
	public static BigDecimal dateHour(Date beginDate, Date endDate) {
		long date1ms = beginDate.getTime();
		long date2ms = endDate.getTime();
		BigDecimal ms=new BigDecimal(date2ms - date1ms);
		BigDecimal msss=BigDecimal.valueOf((int)(1000 * 60 * 60));
		BigDecimal result = ms.divide(msss,3,BigDecimal.ROUND_HALF_UP);
		return result;
	}

	// 获取两个日期中的最大日期
	public static Date max(Date beginDate, Date endDate) {
		if (beginDate == null) {
			return endDate;
		}
		if (endDate == null) {
			return beginDate;
		}
		if (beginDate.after(endDate)) {
			return beginDate;
		}
		return endDate;
	}

	// 获取两个日期中的最小日期
	public static Date min(Date beginDate, Date endDate) {
		if (beginDate == null) {
			return endDate;
		}
		if (endDate == null) {
			return beginDate;
		}
		if (beginDate.after(endDate)) {
			return endDate;
		}
		return beginDate;
	}

	// 返回某月该季度的第一个月
	public static Date getFirstSeasonDate(Date date) {
		final int[] SEASON = { 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4 };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int sean = SEASON[cal.get(Calendar.MONTH)];
		cal.set(Calendar.MONTH, sean * 3 - 3);
		return cal.getTime();
	}

	// 返回某个日期下几天的日期
	public static Date getNextDay(Date date, int i) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) + i);
		return cal.getTime();
	}

	// 返回某个日期前几天的日期
	public static Date getFrontDay(Date date, int i) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) - i);
		return cal.getTime();
	}

	// 获取某年某月到某年某月按天的切片日期集合(间隔天数的集合)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getTimeList(int beginYear, int beginMonth, int endYear, int endMonth, int k) {
		List list = new ArrayList();
		if (beginYear == endYear) {
			for (int j = beginMonth; j <= endMonth; j++) {
				list.add(getTimeList(beginYear, j, k));
			}
		} else {
			{
				for (int j = beginMonth; j < 12; j++) {
					list.add(getTimeList(beginYear, j, k));
				}
				for (int i = beginYear + 1; i < endYear; i++) {
					for (int j = 0; j < 12; j++) {
						list.add(getTimeList(i, j, k));
					}
				}
				for (int j = 0; j <= endMonth; j++) {
					list.add(getTimeList(endYear, j, k));
				}
			}
		}
		return list;
	}

	// 获取某年某月按天切片日期集合(某个月间隔多少天的日期集合)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List getTimeList(int beginYear, int beginMonth, int k) {
		List list = new ArrayList();
		Calendar begincal = new GregorianCalendar(beginYear, beginMonth, 1);
		int max = begincal.getActualMaximum(Calendar.DATE);
		for (int i = 1; i < max; i = i + k) {
			list.add(begincal.getTime());
			begincal.add(Calendar.DATE, k);
		}
		begincal = new GregorianCalendar(beginYear, beginMonth, max);
		list.add(begincal.getTime());
		return list;
	}

	public static Map<String,Object> findDates() {
		Date dBegin = getBeginDayOfWeek();
		Date dEnd = getEndDayOfWeek();
		List<String> lDate = new ArrayList<>();
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		int index = 0;
		Map<String, Object> param = Maps.newHashMapWithExpectedSize(14);
		while (dEnd.after(calBegin.getTime())) {
			index++;
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			lDate.add(DateFormatUtil.getFormateDate(calBegin.getTime()));
			String startDate = DateFormatUtil.getFormateDate(calBegin.getTime());
			String endDate = DateFormatUtil.getFormateDate(getDayEndTime(calBegin.getTime()));
			switch (index) {
			case 1:
				param.put(WeekDays.MON.getName(), startDate);
				param.put(WeekDays.MON.getName() + "end", endDate);
				break;
			case 2:
				param.put(WeekDays.TUE.getName(), startDate);
				param.put(WeekDays.TUE.getName() + "end", endDate);
				break;
			case 3:
				param.put(WeekDays.WED.getName(), startDate);
				param.put(WeekDays.WED.getName() + "end", endDate);
				break;
			case 4:
				param.put(WeekDays.THU.getName(), startDate);
				param.put(WeekDays.THU.getName() + "end", endDate);
				break;
			case 5:
				param.put(WeekDays.FRI.getName(), startDate);
				param.put(WeekDays.FRI.getName() + "end", endDate);
				break;
			case 6:
				param.put(WeekDays.SAT.getName(), startDate);
				param.put(WeekDays.SAT.getName() + "end", endDate);
				break;
			case 7:
				param.put(WeekDays.SUM.getName(), startDate);
				param.put(WeekDays.SUM.getName() + "end", endDate);
				break;
			default:
				break;
			}
			lDate.add(DateFormatUtil.getFormateDate(getDayEndTime(calBegin.getTime())));
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
		}
		return param;
	}


		//获取当前日期是星期几
	public static  int  getWeekOfDateIndex(Date date) {
		int[] weekDays = {0,1, 2, 3, 4, 5, 6};
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekDays[w];
	}

	//获取当前日期是星期几
	public static  String   getWeekOfDate(Date date) {
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekDays[w];
	}


	/**
	 * 得到几天后的时间
	 *
	 * @param d
	 * @param day
	 * @return
	 */
	public Date getDateAfter(Date d, int day) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);//+后 -前
		return now.getTime();
	}

	/**
	 * 得到几天前的时间集合(包含当天)
	 *
	 * @param d
	 * @param day
	 * @return
	 */
	public static List<Date> getDateBefore(Date d, int day) {
		List<Date> dateList = new ArrayList<>();
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		//+后 -前
		for (int i = 0; i < day +1; i++) {
			dateList.add(now.getTime());
			now.set(Calendar.DATE, now.get(Calendar.DATE) - 1);
		}
		return dateList;
	}


//	/**
//	 * 获取过去或者未来 任意天内的日期数组
//	 *
//	 * @param intervals intervals天内
//	 * @return 日期数组
//	 */
//	public static ArrayList<String> test(int intervals) {
//		ArrayList<String> pastDaysList = new ArrayList<>();
//		ArrayList<String> fetureDaysList = new ArrayList<>();
//		for (int i = 0; i < intervals; i++) {
//			pastDaysList.add(getPastDate(i));
//			fetureDaysList.add(getFetureDate(i));
//		}
//		return pastDaysList;
//
//	}

	/**
	 *       * 根据开始时间和结束时间返回时间段内的时间集合
	 *       * @param beginDate
	 *       * @param endDate
	 *       * @return List<Date>
	 *       * @throws ParseException
	 *      
	 */
	public static List<String> getDatesBetweenTwoDate(String beginDate, String endDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<String> lDate = new ArrayList<String>();
		lDate.add(beginDate);//把开始时间加入集合
		Calendar cal = Calendar.getInstance();
//使用给定的 Date 设置此 Calendar 的时间
		cal.setTime(sdf.parse(beginDate));
		boolean bContinue = true;
		while (bContinue) {
			//根据日历的规则，为给定的日历字段添加或减去指定的时间量
			cal.add(Calendar.DAY_OF_MONTH, 1);
// 测试此日期是否在指定日期之后
			if (sdf.parse(endDate).after(cal.getTime())) {
				lDate.add(sdf.format(cal.getTime()));
			} else {
				break;
			}
		}
		lDate.add(endDate);//把结束时间加入集合
		return lDate;
	}

	/**
	 * 根据提供的年月日获取该月份的第一天
	 */
	public static String getSupportBeginDayOfMonth(Date date) {
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(date);
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		startDate.set(Calendar.HOUR_OF_DAY, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.set(Calendar.MILLISECOND, 0);
		Date firstDate = startDate.getTime();
		//  SimpleDateFormat myFmt7=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat myFmt7=new SimpleDateFormat("yyyy-MM-dd");
		return myFmt7.format(firstDate);

	}

	public static Date getSupportBeginDayOfMonthToDate(Date date) {
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(date);
		startDate.set(Calendar.DAY_OF_MONTH, startDate.getActualMaximum(Calendar.DAY_OF_MONTH));
		startDate.set(Calendar.HOUR_OF_DAY, 23);
		startDate.set(Calendar.MINUTE, 59);
		startDate.set(Calendar.SECOND, 59);
		startDate.set(Calendar.MILLISECOND, 999);
		return startDate.getTime();

	}

	/**
	 * 根据提供的年月获取该月份的最后一天
	 */
	public  static String getSupportEndDayOfMonth(Date date) {
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(date);
		startDate.set(Calendar.DAY_OF_MONTH, startDate.getActualMaximum(Calendar.DAY_OF_MONTH));
		startDate.set(Calendar.HOUR_OF_DAY, 23);
		startDate.set(Calendar.MINUTE, 59);
		startDate.set(Calendar.SECOND, 59);
		startDate.set(Calendar.MILLISECOND, 999);
		Date firstDate = startDate.getTime();
//   SimpleDateFormat myFmt7=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat myFmt7=new SimpleDateFormat("yyyy-MM-dd");
		return myFmt7.format(firstDate);
	}


}
