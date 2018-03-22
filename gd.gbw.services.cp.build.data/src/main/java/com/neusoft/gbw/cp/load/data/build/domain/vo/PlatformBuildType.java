package com.neusoft.gbw.cp.load.data.build.domain.vo;

public enum PlatformBuildType {

	first_start_build,//平台首次运行
	
	period_del_task_build,//周期删除任务
	
	period_build,//周期任务构建（收测效果）
	
	burst_task_build,//突发频率任务
	
	leakage_task_build,//补采任务
	
	upload_stream,//下载录音
	
	auto_recover_data,//自动回收
	
	auto_monitor_inspect//站点巡检
}