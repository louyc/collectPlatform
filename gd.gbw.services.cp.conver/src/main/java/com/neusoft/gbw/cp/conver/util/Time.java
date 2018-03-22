package com.neusoft.gbw.cp.conver.util;

public class Time {

	public static void main(String args[]) {
		System.out.println(timeToint("00:00:05"));
	}

	private String time;

	public Time(int i) {
		this.time = secToTime(i);

	}

	public Time(String str) {
		this.time = str;
	}

	public Time() {

	}

	public String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time < 0) {
			throw new IllegalArgumentException("時間參數不能小于0");
		}
		minute = time / 60;
		if (minute < 60) {
			second = time % 60;
			hour = minute / 60;
			timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":"
					+ unitFormat(second);
		} else {
			hour = minute / 60;
			if (hour > 99) {
				throw new IllegalArgumentException("時間參數過大");
			}
			minute = minute % 60;
			second = time - hour * 3600 - minute * 60;
			timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":"
					+ unitFormat(second);
		}

		return timeStr;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * Time类型的字符转化为int
	 * 
	 * @param str
	 * @return
	 */
	public static int timeToint(String str) {
		int j = 0;
		String[] s = str.split(":");
		if (s.length != 3) {
			throw new IllegalArgumentException("参数格式不对(00:00:00)" + str);
		}
		int array[] = new int[s.length];
		for (int i = 0; i < s.length; i++) {
			array[i] = Integer.parseInt(s[i]);
		}
		j = array[0] * 60 * 60 + array[1] * 60 + array[2];
		return j;
	}

	@Override
	public String toString() {
		return time;
	}

	public String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

}
