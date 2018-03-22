package com.neusoft.gbw.cp.core;

public class EventServiceTopic {
	//数据采集主题
	public static final String COLLECT_TASK_TOPIC = "COLLECT_TASK_TOPIC";
	//站点设备信息同步主题
	public static final String SYNT_MONITOR_DEVICE_TOPIC = "SYNT_MONITOR_DEVICE_TOPIC";
	//周期任务接收主题
	//public static final String NO_REAL_COLLECT_TASK_TOPIC = "NO_REAL_COLLECT_TASK_TOPIC";
//	//删除任务接收主题
//	public static final String DEL_COLLECT_TASK_TOPIC = "DEL_COLLECT_TASK_TOPIC";
	//接收消息主题
	public static final String RECEIVE_MSG_TOPIC = "RECEIVE_MSG_TOPIC";
	//采集任务发送失败消息主题
	public static final String FAILURE_COLLECT_TASK_TOPIC = "FAILURE_COLLECT_TASK_TOPIC";
	//采集回收数据上报主题
	public static final String REPORT_COLLECT_DATA_TOPIC = "REPORT_COLLECT_DATA_TOPIC";
	//采集数据业务处理主题
	public static final String COLLECT_DATA_PROCESS_TOPIC = "COLLECT_DATA_PROCESS_TOPIC";
	//数据存储主题
	public static final String STORE_DATA_TOPIC = "STORE_DATA_TOPIC";
	//录音完成后通知构建主题
	public static final String STREAM_TASK_OVER_TOPIC = "STREAM_TASK_OVER_TOPIC";
	//播音任务处理主题
	public static final String STREAM_RECORD_TASK_POCESS_TOPIC = "STREAM_RECORD_TASK_POCESS_TOPIC";
	//强占接收机消息
	public static final String COLLECT_FORCE_MGS_TOPIC = "COLLECT_FORCE_MGS_TOPIC";
	//socket配置服务主题
	public static final String SOCKET_SERVICE_TOPIC = "SOCKET_SERVICE_TOPIC";
	//手动设置任务处理服务
	public static final String MANUAL_SET_TASK_PROCESS_TOPIC = "MANUAL_SET_TASK_PROCESS_TOPIC";
	//自动设置任务处理服务
	public static final String MANUAL_AUTO_SET_TASK_PROCESS_TOPIC = "MANUAL_AUTO_SET_TASK_PROCESS_TOPIC";
	//手动回收任务处理服务
	public static final String MANUAL_RECOVER_TASK_PROCESS_TOPIC = "MANUAL_RECOVER_TASK_PROCESS_TOPIC";
	//手动录音文件回收任务处理服务
	public static final String MANUAL_RECOVER_RECORD_FILE_PROCESS_TOPIC = "MANUAL_RECOVER_RECORD_FILE_PROCESS_TOPIC";
	//任务管理任务操作（下发，删除，修改，），数据库更新主图、此主题包含定时同步
	public static final String MANUAL_TASK_SYNT_DB_TOPIC = "MANUAL_TASK_SYNT_DB_TOPIC";
	//任务管理任务操作，定时删除设置类任务
	public static final String MANUAL_AUTO_DELETE_TASK_TOPIC = "MANUAL_AUTO_DELETE_TASK_TOPIC";
	//站点连通性监测以及软件存活状态校验
	public static final String SITE_CHECK_TRANSFER_TOPIC = "SITE_CHECK_TRANSFER_TOPIC";
	//同步采集点手动任务的code对应表名称主题
	public static final String QUALITY_TYPE_TOPIC = "QUALITY_TYPE_TOPIC";
	//同步告警类型字典数据主题
	public static final String AlARM_TYPE_TOPIC = "AlARM_TYPE_TOPIC";
	//下载音频文件的主题
	public static final String UPLOAD_STREAM_FILE_TOPIC = "UPLOAD_STREAM_FILE_TOPIC";
	//同步FTP服务器相关信息主题
	public static final String FTP_SERVER_TOPIC = "FTP_SERVER_TOPIC";
	//同步录音存储路径主题
	public static final String RECORD_ADDRESS_TOPIC = "RECORD_ADDRESS_TOPIC";
	//同步实时收测单元更新完成状态的站点信息主题
	public static final String REAL_MEASURE_SITE_TOPIC = "REAL_MEASURE_SITE_TOPIC";
	//同步实时收测单元运行图关联台名信息主题
	public static final String REAL_MEASURE_STATION_TOPIC = "REAL_MEASURE_STATION_TOPIC";
	//任务定时回收，回收当前时间前一天的任务数据
	public static final String RECOVER_TASK_DATA_TOPIC = "RECOVER_TASK_DATA_TOPIC";
	//任务定时回收，回收数据比对去重主题（数据库中存储昨天的数据）
	public static final String RECOVER_DATA_REPEAT_TOPIC = "RECOVER_DATA_REPEAT_TOPIC";
	//采集音频录音的请求主题
	public static final String COLLECT_STREAM_REQUEST_TOPIC = "COLLECT_STREAM_REQUEST_TOPIC";
	//收测服务采集音频的应答主题
	public static final String COLLECT_MEASURE_STREAM_RESPONSE_TOPIC = "COLLECT_MEASURE_STREAM_RESPONSE_TOPIC";
	//实时服务采集音频的应答主题
	public static final String COLLECT_REALTIME_STREAM_RESPONSE_TOPIC = "COLLECT_REALTIME_STREAM_RESPONSE_TOPIC";
	//巡检指令处理主题
	public static final String COLLECT_INSPECT_TASK_PROCESS_TOPIC = "COLLECT_INSPECT_TASK_PROCESS_TOPIC";
	//故障告警发送给NP5.3采集平台主题
	public static final String COLLECT_ALARM_TOPIC = "COLLECT_ALARM_TOPIC";
	//站点程序存活状态校验
	public static final String MONITOR_SOFT_ONLINE_STATUS_TOPIC = "MONITOR_SOFT_ONLINE_STATUS_TOPIC";
	//站点告警处理主题
	public static final String MONITOR_ALARM_DISPOSE_TOPIC = "MONITOR_ALARM_DISPOSE_TOPIC";
	//站点状态通知处理主题（用于更新站点Map，提供选择工作站点依据）
	public static final String MONITOR_STATUS_NOTIFY_TOPIC = "MONITOR_STATUS_NOTIFY_TOPIC";
	//jms发送主题
	public static final String JMS_SEND_MSG_TOPIC = "JMS_SEND_MSG_TOPIC";
	
	/**
	 * 任务控制分发服务主题 
	 */
	public static class TaskControl {
		
		public static final String LIST_COLLECT_TASK_TOPIC = "LIST_COLLECT_TASK_TOPIC";
		
		public static final String LIST_COLLECT_EFFECT_TASK_TOPIC = "LIST_COLLECT_EFFECT_TASK_TOPIC"; //针对效果任务  创建和下发并行导致更新问题 新加主题  20160818
		
		public static final String SINGLE_COLLECT_TASK_TOPIC = "SINGLE_COLLECT_TASK_TOPIC";
		
		public static final String DEL_COLLECT_TASK_TOPIC = "DEL_COLLECT_TASK_TOPIC";
		
		public static final String TIME_REMIND_SET_TOPIC = "TIME_REMIND_SET_TOPIC";
	}
	
	
	/**
	 * REST通信主题
	 */
	public static class RestTopic {
		
//		public static final String REST_SYNT_REALTIME_STREAM = "REST_SYNT_getRealTimeStream";
//		
//		public static final String REST_SYNT_REALTIME_RECORD = "REST_SYNT_getRealTimeRecord";
		
		public static final String REST_SYNT_UPDATE_TASK = "REST_SYNT_editTaskEvents";
		
		public static final String REST_SYNT_DELETE_TASK = "REST_SYNT_deleteTaskEvents";
		
		public static final String REST_SYNT_SET_TASK = "REST_SYNT_sendTaskEvents";
		
		public static final String REST_SYNT_RECOVER_TASK = "REST_SYNT_recoveryTaskRecordEvents";
		
		public static final String REST_SYNT_DROP_CLIENT_TASK = "REST_SYNT_dropMonitorClientLinks";
		
		public static final String REST_SYNT_CONTROL_MONITOR = "REST_SYNT_controlMonitor";
		
		public static final String REST_SYNT_EQU_ALARM_SET = "REST_SYNT_saveMonitorDev";
		
		public static final String REST_SYNT_EQU_INIT_SET = "REST_SYNT_saveMonitorBase";
		
		public static final String REST_SYNT_QUALITY_ALARM_SET= "REST_SYNT_saveMonitorKpi";
		
	}
}
