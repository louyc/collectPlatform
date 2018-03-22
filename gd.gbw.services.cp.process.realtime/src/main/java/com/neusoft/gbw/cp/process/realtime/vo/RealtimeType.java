package com.neusoft.gbw.cp.process.realtime.vo;

public enum RealtimeType {
	/**
	 * 客户端连接
	 */
	client_stream,
	/**
	 * 设备状态
	 */
	equ_status,
	/**
	 * 客户端连接断开
	 */
	client_stop,
	/**
	 * 版本查询
	 */
	program_command,
	/**
	 * 实时指标
	 */
	quality_realtime,
	/**
	 * 实时频谱
	 */
	spectrum_realtime,
	/**
	 * 设备初始化设置
	 */
	param_init_set,
	/**
	 * 实时音频
	 */
	real_stream,
	
//	real_record,
	/**
	 * 手动任务
	 */
	manual_task,
	/**
	 * 手动回收
	 */
	manual_recover,
	/**
	 * 下载录音文件
	 */
	upload_stream
}
