package com.neusoft.gbw.cp.build.domain.build.quality;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;

public class QualityHistoryQueryBuilder {

	private static Map<Integer, com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryQueryQualityIndex> qHistoryIndexMap = null;

	static {
		qHistoryIndexMap = new HashMap<Integer, com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryQueryQualityIndex>();
		qHistoryIndexMap.put(1, buildQualityHistoryIndex(1, "Level"));
		qHistoryIndexMap.put(3, buildQualityHistoryIndex(3, "AM-Modulation"));
		qHistoryIndexMap
				.put(5, buildQualityHistoryIndex(5, "FM-ModulationMax"));
		qHistoryIndexMap.put(8, buildQualityHistoryIndex(8, "BandWidth"));
	}

	public static com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryQuery buildQualityKpiHistoryQuery(Task kTask) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryQuery hisQuery = new com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryQuery();
		hisQuery.setTaskID(DataUtils.getTaskID(kTask) + "");
		hisQuery.setEquCode(kTask.getTaskFreq().getReceiver_code() == null ? ""
				: kTask.getTaskFreq().getReceiver_code());
		hisQuery.setFreq(kTask.getTaskFreq().getFreq());
		hisQuery.setBand(kTask.getTaskFreq().getBand() + "");
		hisQuery.setSampleNumber(kTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_NUMBER));
		hisQuery.setUnit(kTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_CIRCLE));
//		hisQuery.setStartDateTime(kTask.getMeasureTask().getValidity_b_time());
//		hisQuery.setEndDateTime(kTask.getMeasureTask().getValidity_e_time());
		hisQuery.setStartDateTime(kTask.getMeasureTask().getRecover_b_time());
		hisQuery.setEndDateTime(kTask.getMeasureTask().getRecover_e_time());
		for (com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryQueryQualityIndex index : getQualityHistoryIndexList(kTask))
			hisQuery.addQualityHistoryQuery_QualityIndex(index);

		return hisQuery;
	}

	public static com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetHistoryQuery buildQualityOffsetHistoryQuery(Task oTask) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetHistoryQuery hisQuery = new com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetHistoryQuery();
		hisQuery.setTaskID(DataUtils.getTaskID(oTask) + "");
		hisQuery.setEquCode(oTask.getTaskFreq().getReceiver_code() == null ? ""
				: oTask.getTaskFreq().getReceiver_code());
		hisQuery.setFreq(oTask.getTaskFreq().getFreq());
		hisQuery.setBand(oTask.getTaskFreq().getBand() + "");
		hisQuery.setSampleNumber(oTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_NUMBER));
		hisQuery.setUnit(oTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_CIRCLE));
//		hisQuery.setStartDateTime(kTask.getMeasureTask().getValidity_b_time());
//		hisQuery.setEndDateTime(kTask.getMeasureTask().getValidity_e_time());
		hisQuery.setStartDateTime(oTask.getMeasureTask().getRecover_b_time());
		hisQuery.setEndDateTime(oTask.getMeasureTask().getRecover_e_time());
		return hisQuery;
	}

	protected static List<com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryQueryQualityIndex> getQualityHistoryIndexList(
			Task kTask) {
		Map<String, String> taskConfMap = kTask.getTaskConfAttr();
		List<com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryQueryQualityIndex> list = new ArrayList<com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryQueryQualityIndex>();
		if (taskConfMap.containsKey(BuildConstants.QualityTask.LEVEL_KPI)) {
			list.add(qHistoryIndexMap.get(1));
		}
		int band = kTask.getTaskFreq().getBand(); 
		if (band != 2 && taskConfMap.containsKey(BuildConstants.QualityTask.AM_MODULATION_KPI)) {
			list.add(qHistoryIndexMap.get(3));
		}
		if (band == 2 && taskConfMap.containsKey(BuildConstants.QualityTask.FM_MODULATION_KPI)) {
			list.add(qHistoryIndexMap.get(5));
		}
		if (taskConfMap.containsKey(BuildConstants.QualityTask.BANDWIDTH_KPI)) {
			list.add(qHistoryIndexMap.get(8));
		}

		return list;
	}

	private static com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryQueryQualityIndex buildQualityHistoryIndex(
			int type, String desc) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryQueryQualityIndex index = new com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityHistoryQueryQualityIndex();
		index.setType(type + "");
		index.setDesc(desc);
		return index;
	}

	// private static Date getStartDateTime(Task kTask) {
	// try {
	// return
	// NMDateUtil.convertStringToDate(kTask.getMeasureTask().getValidity_b_time());
	// } catch (ParseException e) {
	// return null;
	// }
	// }
	//
	// private static Date getEndDateTime(Task kTask) {
	// try {
	// return
	// NMDateUtil.convertStringToDate(kTask.getMeasureTask().getValidity_b_time());
	// } catch (ParseException e) {
	// return null;
	// }
	// }
}
