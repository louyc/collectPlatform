package com.neusoft.gbw.cp.core.business;

public enum BusinessTaskType {

	measure_manual_set,//手动任务设置
	
	measure_auto,//自动任务（效果类任务）
	
	measure_manual_del,//手动删除
	
	measure_manual_recover,//手动回收
	
	measure_realtime,//实时类任务
	
	measure_real_record,//实时录音
	
	measure_report,//上报业务
	
	measure_online,//在线监听
	
	measure_inspect//巡检任务
}
