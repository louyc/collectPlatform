package com.neusoft.gbw.cp.build.infrastructure.constants;

public class ConfigVariable {

	public static String MSG_SRC_CODE;
	
//	public static String UNIT_STATUS_FILE_PATH;
	
	public static int TRANSFER_APP_TYPE;
	
	public static int SYNT_DB_TIME;//同步数据库数据时间：单位 分钟
	
	public static int SYNT_DELETE_TASK_TIME;//同步数据库数据时间：单位 分钟
	
	public static int SITE_CHECK_TRANSFER_TIME;//同步数据库数据时间：单位 分钟
	
	public static int SITE_CHECK_TRANSFER_SWITCH;//站点连通性检测开关 // 
	
	public static int RECOVER_DATA_TIME_SWITCH;//回收任务数据开关 // 
	
	public static String MEASURE_RECORD_TIME;//收测单元录音时长

	public static int RECOVER_DATA_TIME;//回收任务数据时间：单位 分钟
	
	public static String RECOVER_DATA_UNIT;//回收任务数据时间单位 1：小时，0：分钟
	
	//回收任务执行回收当前时间提前多少分钟的数据 单位：分钟（回收不能回收当前时刻，因为当前时刻能能正在采集数据，没有入库回收不上来）
	public static int RECOVER_DATA_TIME_POINT;
	
	public static String TRANSFER_APP;
	
	public static int TAISHUN_SET_TASK_ENCODE_USE_SWITCH; //泰顺设置任务默认使用接收机开关 0、关闭，1、开启
	
	public static String TAISHUN_QUALITYTASKSET_ENCODE; //泰顺指标任务设置接收机
	
	public static String TAISHUN_STREAMTASKSET_ENCODE; //泰顺录音任务设置接收机
	
	public static String COLLECT_ZK_USE;	//备选采集方案按钮 0：停止    1：启动
	
	public static String INSPECT_MONITOR_TIME; //遥控站巡检 时间配置： 10: 10点开始巡检（24小时制）
	public static String INSPECT_MONITOR_UNIT; //遥控站巡检 次数：20  20分一次  即10点开始 巡检3次
}
