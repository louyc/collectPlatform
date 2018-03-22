package com.neusoft.gbw.cp.build.domain.build.audio;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;

public class EffectTaskSetBuilder {

	public static com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamTaskSet buildEffectSteamTaskSet(Task sTask) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamTaskSet sTaskSet = new com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamTaskSet();
		sTaskSet.setAction(BuildConstants.ACTION_MEASURE_SET);
		sTaskSet.setTaskID(DataUtils.getTaskID(sTask) + "");
//		sTaskSet.setEquCode(sTask.getTaskFreq().getReceiver_code() == null ? "" : sTask.getTaskFreq().getReceiver_code());
		sTaskSet.setEquCode("");
		sTaskSet.setEncode(BuildConstants.MPEG2_ENCODE);
		sTaskSet.setBps(sTask.getTaskConfAttr().get(BuildConstants.COLLECT_TASK_BPS));
		sTaskSet.setValidStartDateTime(sTask.getMeasureTask().getValidity_b_time());
		sTaskSet.setValidEndDateTime(sTask.getMeasureTask().getValidity_e_time());
		sTaskSet.setStreamTask(EffectTaskBuilder.buildStreamTask(sTask));

		return sTaskSet;
	}
	
	public static com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamTaskSet buildEffectSteamTaskSetV5(Task sTask) {
		com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamTaskSet sTaskSet = new com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamTaskSet();
		sTaskSet.setAction(BuildConstants.ACTION_MEASURE_SET);
		sTaskSet.setTaskID(DataUtils.getTaskID(sTask) + "");
//		sTaskSet.setEquCode(sTask.getTaskFreq().getReceiver_code() == null ? "" : sTask.getTaskFreq().getReceiver_code());
		sTaskSet.setEquCode("");
		sTaskSet.setEncode(BuildConstants.MPEG2_ENCODE);
		sTaskSet.setBps(sTask.getTaskConfAttr().get(BuildConstants.COLLECT_TASK_BPS));
		sTaskSet.setValidStartDateTime(sTask.getMeasureTask().getValidity_b_time());
		sTaskSet.setValidEndDateTime(sTask.getMeasureTask().getValidity_e_time());
		sTaskSet.setStreamTask(EffectTaskBuilder.buildStreamTaskV5(sTask));

		return sTaskSet;
	}
	
	public static com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamTaskSet buildEffectSteamTaskSetV6(Task sTask) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamTaskSet sTaskSet = new com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamTaskSet();
		sTaskSet.setAction(BuildConstants.ACTION_MEASURE_SET);
		sTaskSet.setTaskID(DataUtils.getTaskID(sTask) + "");
//		sTaskSet.setEquCode(sTask.getTaskFreq().getReceiver_code() == null ? "" : sTask.getTaskFreq().getReceiver_code());
		sTaskSet.setEquCode("");
		sTaskSet.setEncode(BuildConstants.MPEG2_ENCODE);
//		sTaskSet.setBps(sTask.getTaskFreq().getCode_rate() + "");
		sTaskSet.setBps(sTask.getTaskConfAttr().get(BuildConstants.COLLECT_TASK_BPS));
		sTaskSet.setValidStartDateTime(sTask.getMeasureTask().getValidity_b_time());
		sTaskSet.setValidEndDateTime(sTask.getMeasureTask().getValidity_e_time());
		sTaskSet.setStreamTask(EffectTaskBuilder.buildStreamTaskV6(sTask));

		return sTaskSet;
	}
	
	public static com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamTaskSet buildEffectSteamTaskSetV7(Task sTask) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamTaskSet sTaskSet = new com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamTaskSet();
		sTaskSet.setAction(BuildConstants.ACTION_MEASURE_SET);
		sTaskSet.setTaskID(DataUtils.getTaskID(sTask) + "");
//		sTaskSet.setEquCode(sTask.getTaskFreq().getReceiver_code() == null ? "" : sTask.getTaskFreq().getReceiver_code());
		sTaskSet.setEquCode("");
		sTaskSet.setEncode(BuildConstants.MPEG2_ENCODE);
//		sTaskSet.setBps(sTask.getTaskFreq().getCode_rate() + "");
		sTaskSet.setBps(sTask.getTaskConfAttr().get(BuildConstants.COLLECT_TASK_BPS));
		sTaskSet.setValidStartDateTime(sTask.getMeasureTask().getValidity_b_time());
		sTaskSet.setValidEndDateTime(sTask.getMeasureTask().getValidity_e_time());
		sTaskSet.setStreamTask(EffectTaskBuilder.buildStreamTaskV7(sTask));

		return sTaskSet;
	}

	// private static Date getValidStartDateTime(Task kTask) {
	// try {
	// return
	// NMDateUtil.convertStringToDate(kTask.getMeasureTask().getValidity_b_time());
	// } catch (ParseException e) {
	// return null;
	// }
	// }
	//
	// private static Date getValidEndDateTime(Task kTask) {
	// try {
	// return
	// NMDateUtil.convertStringToDate(kTask.getMeasureTask().getValidity_e_time());
	// } catch (ParseException e) {
	// return null;
	// }
	// }

}
