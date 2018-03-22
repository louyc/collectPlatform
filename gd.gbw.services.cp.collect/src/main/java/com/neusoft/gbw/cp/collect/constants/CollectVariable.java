package com.neusoft.gbw.cp.collect.constants;

public class CollectVariable {

	public static int READ_TIME_QUEUE_SIZE;
	
	public static int PLAN_AND_CYCLE_QUEUE_SIZE;
	
	public static int RECOVERY_DATE_QUEUE_SIZE;
	
	public static int REAL_TIME_COLLECT_TASK_THREAD_POLL_SIZE;
	
	public static int PLAN_CYCLE_COLLECT_TASK_THREAD_POLL_SIZE;
	/**
	 * 回收数据线程池
	 */
	public static int RECOVERY_DATE_THREAD_POLL_SIZE;
	
	/**
	 * 回收数据的servlet寻址串
	 */
	public static String RECOVERY_DATE_SERVLET_SPLIT_STRING;
	
	public static int COLLECT_TIME_OUT;
	
	/**
	 * 采集间隔时间，单位：毫秒
	 */
	public static int COLLECT_SEP_TIME;
}
