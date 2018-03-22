package com.neusoft.gbw.cp.measure.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimeSplitUtils {
	
	private static final String TIME = "00:00:00";
	private static final int[] weekDays = {0,1,2,3,4,5,6};

	public static int getWeekofDate(String time) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(getTimeToMilliSecond(time));
		int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (week < 0)
			week = 0;
		return weekDays[week];
	}
	
	public static String getAfterTimeByDay(String time, int day) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(df.parse(time));
		cal.add(Calendar.DAY_OF_WEEK, day);
		return df.format(cal.getTime());
	}
	
	public static String getAfterTimeByMin(String time, int min) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(df.parse(time));
		cal.add(Calendar.MINUTE, min);
		return df.format(cal.getTime());
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
	
	public static String getMilliSecondToTime(long million) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(million);
	}
	
	public static int isScopeTime(String startTime, String endTime, String startRemindTime, String endRemindTime) {
		int index = 0;  //����0��?��أ�����0���ɹ���6��?����
		try {
			long start = getTimeToMilliSecond(startTime);
			long end = getTimeToMilliSecond(endTime);
			long startRemind = getTimeToMilliSecond(startRemindTime);
			long endRemind = getTimeToMilliSecond(endRemindTime);
			if (startRemind <= start && start < endRemind) {
				if (startRemind <= end && end <= endRemind) {
					index = 6;
				} else {
					index = 1;
				}
			} else if (startRemind == start && end > endRemind) {
				index = 2;
			} else if (startRemind > start && end > endRemind) {
				index = 3;
			} else if (startRemind > start && end == endRemind) {
				index = 4;
			} else if (startRemind < end && end < endRemind) {
				if (startRemind <= start && start <= endRemind) {
					index = 6;
				} else {
					index = 5;
				}
			}
		} catch (Exception e) {
			return index;
		}
		return index;
	}
	
	public static int isScopeHHTime(String startTime, String endTime, String overStartTime, String overEndTime) {
		int index = 0; 
		try {
			long start = getHHTimeToMilliSecond(startTime);
			long end = getHHTimeToMilliSecond(endTime);
			long startRemind = getHHTimeToMilliSecond(overStartTime);
			long endRemind = getHHTimeToMilliSecond(overEndTime);
			if (startRemind <= start && start < endRemind) {
				if (startRemind <= end && end <= endRemind) {
					index = 6;
				} else {
					index = 1;
				}
			} else if (startRemind == start && end > endRemind) {
				index = 2;
			} else if (startRemind > start && end > endRemind) {
				index = 3;
			} else if (startRemind > start && end == endRemind) {
				index = 4;
			} else if (startRemind < end && end < endRemind) {
				if (startRemind <= start && start <= endRemind) {
					index = 6;
				} else {
					index = 5;
				}
			}
		} catch (Exception e) {
			return index;
		}
		return index;
	}
	
	public static String getDefaultTime(String time) {
		return time + " " + TIME;
	}
	
	public static long getTimeToMilliSecond(String time) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.parse(time).getTime();
	}
	
	public static long getHHTimeToMilliSecond(String time) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		return df.parse(time).getTime();
	}
	
	public static boolean isSpanDay(String startTime, String endTime) throws Exception {
		int start = getWeekofDate(startTime);
		int end = getWeekofDate(endTime);
		return start != end;
	}
	
	public static String getTimeByWeek(String hmsTime, String startTime) {
		String ymdTime = startTime.split(" ")[0];
		return ymdTime + " " + hmsTime;
	}
	
	public static void main(String[] args) throws Exception {
//		String time = "2015-4-8 09:00:00";
//		String hmsTime = "22:21:02";
//		System.out.println(TimeSplitUtils.getHHTimeToMilliSecond(hmsTime));
//		System.out.println(TimeSplitUtils.getTimeByWeek(hmsTime, time));
//		System.out.println(TimeSplitUtils.getAfterTimeByDay(time, 2));
	}
}
