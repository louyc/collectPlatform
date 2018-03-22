package com.neusoft.gbw.cp.schedule.constants;

public class ScheduleConstants {
	
	public static final int COLLECT_TASK_DISPATCHOR_QUEUE_SIZE = 1000;
	
	public static final String YEAR = "YEAR";
	public static final String MONTH = "MONTH";
	public static final String DAY = "DAY";
	public static final String HOUR = "HOUR";
	public static final String MINUTE = "MINUTE";
	public static final String SECOND = "SECOND";
	
	public static class ScheduleTopic {
		
		public static final String RECIEVE_DELAY_SCHEDULE_MSG_TOPIC = "RECIEVE_DELAY_SCHEDULE_MSG_TOPIC";//延时
		
		public static final String RECIEVE_TIME_REMIND_MSG_TOPIC = "RECIEVE_TIME_REMIND_MSG_TOPIC";//提醒接收主题
		
		/**
		 * 调度任务添加主题
		 */
		public static final String SCHEDULE_INCREMENTS_OBJ = "SCHEDULE_INCREMENTS_OBJ";

		/**
		 * 调度任务删除主题
		 */
		public static final String SCHEDULE_REMOVE_OBJ = "SCHEDULE_REMOVE_OBJ";
	}
}
