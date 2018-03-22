package com.neusoft.gbw.cp.collect.constants;

/**
* 项目名称: IT监管采集平台<br>
* 模块名称: 采集平台服务<br>
* 功能描述: 采集存储公共的常量<br>
* 创建日期: 2014-12-10<br>
* 版权信息: Copyright (c) 2014<br>
* 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
* @author <a href="mailto: jia.h@neusoft.com">贾浩</a>
* @version v1.0
* <pre>
* 修改历史
*   序号      日期          修改人       修改原因
*    1   2014-12-10   贾浩           创建
* </pre>
 */
public final class Constants {
	
	public static final int EQU_QUEUE_SIZE = 1000;
	
	public static final int FORCE_QUEUE_SIZE = 2000;
	
	public static final int OCCUP_QUEUE_SIZE = 2000;
	
	public static final int PRIORITY_CTL_DATA_QUEUE = 2000;
	
	public static final String START_FORCE = "Start";
	
	public static final String STOP_FORCE = "Stop";
	
	public static final String IS_FORCE = "1"; //强制占用
	
	public static final String NO_FORCE = "0"; //正常
	
	public static class QueueName {
//		public static final String SYNT_STATUS_QUEUE = "同步状态数据队列";
//		public static final String FORCE_TASK_QUEUE = "强制占用接收机数据队列";
//		public static final String NO_OCCUP_QUEUE = "非占用接收机数据队列";
//		public static final String EQU_TASK_QUEUE = "采集任务下发队列";
		public static final String STATION_DIS_QUEUE = "站点分发队列";
		public static final String STATION_CHANGE_STATUS_QUEUE = "改变状态队列";
		public static final String EQU_COLLECT_QUEUE = "接收机采集队列";
	}
	
	public static class HandlerName {
		public static final String EQU_CTL_THREAD_NAME = "站点分发控制线程";
		public static final String EQU_OCCUP_ASSIGN_THREAD_NAME = "占用接收机接收分配线程";
		public static final String EQU_OTHER_ASSIGN_THREAD_NAME = "非占用接收机接收分配线程";
		public static final String SYNT_CHANGE_THREAD_NAME = "同步改变状态线程";
		public static final String EQU_COLLECT_THREAD_NAME = "接收机采集线程";
		
//		public static final String EQU_FORCE_CTL_THREAD_NAME = "站点强制分发控制线程";
//		public static final String SYS_STATUS_THREAD_NAME = "同步设备状态线程";
//		public static final String EQU_NO_OCCUP_THREAD_NAME="非占用接收机任务线程";
//		public static final String EQU_COLLECT_HANDLER_NAME = "采集任务发送控制器";
	}
	
	public static class CompareType {
		public static final int TASK_EXECUTE = 1;
		public static final int TASK_WAIT = 0;
	}
}
