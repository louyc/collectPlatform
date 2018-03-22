package com.neusoft.gbw.cp.build.domain.build.query;

import com.neusoft.gbw.cp.build.domain.build.audio.EffectHistoryQueryBuilder;
import com.neusoft.gbw.cp.build.domain.build.audio.EffectTaskSetBuilder;
import com.neusoft.gbw.cp.build.domain.build.quality.QualityHistoryQueryBuilder;
import com.neusoft.gbw.cp.build.domain.build.quality.QualityTaskSetBuilder;
import com.neusoft.gbw.cp.build.domain.build.spectrum.SpectrumHistoryQueryBuilder;
import com.neusoft.gbw.cp.build.domain.build.spectrum.SpectrumTaskSetBuilder;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.constants.ConfigVariable;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.core.collect.TaskPriority;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;

public class QueryV8SetBuilder {

	public Object buildQualityHistoryQuery(Object obj, TaskPriority taskPriority) {
		Task task = (Task) obj;
		Query query = buildBaseQuery(task,taskPriority);
		query.setQuery(QualityHistoryQueryBuilder.buildQualityKpiHistoryQuery(task));
		
		return query;
	}

	public Object buildQualityTaskSet(Object obj, TaskPriority taskPriority) {
		Task task = (Task) obj;
		Query query = buildBaseQuery(task,taskPriority);
		query.setQuery(QualityTaskSetBuilder.buildQualityKpiTaskSet(task));
		
		return query;
	}

	public Object buildSpectrumHistoryQuery(Object obj, TaskPriority taskPriority) {
		Task task = (Task) obj;
		Query query = buildBaseQuery(task,taskPriority);
		query.setQuery(SpectrumHistoryQueryBuilder.buildSpectrumHistoryQuery(task));
		
		return query;
	}

	public Object buildSpectrumTaskSet(Object obj, TaskPriority taskPriority) {
		Task task = (Task) obj;
		Query query = buildBaseQuery(task,taskPriority);
		query.setQuery(SpectrumTaskSetBuilder.buildSpectrumTaskSet(task));
		
		return query;
	}

	public Object buildStreamHistoryQuery(Object obj, TaskPriority taskPriority) {
		Task task = (Task) obj;
		Query query = buildBaseQuery(task,taskPriority);
		query.setQuery(EffectHistoryQueryBuilder.buildEffectStreamHistoryQuery(task));
		
		return query;
	}
	
	public Object buildStreamTaskSet(Object obj, TaskPriority taskPriority) {
		Task task = (Task) obj;
		Query query = buildBaseQuery(task,taskPriority);
		query.setQuery(EffectTaskSetBuilder.buildEffectSteamTaskSet(task));
		
		return query;
	}

	public Object buildOffsetHistoryQuery(Object obj, TaskPriority taskPriority) {
		Task task = (Task) obj;
		Query query = buildBaseQuery(task,taskPriority);
		query.setQuery(QualityHistoryQueryBuilder.buildQualityOffsetHistoryQuery(task));
		
		return query;
	}

	public Object buildOffsetTaskSet(Object obj, TaskPriority taskPriority) {
		Task task = (Task) obj;
		Query query = buildBaseQuery(task,taskPriority);
		query.setQuery(QualityTaskSetBuilder.buildQualityOffsetTaskSet(task));
		
		return query;
	}
	
	private static Query buildBaseQuery(Task task, TaskPriority taskPriority) {
		Query query = new Query();
		query.setMsgID(DataUtils.getMsgID(query)+"");
		query.setVersion(BuildConstants.XML_VERSION_8+"");
		query.setType(BuildConstants.MSG_DOWN_TYPE);
		query.setDateTime(DataUtils.getCurrentTime());
		query.setSrcCode(ConfigVariable.MSG_SRC_CODE);
		query.setDstCode(task.getTaskMonitor().getMonitor_code());
		query.setPriority(taskPriority.getCollectPriority()+"");
		
		return query;
	} 
}
