package com.neusoft.gbw.cp.core.protocol;
/**
 * v8接口协议
 * @author jh
 *
 */
public enum ProtocolType {

	QualityAlarmHistoryQuery,   //指标报警历史查询
	
	QualityAlarmParamSet,   //指标报警参数设置
	
	QualityHistoryQuery,  //指标历史查询
	
	QualityRealtimeQuery,  //指标实时查询
	
	QualityTaskSet,  //指标收测任务设置
	
	SpectrumHistoryQuery,   //频谱历史查询
	
	SpectrumRealtimeQuery,  //频谱实时查询
	
	SpectrumTaskSet,  //频谱任务设置
	
	StreamHistoryQuery,  //录音历史查询
	
	StreamRealtimeQuery,  //音频实时查询
	
	StreamTaskSet,  //录音任务设置
	
	OffsetHistoryQuery,  //频偏历史查询
	
	OffsetTaskSet,  //频偏测量任务设置
	
	VideoRealtimeQuery,  //视频实时查询
	
	EquAlarmHistoryQuery,  //
	
	EquAlarmParamSet,  //
	
	EquStatusRealtimeQuery,  //
	
	EquInitParamSet,  //
	
	EquLogHistoryQuery,  // 设备历史日志查询
	
	ReceiverControl,  //接收机控制
	
	TaskDelete,  //任务删除
	
	TaskStatusQuery,  //任务执行状态查询
	
	FileQuery,  // 文件查询
	
	FileRetrieve,  //文件获取
	
	ProgramCommand,  //版本查询
	
	StreamRealtimeClientQuery,  //客户端连接查询
	
	StreamRealtimeClientStop  //中断客户端音频连接
	
}
