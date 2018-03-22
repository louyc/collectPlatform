package com.neusoft.gbw.cp.build.domain.build.spectrum;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;

public class SpectrumTaskSetBuilder {

	public static com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumTaskSet buildSpectrumTaskSet(Task spTask) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumTaskSet spTaskSet = new com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumTaskSet();
		spTaskSet.setAction(BuildConstants.ACTION_MEASURE_SET);
		spTaskSet.setTaskID(DataUtils.getTaskID(spTask) + "");
		spTaskSet.setEquCode(spTask.getTaskFreq().getReceiver_code() == null ? "" : spTask.getTaskFreq().getReceiver_code());
		spTaskSet.setSampleNumber(spTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_NUMBER) + "");
		spTaskSet.setUnit(spTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_CIRCLE));
		spTaskSet.setValidStartDateTime(spTask.getMeasureTask().getValidity_b_time());
		spTaskSet.setValidEndDateTime(spTask.getMeasureTask().getValidity_e_time());
		spTaskSet.setSpectrumTask(SpectrumTaskBuilder.buildSpectrumTask(spTask));

		return spTaskSet;
	}
	
	public static com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumTaskSet buildSpectrumTaskSetV5(Task spTask) {
		com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumTaskSet spTaskSet = new com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumTaskSet();
		spTaskSet.setAction(BuildConstants.ACTION_MEASURE_SET);
		spTaskSet.setTaskID(DataUtils.getTaskID(spTask) + "");
		spTaskSet.setEquCode(spTask.getTaskFreq().getReceiver_code() == null ? "" : spTask.getTaskFreq().getReceiver_code());
		spTaskSet.setSampleNumber(spTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_NUMBER) + "");
//		spTaskSet.setUnit(spTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_CIRCLE));
		spTaskSet.setValidStartDateTime(spTask.getMeasureTask().getValidity_b_time());
		spTaskSet.setValidEndDateTime(spTask.getMeasureTask().getValidity_e_time());
		spTaskSet.setSpectrumTask(SpectrumTaskBuilder.buildSpectrumTaskV5(spTask));

		return spTaskSet;
	}
	
	public static com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumTaskSet buildSpectrumTaskSetV6(Task spTask) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumTaskSet spTaskSet = new com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumTaskSet();
		spTaskSet.setAction(BuildConstants.ACTION_MEASURE_SET);
		spTaskSet.setTaskID(DataUtils.getTaskID(spTask) + "");
		spTaskSet.setEquCode(spTask.getTaskFreq().getReceiver_code() == null ? "" : spTask.getTaskFreq().getReceiver_code());
		spTaskSet.setSampleNumber(spTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_NUMBER) + "");
		spTaskSet.setUnit(spTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_CIRCLE));
		spTaskSet.setValidStartDateTime(spTask.getMeasureTask().getValidity_b_time());
		spTaskSet.setValidEndDateTime(spTask.getMeasureTask().getValidity_e_time());
		spTaskSet.setSpectrumTask(SpectrumTaskBuilder.buildSpectrumTaskV6(spTask));

		return spTaskSet;
	}
	
	public static com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumTaskSet buildSpectrumTaskSetV7(Task spTask) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumTaskSet spTaskSet = new com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumTaskSet();
		spTaskSet.setAction(BuildConstants.ACTION_MEASURE_SET);
		spTaskSet.setTaskID(DataUtils.getTaskID(spTask) + "");
		spTaskSet.setEquCode(spTask.getTaskFreq().getReceiver_code() == null ? "" : spTask.getTaskFreq().getReceiver_code());
		spTaskSet.setSampleNumber(spTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_NUMBER) + "");
		spTaskSet.setUnit(spTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_CIRCLE));
		spTaskSet.setValidStartDateTime(spTask.getMeasureTask().getValidity_b_time());
		spTaskSet.setValidEndDateTime(spTask.getMeasureTask().getValidity_e_time());
		spTaskSet.setSpectrumTask(SpectrumTaskBuilder.buildSpectrumTaskV7(spTask));

		return spTaskSet;
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
