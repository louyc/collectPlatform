package com.neusoft.gbw.cp.build.domain.build.audio;

import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;

public class EffectHistoryQueryBuilder {

	public static com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamHistoryQuery buildEffectStreamHistoryQuery(Task sTask) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamHistoryQuery streamQuery = new com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamHistoryQuery();
//		streamQuery.setTaskID(DataUtils.getTaskID(sTask) + "");  暂时把任务id 屏蔽掉   lyc 20170417
		streamQuery.setTaskID("");
		streamQuery.setEquCode(sTask.getTaskFreq().getReceiver_code() == null ? "" : sTask.getTaskFreq().getReceiver_code());
		streamQuery.setFreq(sTask.getTaskFreq().getFreq());
		streamQuery.setBand(sTask.getTaskFreq().getBand() + "");
		streamQuery.setStartDateTime(sTask.getMeasureTask().getRecover_b_time());
		streamQuery.setEndDateTime(sTask.getMeasureTask().getRecover_e_time());
		return streamQuery;
	}
	
	public static com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamHistoryQuery buildEffectStreamHistoryQueryV7(Task sTask) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamHistoryQuery streamQuery = new com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamHistoryQuery();
//		streamQuery.setTaskID(DataUtils.getTaskID(sTask) + ""); 暂时把任务id 屏蔽掉   lyc 20170417
		streamQuery.setTaskID("");
		streamQuery.setEquCode(sTask.getTaskFreq().getReceiver_code() == null ? "" : sTask.getTaskFreq().getReceiver_code());
		streamQuery.setFreq(sTask.getTaskFreq().getFreq());
		streamQuery.setBand(sTask.getTaskFreq().getBand() + "");
		streamQuery.setStartDateTime(sTask.getMeasureTask().getRecover_b_time());
		streamQuery.setEndDateTime(sTask.getMeasureTask().getRecover_e_time());
		return streamQuery;
	}
	
	public static com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamHistoryQuery buildEffectStreamHistoryQueryV5(Task sTask) {
		com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamHistoryQuery streamQuery = new com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamHistoryQuery();
//		streamQuery.setTaskID(DataUtils.getTaskID(sTask) + "");   暂时把任务id 屏蔽掉   lyc 20170417
		streamQuery.setTaskID("");
		streamQuery.setEquCode(sTask.getTaskFreq().getReceiver_code() == null ? "" : sTask.getTaskFreq().getReceiver_code());
		streamQuery.setFreq(sTask.getTaskFreq().getFreq());
		streamQuery.setBand(sTask.getTaskFreq().getBand() + "");
		streamQuery.setStartDateTime(sTask.getMeasureTask().getRecover_b_time());
		streamQuery.setEndDateTime(sTask.getMeasureTask().getRecover_e_time());
		return streamQuery;
	}
	
	public static com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamHistoryQuery buildEffectStreamHistoryQueryV6(Task sTask) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamHistoryQuery streamQuery = new com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamHistoryQuery();
//		streamQuery.setTaskID(DataUtils.getTaskID(sTask) + "");    暂时把任务id 屏蔽掉   lyc 20170417
		streamQuery.setTaskID("");
		streamQuery.setEquCode(sTask.getTaskFreq().getReceiver_code() == null ? "" : sTask.getTaskFreq().getReceiver_code());
		streamQuery.setFreq(sTask.getTaskFreq().getFreq());
		streamQuery.setBand(sTask.getTaskFreq().getBand() + "");
		streamQuery.setStartDateTime(sTask.getMeasureTask().getRecover_b_time());
		streamQuery.setEndDateTime(sTask.getMeasureTask().getRecover_e_time());
		return streamQuery;
	}

}
