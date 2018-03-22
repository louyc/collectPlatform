package com.neusoft.gbw.cp.build.domain.build.quality;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityTaskSet;
import com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityTaskSetQualityIndex;
import com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetTaskSet;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;

public class QualityTaskSetV7Builder {
	
	private static Map<Integer, QualityTaskSetQualityIndex> qIndexMap = null;
	
	static {
		qIndexMap = new HashMap<Integer, QualityTaskSetQualityIndex>();
		qIndexMap.put(1, buildQualityIndex(1, "Level"));
		qIndexMap.put(3, buildQualityIndex(3, "AM-Modulation"));
		qIndexMap.put(5, buildQualityIndex(5, "FM-ModulationMax"));
	}

	public static QualityTaskSet buildQualityKpiTaskSet(Task kTask) {
		QualityTaskSet qTaskSet = new QualityTaskSet();
		qTaskSet.setAction(BuildConstants.ACTION_MEASURE_SET);
		qTaskSet.setTaskID(DataUtils.getTaskID(kTask)+"");
		qTaskSet.setEquCode(kTask.getTaskFreq().getReceiver_code()==null? "" : kTask.getTaskFreq().getReceiver_code());
		qTaskSet.setSampleNumber(kTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_NUMBER));
		qTaskSet.setUnit(kTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_CIRCLE));
		qTaskSet.setValidStartDateTime(kTask.getMeasureTask().getValidity_b_time());
		qTaskSet.setValidEndDateTime(kTask.getMeasureTask().getValidity_e_time());
		qTaskSet.addQualityTask(QualityTaskBuilder.buildQualityKpiTaskV7(kTask));
		for(QualityTaskSetQualityIndex index : getQualityIndexList(kTask))
			qTaskSet.addQualityTaskSet_QualityIndex(index);
		
		return qTaskSet;
	}
	
	public static OffsetTaskSet buildQualityOffsetTaskSet(Task oTask) {
		OffsetTaskSet oTaskSet = new OffsetTaskSet();
		oTaskSet.setAction(BuildConstants.ACTION_MEASURE_SET);
		oTaskSet.setTaskID(DataUtils.getTaskID(oTask)+"");
		oTaskSet.setEquCode(oTask.getTaskFreq().getReceiver_code()==null?"" : oTask.getTaskFreq().getReceiver_code());
		oTaskSet.setSampleNumber(oTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_NUMBER));
		oTaskSet.setUnit(oTask.getTaskConfAttr().get(BuildConstants.TASK_SAMPLE_CIRCLE));
		oTaskSet.setValidStartDateTime(oTask.getMeasureTask().getValidity_b_time());
		oTaskSet.setValidEndDateTime(oTask.getMeasureTask().getValidity_e_time());
		oTaskSet.setQualityTask(QualityTaskBuilder.buildQualityOffsetTaskV7(oTask));
		
		return oTaskSet;
	}
	
	protected static List<QualityTaskSetQualityIndex> getQualityIndexList(Task kTask) {
		Map<String, String> taskConfMap = kTask.getTaskConfAttr();
		List<QualityTaskSetQualityIndex> list = new ArrayList<QualityTaskSetQualityIndex>();
		if (taskConfMap.containsKey(BuildConstants.QualityTask.LEVEL_KPI)) {
			list.add(qIndexMap.get(1));
		}
		int band = kTask.getTaskFreq().getBand(); 
		if (band != 2 && taskConfMap.containsKey(BuildConstants.QualityTask.AM_MODULATION_KPI)) {
			list.add(qIndexMap.get(3));
		}
		if (band == 2 && taskConfMap.containsKey(BuildConstants.QualityTask.FM_MODULATION_KPI)) {
			list.add(qIndexMap.get(5));
		}
		return list;
	}
	
	private static QualityTaskSetQualityIndex buildQualityIndex(int type, String desc) {
		QualityTaskSetQualityIndex index = new QualityTaskSetQualityIndex();
		index.setType(type+"");
		index.setDesc(desc);
		return index;
	}
}
