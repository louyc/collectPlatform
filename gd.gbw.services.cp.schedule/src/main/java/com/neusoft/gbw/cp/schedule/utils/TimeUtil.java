package com.neusoft.gbw.cp.schedule.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import com.neusoft.gbw.cp.schedule.constants.ScheduleConstants;
import com.neusoft.np.arsf.base.bundle.Log;


public class TimeUtil {
	
	/**
	 * yMdHms 转化为map
	 * @param time
	 * @return
	 */
	@SuppressWarnings("static-access")
	public Map<String, String> dateToMap(String time){
		Map<String,String> map = new Hashtable<String, String>();
		String s = time.trim().toLowerCase();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(s);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int month = cal.get(cal.MONTH)+1;
			int day = cal.get(cal.DAY_OF_MONTH);
			int hour = cal.get(cal.HOUR_OF_DAY);
			int minute = cal.get(cal.MINUTE);
			int second = cal.get(cal.SECOND);
			map.put(ScheduleConstants.YEAR, cal.get(cal.YEAR)+"");
			map.put(ScheduleConstants.MONTH, toplanMonth(month));
			map.put(ScheduleConstants.DAY, toplanDay(day));
			map.put(ScheduleConstants.HOUR, toplanHour(hour));
			map.put(ScheduleConstants.MINUTE, toplayMinute(minute));
			map.put(ScheduleConstants.SECOND, toplaySecond(second));
		} catch (ParseException e) {
			throw new IllegalArgumentException("参数格式不对(yyyy-MM-dd HH:mm:ss)" + time);
		}
		//添加捕捉异常
		catch(NullPointerException e){
			Log.debug("NullPointerException     空指针异常        TimeUtil");
		}catch(IllegalArgumentException  e){
			Log.debug("IllegalArgumentException 不合法 不合适参数    TimeUtil");
		}
		return map;
	}
	public static String getAfterTime(String time, int interval) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(df.parse(time));
		cal.add(Calendar.MINUTE, interval);
		return df.format(cal.getTime());
	}
	
	public Map<String, String> hourToMap(String time){
		Map<String,String> map = new Hashtable<String, String>();
		int hour = Integer.parseInt(time);
		map.put(ScheduleConstants.HOUR, toplanHour(hour));
		return map;
	}
	public Map<String, String> minuteToMap(String time){
		Map<String,String> map = new Hashtable<String, String>();
		int minute = Integer.parseInt(time);
		map.put(ScheduleConstants.HOUR, toplanHour(minute));
		return map;
	}
	
	/**
	 * Hms转化为map
	 * @param time
	 * @return
	 */
	public Map<String, String> timeToMap(String time){
		Map<String,String> map = new Hashtable<String, String>();
		int[] array = timeToint(time);
		map.put(ScheduleConstants.HOUR, toplanHour(array[0]));
		map.put(ScheduleConstants.MINUTE, toplayMinute(array[1]));
		map.put(ScheduleConstants.SECOND, toplaySecond(array[2]));
		return map;
	}
	
	/**
	 * Time类型的字符转化为int
	 * 
	 * @param str
	 * @return
	 */
	public  int[] timeToint(String str) {
		String[] s = str.split(":");
		if (s.length != 3) {
			throw new IllegalArgumentException("参数格式不对(00:00:00)" + str);
		}
		int[] array = new int[s.length];
		for (int i = 0; i < s.length; i++) {
			array[i] = Integer.parseInt(s[i]);
		}
		return array;
	}
	
	public String toplanMonth(int m){
		String planMonth = "000000000000";
		String month = planMonth.substring(0, m-1)+"1"+planMonth.substring(m);
		return month;
	}
	public String toplanDay(int d){
		String planDay = "0000000000000000000000000000000";
		String day = planDay.substring(0, d-1)+"1"+planDay.substring(d);
		return day;
	}
	public String toplanHour(int h){
		String planHour = "000000000000000000000000";
		if (h==0) {
			return planHour = "000000000000000000000000";
		}
		String hour = planHour.substring(0, h-1)+"1"+planHour.substring(h);
		return hour;
	}
	public String toplanWeek(int w){
		String planWeek = "0000000";
		String week = planWeek.substring(0, w-1)+"1"+planWeek.substring(w);
		return week;
	}
	public String toplayMinute(int min){
		String playMinute = "000000000000000000000000000000000000000000000000000000000000";
		if (min==0) {
			return playMinute = "000000000000000000000000000000000000000000000000000000000000";
		}
		String minute = playMinute.substring(0, min-1)+"1"+playMinute.substring(min);
		return minute;
	}
	public String toplaySecond(int sec){
		String playSecond = "000000000000000000000000000000000000000000000000000000000000";
		if (sec==0) {
			return playSecond = "000000000000000000000000000000000000000000000000000000000000";
		}
		String second = playSecond.substring(0, sec-1)+"1"+playSecond.substring(sec);
		return second;
	}
	
	/**
	 * 验证传进来的开始时间是否大于当前日期
	 */
	public boolean checkStartTime(String startTime) {
		Date dt1 = string2Date(startTime);
		Date dt2 = new Date();
		if(dt1.getTime() < dt2.getTime()) {
			return false;
		}else {
			return true;
		}
	}
	public String date2String(Date date){
		//时间格式化器
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(true);
		return dateFormat.format(date);
	}
	
	private Date string2Date(String date) {
		try {
			//时间格式化器
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//指定日期/时间解析是否不严格 lenient - 为 true 时，解析过程是不严格的
			dateFormat.setLenient(true);
			return dateFormat.parse(date);
		} catch (Exception e) {
			Log.debug("NullPointerException IllegalArgumentException  ParseException   TimeUtil");
			return null;
		}
	}
	

}
