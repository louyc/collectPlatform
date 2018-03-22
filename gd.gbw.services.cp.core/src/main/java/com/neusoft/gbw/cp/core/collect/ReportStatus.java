package com.neusoft.gbw.cp.core.collect;

public enum ReportStatus {
	date_collect_success,
	date_collect_time_out,//采集超时
	date_no_collect_time_out,  //未采集超时
	date_collect_error,        //采集失败（解析返回结果错误）
	data_collect_null,         //采集回收为空
	date_collect_active_report,//数据主动上报
	date_collect_no_found_task,//未找到下发任务
	date_collect_occupy,	   //采集任务下发接收机后被占用   
	
	collect_task_analytical_failure,//采集任务解析失败
	collect_task_send_failure,//采集任务发送失败
	transfer_base_info_validate_failure//通信基础信息验证失败
	
}
