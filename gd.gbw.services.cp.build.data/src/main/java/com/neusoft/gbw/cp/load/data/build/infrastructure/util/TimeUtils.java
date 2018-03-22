package com.neusoft.gbw.cp.load.data.build.infrastructure.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.neusoft.gbw.cp.conver.util.Time;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMDateUtil;

public class TimeUtils {
	
	private static final String TIME = "00:00:00";
	private static int[] weekDays = {0,1,2,3,4,5,6};
	
	public static List<String> getTimeSepList(int interval) throws Exception {
		String start = getYMdTime() + " " + TIME;
		String end = getDifferOneDay() + " " + TIME;
		return getTimeSepList(start, end, interval);
	}

	public static List<String> getTimeSepList(String start, String end, int interval) throws Exception {
		long startTime = getTimeToMilliSecond(start);
		long endTime = getTimeToMilliSecond(end);
		int sep = interval*60*1000;
		int m = (int) ((endTime-startTime) / sep);
		List<String> list = new ArrayList<String>();
		long sum = startTime;
		list.add(getMilliSecondToTime(sum));
		for(int i = 0; i<m;i++){
			sum = sum + (long)sep;
			list.add(getMilliSecondToTime(sum));
		}
		
		return list;
	}

	public static String getYMdTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(new Date()).toString();
	}
	
	public static String getHmsTime() {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		return df.format(new Date()).toString();
	}
	
	public static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}
	
	public static String getDifferOneDay() {
		return NMDateUtil.getDateBetweenString(new Date(), 1, "yyyy-MM-dd");
	}
	
	public static String getMilliSecondToTime(long million) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(million);
	}
	
	public static long getTimeToMilliSecond(String time) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.parse(time).getTime();
	}
	
	public static long getHHmmssTimeToMilliSecond(String time) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		return df.parse(time).getTime();
	}
	
	public static int converterToTime(String time) {
		int i = Time.timeToint(time);
		return i;
	}
	
	public static boolean isScopeWeekTime(String startTime, String endTime, int dayofWeek, String startRemindTime, String endRemindTime) {
		if (dayofWeek == -1) {
			return isScopeTime(getMergeTime(startTime), getMergeTime(endTime), startRemindTime, endRemindTime);
		}
		
		int currentWeek = getWeekofCurrentDate(); // 得到当前星期
		if (dayofWeek == currentWeek) {
			return isScopeTime(getMergeTime(startTime), getMergeTime(endTime), startRemindTime, endRemindTime);
		}
		return false;
	}
	
	public static String getMergeTime(String time) {
		return getYMdTime() + " " + time;
	}
	
	public static int getWeekofCurrentDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (week < 0)
			week = 0;
		return weekDays[week];
	}
	
	public static String getAfterTime(String time, int interval) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(df.parse(time));
		cal.add(Calendar.MINUTE, interval);
		return df.format(cal.getTime());
	}
	
	public static String getBeforeTime(String time, int interval) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(df.parse(time));
		cal.add(Calendar.MINUTE, -interval);
		return df.format(cal.getTime());
	}
	
	public static String getBeforeDayTime(String time, int interval) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(df.parse(time));
		cal.add(Calendar.DAY_OF_YEAR, -interval);
		return df.format(cal.getTime());
	}
	
	public static String getAfterDayTime(String time, int interval) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(df.parse(time));
		cal.add(Calendar.DAY_OF_YEAR, interval);
		return df.format(cal.getTime());
	}
	
	public static boolean isScopeTime(String startTime, String endTime, String startRemindTime, String endRemindTime) {
		boolean isScope = false;
		try {
			long start = getTimeToMilliSecond(startTime);
			long end = getTimeToMilliSecond(endTime);
			long startRemind = getTimeToMilliSecond(startRemindTime);
			long endRemind = getTimeToMilliSecond(endRemindTime);
			if (startRemind <= start && start < endRemind)
				isScope = true;
			if (startRemind == start && end > endRemind)
				isScope = true;
			if (startRemind > start && end > endRemind)
				isScope = true;
			if (startRemind > start && end == endRemind)
				isScope = true;
			if (startRemind < end && end < endRemind)
				isScope = true;
			
		} catch (Exception e) {
			return false;
		}
		return isScope;
	}
	
	public static boolean isExpireTime(String startTime , int expiredays, String currentTime) {
		boolean isExpire = false;
		try {
			long start = getTimeToMilliSecond(startTime);
			long expireTime = 60*60*24*1000*expiredays;
			long time = getTimeToMilliSecond(currentTime);
			if(time-start <= expireTime) {
				isExpire = true;
			}
		} catch (Exception e) {
			return false;
		}
		return isExpire;
	}
	
	public static String[] getBeginAndEndTime(String start, String end, String time, int interval) throws Exception {
		List<String> list = TimeUtils.getTimeSepList(start, end, interval);
		String current = null;
		for(String str : list) {
			long t = TimeUtils.getTimeToMilliSecond(time);
			long s = TimeUtils.getTimeToMilliSecond(str);
			if (t >= s) {
				continue;
			} else {
				current = str;
				break;
			}
		}
		
		long stime = TimeUtils.getTimeToMilliSecond(current) - interval*60*1000;
		String startTime = getMilliSecondToTime(stime);
		String[] array = new String[2];
		array[0] = startTime;
		array[1] = current;
		return array;
	}
	
	public static boolean isCrossDay(String start, String end) {
		int s = Integer.parseInt(start.replaceAll(":", ""));
		int e = Integer.parseInt(end.replaceAll(":", ""));
		return s >= e;
	}
	
	public static String getCurrentStartTime(int interval) {
		String result = getRemindTime(interval);
		try {
			result = TimeUtils.getBeforeTime(result, interval);
		} catch (Exception e) {
		}
		return result;
	}
	
	public static String getRemindTime(int interval) {
		List<String> list = null;
		String result = null;
		try {
			list = TimeUtils.getTimeSepList(interval);
			String currentTime = TimeUtils.getCurrentTime();
			
			for(String time : list) {
				long t = TimeUtils.getTimeToMilliSecond(time);
				long c = TimeUtils.getTimeToMilliSecond(currentTime);
				if (t >= c) {
					result = time;
					break;
				} 
			}
			
			if (result == null) {
				result = getDefaultTime();
			}
		} catch (Exception e) {
			Log.error("时间格式转换失败", e);
		}
			
		return result;
	}
	
	public static String getDefaultTime() {
		return TimeUtils.getDifferOneDay() + " " + TIME;
	}
	
	public static void main(String[] args0) throws Exception {
		String start = "00:00:00";
		String start1 = "14:30:00";
//		String end = "2015-01-16 14:30:00";
//		String time = "2015-01-16 13:15:00";
//		System.out.println(getBeforeTime(start, 60));
//		System.out.println(getAfterTime(start, 20));
//		String[] array = TimeUtils.getBeginAndEndTime(start, end, time, 5);
//		System.out.println("分隔时间段：" + array[0] + " ~ " + array[1]);
		System.out.println(getTimeToMilliSecond(getMergeTime(start)));
		System.out.println(getTimeToMilliSecond(getMergeTime(start1)));
	}

	public static boolean isExpireTime(String startTime) {
		boolean isExpire = false;
		try {
			long start = getTimeToMilliSecond(startTime);
			long now = System.currentTimeMillis();
			if(start > now) {
				isExpire = true;
			}
		} catch (Exception e) {
			return false;
		}
		return isExpire;

	}
}
