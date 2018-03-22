package com.neusoft.np.arsf.common.util;

public class NMTimer {

	private long startTime;

	public NMTimer() {
		this.startTime = System.currentTimeMillis();
	}

	public NMTimer(long time) {
		this.startTime = time;
	}

	public void begin() {
		startTime = System.currentTimeMillis();
	}

	/**
	 * 计时，默认单位为秒
	 */
	public String lap() {
		long nowTime = System.currentTimeMillis();
		return "执行时间：" + (nowTime - startTime) / 1000 + "秒";
	}

	/**
	 * 计时
	 */
	public String lap(TimerUnit unit) {
		long nowTime = System.currentTimeMillis();
		return "执行时间：" + count(unit, nowTime - startTime) + unit;
	}

	/**
	 * 计时，以当前时间为开始时间，默认单位为秒
	 */
	public String end() {
		long nowTime = System.currentTimeMillis();
		String msg = "执行时间：" + (nowTime - startTime) / 1000 + "秒";
		startTime = nowTime;
		return msg;
	}

	/**
	 * 计时，以当前时间为开始时间
	 */
	public String end(TimerUnit unit) {
		long nowTime = System.currentTimeMillis();
		String msg = "执行时间：" + count(unit, nowTime - startTime) + unit;
		startTime = nowTime;
		return msg;
	}

	private static long count(TimerUnit unit, long time) {
		switch (unit) {
		case MILLISECOND:
			return time;
		case SECOND:
			return time / 1000;
		case MINUTE:
			return time / 60000;
		case HOUR:
			return time / 3600000;
		}
		return time;
	}

	public static void main(String[] args) throws InterruptedException {
		NMTimer timer = new NMTimer();
		Thread.sleep(3100);
		System.out.println(timer.lap(NMTimer.TimerUnit.SECOND));
		Thread.sleep(3100);
		System.out.println(timer.lap(NMTimer.TimerUnit.MILLISECOND));
		Thread.sleep(2100);
		System.out.println(timer.end(NMTimer.TimerUnit.MILLISECOND));
		Thread.sleep(1100);
		System.out.println(timer.end(NMTimer.TimerUnit.MINUTE));
	}

	public enum TimerUnit {
		MILLISECOND {
			@Override
			public String toString() {
				return "毫秒";
			}
		},
		SECOND {
			@Override
			public String toString() {
				return "秒";
			}
		},
		MINUTE {
			@Override
			public String toString() {
				return "分钟";
			}
		},
		HOUR {
			@Override
			public String toString() {
				return "小时";
			}
		}
	}
}
