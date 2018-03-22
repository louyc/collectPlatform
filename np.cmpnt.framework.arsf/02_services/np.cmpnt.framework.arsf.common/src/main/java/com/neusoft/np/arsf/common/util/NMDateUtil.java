/**
 * 
 * sql时间转换为字符串（时间精确到年月日） 将Sql时间戳转换为字符串（时间精确到年月日时分秒） 将字符串转换为sql时间（时间精确到年月日）
 * 将字符串转换为Sql时间戳（时间精确到年月日时分秒） 将util时间转换为sql时间 将util时间转换为字符串，一个例子："yyyy-MM-dd
 * HH:mm:ss" 将时间转化为util时间 获取当前日期为日期型 获取当前日期为字符串 按格式转换日期的格式到固定格式的时间 将固定格式字符串转化为日期
 * 为 sql 里直接通过result.getObject获取日期型变量准备的方法. 取得与原日期相差一定月数的日期，返回Date型日期
 * 取得与原日期相差一定天数的日期，返回String型日期 将日期型的对象进行运算 计算某天所在月的第一天 计算某天所在月的最后一天
 * 得到两个日期之间相差的天数 根据日期得出当前周在当月的第几周 根据日期得出当前周在当年的第几周
 * 
 */
package com.neusoft.np.arsf.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.lang.StringUtils;

/**
 * From Netpatrol 5.1 Web
 * 
 * @author 骆宾
 */
public class NMDateUtil {

	// 周期(unit 1秒 2 分钟,3 小时,4 天,5周,6月,7季,6 年)
	public final static int UNIT_SECOND = 1;
	public final static int UNIT_MINUTE = 2;
	public final static int UNIT_HOUR = 3;
	public final static int UNIT_DAY = 4;
	public final static int UNIT_WEEK = 5;
	public final static int UNIT_MONTH = 6;
	public final static int UNIT_SEASON = 7;
	public final static int UNIT_YEAR = 8;

	private static String defaultDatePattern = null;
	private static String timePattern = "HH:mm";
	public static final String TS_FORMAT = NMDateUtil.getDatePattern() + " HH:mm:ss.S";
	private static Calendar cale = Calendar.getInstance();

	/**
	 * 将util时间转换为字符串，一个例子："yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param inDate
	 * @param pattern
	 * @return String
	 */
	public static String dateToString(java.util.Date inDate, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(inDate);
	}

	/**
	 * 将util时间转换为字符串，格式："yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param inDate
	 * @param pattern
	 * @return String
	 */
	public static String dateToString(java.util.Date inDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(inDate);
	}

	/**
	 * 将时间转化为util时间
	 * 
	 * @param inStr
	 * @param pattern
	 * @return Date
	 */
	public static java.util.Date stringToDate(String inStr, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(inStr, new ParsePosition(0));
	}

	/**
	 * 获取当前日期为日期型
	 * 
	 * @return
	 */
	public static java.util.Date getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

	/**
	 * 获取当前日期为字符串
	 * 
	 * @return
	 */
	public static String getCurrentDateToString(String format) {
		return dateToString(getCurrentDate(), format);
	}

	/**
	 * 取得与原日期相差一定月数的日期，返回Date型日期
	 * 
	 * @param date
	 * @param intBetween
	 * @return
	 */
	public static java.util.Date getDateMonthBetween(java.util.Date date, int intBetween) {
		Calendar calo = Calendar.getInstance();
		calo.setTime(date);
		calo.add(Calendar.MONTH, intBetween);
		return calo.getTime();
	}

	/**
	 * 取得与原日期相差一定天数的日期，返回Date型日期
	 * 
	 * @param date
	 * @param intBetween
	 * @return
	 */
	public static java.util.Date getDateBetween(java.util.Date date, int intBetween) {
		Calendar calo = Calendar.getInstance();
		calo.setTime(date);
		calo.add(Calendar.DATE, intBetween);
		return calo.getTime();
	}

	/**
	 * 取得与原日期相差一定天数的日期，返回String型日期
	 * 
	 * @param date
	 * @param intBetween
	 * @param strFromat
	 * @return
	 */
	public static String getDateBetweenString(java.util.Date date, int intBetween, String strFromat) {
		java.util.Date dateOld = getDateBetween(date, intBetween);
		return dateToString(dateOld, strFromat);
	}

	/**
	 * 按格式转换日期的格式到固定格式的时间 <br>
	 * 转换时格式的字符必须符合要求.
	 * 
	 * @param date
	 *            待转换的日期.
	 * 
	 * @param format
	 *            转换格式. 格式必须符合: <br>
	 *            yyyy, 输出四位年 yy, 输出两位年 <br>
	 *            MM, 月 <br>
	 *            dd, 日期 <br>
	 *            HH, 小时24小时制 <br>
	 *            mm, 分钟 <br>
	 *            ss, 秒 <br>
	 *            中间间隔符号按照需要填写. 如: yyyy--MM--dd
	 */
	public static String stringDateTime(java.util.Date date, String format) {
		if (date == null) {
			throw new IllegalArgumentException("design data is null. ");
		}
		SimpleDateFormat subDateFormat = new SimpleDateFormat(format);
		return subDateFormat.format(date);
	}

	/**
	 * 为 sql 里直接通过result.getObject获取日期型变量准备的方法. 按格式转换日期的格式到固定格式的时间 <br>
	 * 转换时格式的字符必须符合要求.
	 * 
	 * @param date
	 *            待转换的日期.
	 * @param format
	 *            转换格式. 格式必须符合: <br>
	 *            yyyy, 输出四位年 yy, 输出两位年 <br>
	 *            MM, 月 <br>
	 *            dd, 日期 <br>
	 *            HH, 小时 <br>
	 *            mm, 分钟 <br>
	 *            ss, 秒 <br>
	 *            中间间隔符号按照需要填写. 如: yyyy--MM--dd
	 */
	public static String stringDateTime(Object date, String format) {
		return stringDateTime((java.util.Date) date, format);
	}

	/**
	 * 将日期型的对象进行运算.
	 * 
	 * @param date
	 *            待计算的日期
	 * @param field
	 *            待计算的项目 Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, <br>
	 *            Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND
	 * @param amount
	 *            待计算的数量. 负数表示减.
	 */
	public static java.util.Date dateAdd(java.util.Date date, int field, int amount) {
		if (date == null) {
			throw new IllegalArgumentException("design data is null. ");
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * 将固定格式字符串转化为日期"
	 * 
	 * @param strDate
	 *            格式为:"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static java.util.Date dateString(String strDate) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.parse(strDate);
	}

	/**
	 * 
	 * 计算某天所在月的第一天
	 * 
	 * @param date
	 *            日期类型
	 * 
	 * @return Date 日期类型
	 * @throws ParseException
	 */
	public static java.util.Date monthlyFirstDate(java.util.Date date) throws ParseException {
		if (date == null) {
			throw new IllegalArgumentException("design data is null. ");
		}
		String strDate = stringDateTime(date, "yyyy-MM");
		return dateString(strDate + "-01 00:00:00");
	}

	/**
	 * 计算某天所在月的最后一天
	 * 
	 * @param date
	 *            参加计算的某天日期
	 * 
	 * @return Date 日期类型
	 * @throws ParseException
	 */
	public static java.util.Date monthlyEndDate(java.util.Date date) throws ParseException {
		if (date == null) {
			throw new IllegalArgumentException("design data is null. ");
		}
		java.util.Date nextMonth = dateAdd(date, Calendar.MONTH, 1);
		String strDate = stringDateTime(nextMonth, "yyyy-MM");
		strDate += "-01 23:59:59";
		return dateAdd(dateString(strDate), Calendar.DATE, -1);
	}

	/**
	 * 得到两个日期之间相差的天数
	 * 
	 * @param newDate
	 * @param oldDate
	 * @return days
	 */
	public static int daysBetweenDates(java.util.Date newDate, java.util.Date oldDate) {
		int days = 0;
		Calendar calo = Calendar.getInstance();
		Calendar caln = Calendar.getInstance();
		calo.setTime(oldDate);
		caln.setTime(newDate);
		int oday = calo.get(Calendar.DAY_OF_YEAR);
		int nyear = caln.get(Calendar.YEAR);
		int oyear = calo.get(Calendar.YEAR);
		while (nyear > oyear) {
			calo.set(Calendar.MONTH, 11);
			calo.set(Calendar.DATE, 31);
			days = days + calo.get(Calendar.DAY_OF_YEAR);
			oyear = oyear + 1;
			calo.set(Calendar.YEAR, oyear);
		}
		int nday = caln.get(Calendar.DAY_OF_YEAR);
		days = days + nday - oday;
		return days;
	}

	/**
	 * 根据日期得出当前周在当月的第几周
	 * 
	 * @param date
	 * @return String
	 */
	public static String getWeekOfMonths(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(stringToDate(date, "yyyymmdd"));
		int weeks = cal.get(Calendar.WEEK_OF_MONTH);
		return String.valueOf(weeks);
	}

	/**
	 * 
	 */

	public static String getFirstDayOf(String date) {

		Calendar c = Calendar.getInstance();
		try {
			c.setTime(dateString(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String first = sdf2.format(c.getTime());
		return first;
	}

	/**
	 * 根据日期得出当前周在当年的第几周
	 * 
	 * @param date
	 * @return String
	 */
	public static String getWeekOfYears(String date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(stringToDate(date, "yyyymmdd"));
		int weeks = cal.get(Calendar.WEEK_OF_YEAR);
		return String.valueOf(weeks);
	}

	public static Date getNextTimeStampByUnit(int unit) {
		Date nextTimeStamp = null;
		switch (unit) {
		case UNIT_MINUTE:
			nextTimeStamp = QuartzDateBuilder.nextMinutesDateAfterNow();
			break;
		case UNIT_HOUR:
			nextTimeStamp = QuartzDateBuilder.nextHoursDateAfterNow();
			break;
		case UNIT_DAY:
			nextTimeStamp = QuartzDateBuilder.nextDayDateAfterNow();
			break;
		case UNIT_WEEK:
			nextTimeStamp = QuartzDateBuilder.nextWeekDateAfterNow();
			break;
		case UNIT_MONTH:
			nextTimeStamp = QuartzDateBuilder.nextMonthDateAfterNow();
			break;
		case UNIT_SEASON:
			nextTimeStamp = QuartzDateBuilder.nextSeasonDateAfterNow();
			break;
		case UNIT_YEAR:
			nextTimeStamp = QuartzDateBuilder.nextYearDateAfterNow();
			break;
		}

		return nextTimeStamp;
	}

	public static Date getNextTimeStampByUnit(int unit, Date startTime) {
		Date nextTimeStamp = null;
		switch (unit) {
		case UNIT_MINUTE:
			nextTimeStamp = QuartzDateBuilder.nextMinutesDate(startTime, 1);
			break;
		case UNIT_HOUR:
			nextTimeStamp = QuartzDateBuilder.nextHoursDate(startTime, 1);
			break;
		case UNIT_DAY:
			nextTimeStamp = QuartzDateBuilder.nextDayDate(startTime, 1);
			break;
		case UNIT_WEEK:
			nextTimeStamp = QuartzDateBuilder.nextWeekDate(startTime, 1);
			break;
		case UNIT_MONTH:
			nextTimeStamp = QuartzDateBuilder.nextMonthDate(startTime, 1);
			break;
		case UNIT_SEASON:
			nextTimeStamp = QuartzDateBuilder.nextSeasonDate(startTime, 1);
			break;
		case UNIT_YEAR:
			nextTimeStamp = QuartzDateBuilder.nextYearDate(startTime, 1);
			break;
		}

		return nextTimeStamp;
	}

	/**
	 * 根据单位（ 1秒 2 分钟,3 小时,4 天,5 周,6 月,7 季,8 年）计算秒，如 1 minute = 60 second
	 * 
	 * @param timeUint
	 *            时间单位
	 * @return period 时间步长
	 */
	public static String toSecond(String timeUint, String period) {
		try {
			int iPeriod = Integer.parseInt(period);
			int unit = Integer.parseInt(timeUint);
			switch (unit) {
			case UNIT_MINUTE:
				iPeriod *= 60;
				break;
			case UNIT_HOUR:
				iPeriod *= 60 * 60;
				break;
			case UNIT_DAY:
				iPeriod *= 60 * 60 * 24;
				break;
			case UNIT_WEEK:
				iPeriod *= 7 * 60 * 60 * 24;
				break;
			case UNIT_MONTH:
				iPeriod *= 60 * 60 * 24 * 30;
				break;
			case UNIT_SEASON:
				iPeriod *= 3 * 60 * 60 * 24 * 30;
				break;
			case UNIT_YEAR:
				iPeriod *= 12 * 60 * 60 * 24 * 30;
				break;
			default:
				break;
			}
			return String.valueOf(iPeriod);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获得服务器当前日期及时间，以格式为：yyyy-MM-dd HH:mm:ss的日期字符串形式返回
	 */
	public static String getDateTime() {
		try {
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf2.format(cale.getTime());
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获得服务器当前日期，以格式为：yyyy-MM-dd的日期字符串形式返回
	 */
	public static String getDate() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(cale.getTime());
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获得服务器当前时间，以格式为：HH:mm:ss的日期字符串形式返回
	 */
	public static String getTime() {
		String temp = "";
		try {
			SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
			temp += sdf1.format(cale.getTime());
			return temp;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 统计时开始日期的默认值, 今年的开始时间
	 */
	public static String getStartDate() {
		try {
			return getYear() + "-01-01";
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 统计时结束日期的默认值
	 */
	public static String getEndDate() {
		try {
			return getDate();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获得服务器当前日期的年份
	 */
	public static String getYear() {
		try {
			// 返回的int型，需要字符串转换
			return String.valueOf(cale.get(Calendar.YEAR));
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获得服务器当前日期的月份
	 */
	public static String getMonth() {
		try {
			// 一个数字格式，非常好
			java.text.DecimalFormat df = new java.text.DecimalFormat();
			df.applyPattern("00");
			return df.format((cale.get(Calendar.MONTH) + 1));
			// return String.valueOf(cale.get(Calendar.MONTH) + 1);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获得服务器在当前月中天数
	 */
	public static String getDay() {
		try {
			return String.valueOf(cale.get(Calendar.DAY_OF_MONTH));
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 比较两个日期相差的天数, 第一个日期要比第二个日期要晚
	 */
	public static int getMargin(String date1, String date2) {
		int margin;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			ParsePosition pos = new ParsePosition(0);
			ParsePosition pos1 = new ParsePosition(0);
			Date dt1 = sdf.parse(date1, pos);
			Date dt2 = sdf.parse(date2, pos1);
			long l = dt1.getTime() - dt2.getTime();
			margin = (int) (l / (24 * 60 * 60 * 1000));
			return margin;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 比较两个日期相差的天数，格式不一样 第一个日期要比第二个日期要晚
	 */
	public static double getDoubleMargin(String date1, String date2) {
		double margin;
		try {
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ParsePosition pos = new ParsePosition(0);
			ParsePosition pos1 = new ParsePosition(0);
			Date dt1 = sdf2.parse(date1, pos);
			Date dt2 = sdf2.parse(date2, pos1);
			long l = dt1.getTime() - dt2.getTime();
			margin = (l / (24 * 60 * 60 * 1000.00));
			return margin;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 比较两个日期相差的月数
	 */
	public static int getMonthMargin(String date1, String date2) {
		int margin;
		try {
			margin = (Integer.parseInt(date2.substring(0, 4)) - Integer.parseInt(date1.substring(0, 4))) * 12;
			margin += (Integer.parseInt(date2.substring(4, 7).replaceAll("-0", "-")) - Integer.parseInt(date1
					.substring(4, 7).replaceAll("-0", "-")));
			return margin;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 返回日期加X天后的日期
	 */
	public static String addDay(String date, int i) {
		try {
			GregorianCalendar gCal = new GregorianCalendar(Integer.parseInt(date.substring(0, 4)),
					Integer.parseInt(date.substring(5, 7)) - 1, Integer.parseInt(date.substring(8, 10)));
			gCal.add(GregorianCalendar.DATE, i);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(gCal.getTime());
		} catch (Exception e) {
			return getDate();
		}
	}

	/**
	 * 返回日期加X月后的日期
	 */
	public static String addMonth(String date, int i) {
		try {
			GregorianCalendar gCal = new GregorianCalendar(Integer.parseInt(date.substring(0, 4)),
					Integer.parseInt(date.substring(5, 7)) - 1, Integer.parseInt(date.substring(8, 10)));
			gCal.add(GregorianCalendar.MONTH, i);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(gCal.getTime());
		} catch (Exception e) {
			return getDate();
		}
	}

	/**
	 * 返回日期加X年后的日期
	 */
	public static String addYear(String date, int i) {
		try {
			GregorianCalendar gCal = new GregorianCalendar(Integer.parseInt(date.substring(0, 4)),
					Integer.parseInt(date.substring(5, 7)) - 1, Integer.parseInt(date.substring(8, 10)));
			gCal.add(GregorianCalendar.YEAR, i);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(gCal.getTime());
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 返回某年某月中的最大天
	 */
	public static int getMaxDay(String year, String month) {
		int day = 0;
		try {
			int iyear = Integer.parseInt(year);
			int imonth = Integer.parseInt(month);
			if (imonth == 1 || imonth == 3 || imonth == 5 || imonth == 7 || imonth == 8 || imonth == 10 || imonth == 12) {
				day = 31;
			} else if (imonth == 4 || imonth == 6 || imonth == 9 || imonth == 11) {
				day = 30;
			} else if ((0 == (iyear % 4)) && (0 != (iyear % 100)) || (0 == (iyear % 400))) {
				day = 29;
			} else {
				day = 28;
			}
			return day;
		} catch (Exception e) {
			return 1;
		}
	}

	/**
	 * 格式化日期
	 */
	@SuppressWarnings("static-access")
	public String rollDate(String orgDate, int Type, int Span) {
		try {
			String temp = "";
			int iyear, imonth, iday;
			int iPos = 0;
			char seperater = '-';
			if (orgDate == null || orgDate.length() < 6) {
				return "";
			}

			iPos = orgDate.indexOf(seperater);
			if (iPos > 0) {
				iyear = Integer.parseInt(orgDate.substring(0, iPos));
				temp = orgDate.substring(iPos + 1);
			} else {
				iyear = Integer.parseInt(orgDate.substring(0, 4));
				temp = orgDate.substring(4);
			}

			iPos = temp.indexOf(seperater);
			if (iPos > 0) {
				imonth = Integer.parseInt(temp.substring(0, iPos));
				temp = temp.substring(iPos + 1);
			} else {
				imonth = Integer.parseInt(temp.substring(0, 2));
				temp = temp.substring(2);
			}

			imonth--;
			if (imonth < 0 || imonth > 11) {
				imonth = 0;
			}

			iday = Integer.parseInt(temp);
			if (iday < 1 || iday > 31)
				iday = 1;

			Calendar orgcale = Calendar.getInstance();
			orgcale.set(iyear, imonth, iday);
			temp = this.rollDate(orgcale, Type, Span);
			return temp;
		} catch (Exception e) {
			return "";
		}
	}

	public static String rollDate(Calendar cal, int Type, int Span) {
		try {
			String temp = "";
			Calendar rolcale;
			rolcale = cal;
			rolcale.add(Type, Span);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			temp = sdf.format(rolcale.getTime());
			return temp;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 
	 * 返回默认的日期格式
	 * 
	 */
	public static synchronized String getDatePattern() {
		defaultDatePattern = "yyyy-MM-dd";
		return defaultDatePattern;
	}

	/**
	 * 将指定日期按默认格式进行格式代化成字符串后输出如：yyyy-MM-dd
	 */
	public static final String getDate(Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat(getDatePattern());
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * 取得给定日期的时间字符串，格式为当前默认时间格式
	 */
	public static String getTimeNow(Date theTime) {
		return getDateTime(timePattern, theTime);
	}

	/**
	 * 取得当前时间的Calendar日历对象
	 */
	public Calendar getToday() throws ParseException {
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat(getDatePattern());
		String todayAsString = df.format(today);
		Calendar cal = new GregorianCalendar();
		cal.setTime(convertStringToDate(todayAsString));
		return cal;
	}

	/**
	 * 将日期类转换成指定格式的字符串形式
	 */
	public static final String getDateTime(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate == null) {
		} else {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}
		return (returnValue);
	}

	/**
	 * 将指定的日期转换成默认格式的字符串形式
	 */
	public static final String convertDateToString(Date aDate) {
		return getDateTime(getDatePattern(), aDate);
	}

	/**
	 * 将日期字符串按指定格式转换成日期类型
	 * 
	 * @param aMask
	 *            指定的日期格式，如:yyyy-MM-dd
	 * @param strDate
	 *            待转换的日期字符串
	 */

	public static final Date convertStringToDate(String aMask, String strDate) throws ParseException {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);

		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			throw pe;
		}
		return (date);
	}

	/**
	 * 将日期字符串按默认格式转换成日期类型
	 */
	public static Date convertStringToDate(String strDate) throws ParseException {
		Date aDate = null;

		try {
			aDate = convertStringToDate(getDatePattern(), strDate);
		} catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());

		}

		return aDate;
	}

	/**
	 * 返回一个JAVA简单类型的日期字符串
	 */
	public static String getSimpleDateFormat() {
		SimpleDateFormat formatter = new SimpleDateFormat();
		String NDateTime = formatter.format(new Date());
		return NDateTime;
	}

	/**
	 * 将两个字符串格式的日期进行比较
	 * 
	 * @param last
	 *            要比较的第一个日期字符串
	 * @param now
	 *            要比较的第二个日期格式字符串
	 * @return true(last 在now 日期之前),false(last 在now 日期之后)
	 */
	public static boolean compareTo(String last, String now) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date temp1 = formatter.parse(last);
			Date temp2 = formatter.parse(now);
			if (temp1.after(temp2))
				return false;
			else if (temp1.before(temp2))
				return true;
		} catch (ParseException e) {
		}
		return false;
	}

	protected Object convertToDate(Class<?> type, Object value) {
		DateFormat df = new SimpleDateFormat(TS_FORMAT);
		if (value instanceof String) {
			try {
				if (StringUtils.isEmpty(value.toString())) {
					return null;
				}
				return df.parse((String) value);
			} catch (Exception pe) {
				throw new ConversionException("Error converting String to Timestamp");
			}
		}

		throw new ConversionException("Could not convert " + value.getClass().getName() + " to " + type.getName());
	}

	/**
	 * 为查询日期添加最小时间
	 * 
	 * @param 目标类型Date
	 * @param 转换参数Date
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date addStartTime(Date param) {
		Date date = param;
		try {
			date.setHours(0);
			date.setMinutes(0);
			date.setSeconds(0);
			return date;
		} catch (Exception ex) {
			return date;
		}
	}

	/**
	 * 为查询日期添加最大时间
	 * 
	 * @param 目标类型Date
	 * @param 转换参数Date
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date addEndTime(Date param) {
		Date date = param;
		try {
			date.setHours(23);
			date.setMinutes(59);
			date.setSeconds(0);
			return date;
		} catch (Exception ex) {
			return date;
		}
	}

	/**
	 * 返回系统现在年份中指定月份的天数
	 * 
	 * @param 月份month
	 * @return 指定月的总天数
	 */
	@SuppressWarnings("deprecation")
	public static String getMonthLastDay(int month) {
		Date date = new Date();
		int[][] day = { { 0, 30, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 },
				{ 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 } };
		int year = date.getYear() + 1900;
		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
			return day[1][month] + "";
		} else {
			return day[0][month] + "";
		}
	}

	/**
	 * 返回指定年份中指定月份的天数
	 * 
	 * @param 年份year
	 * @param 月份month
	 * @return 指定月的总天数
	 */
	public static String getMonthLastDay(int year, int month) {
		int[][] day = { { 0, 30, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 },
				{ 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 } };
		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
			return day[1][month] + "";
		} else {
			return day[0][month] + "";
		}
	}

	/**
	 * 取得当前时间的日戳
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getTimestamp() {
		Date date = new Date();
		String timestamp = "" + (date.getYear() + 1900) + date.getMonth() + date.getDate() + date.getMinutes()
				+ date.getSeconds() + date.getTime();
		return timestamp;
	}

	/**
	 * 取得指定时间的日戳
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getTimestamp(Date date) {
		String timestamp = "" + (date.getYear() + 1900) + date.getMonth() + date.getDate() + date.getMinutes()
				+ date.getSeconds() + date.getTime();
		return timestamp;
	}

}
