package com.neusoft.gbw.cp.build.domain.build;

import java.util.Map;

import com.neusoft.gbw.cp.core.collect.TaskPriority;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskPriorityInfo;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskType;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.common.util.NPEvaluators;

public class CalTaskPriority {
	
	private static Map<String, String> priorityMap = ARSFToolkit.getConfiguration().getAllBusinessProperty("gbw_priority"); 

	/**
	 * 计算优先级分数
	 * @param info
	 * @return
	 */
	public static TaskPriority calculatePriority(TaskPriorityInfo info) {
		int taskTypePri = getTaskTypePriority(info.getType());              //任务权限
		int taskRolePri = getTaskRolePriority(info.getRole());              //角色权限
		int taskProPri = getTaskProtocolPriority(info.getTaskProType());    //协议类型的权限
		int baseProiority = info.getBasePriority();                         //基本权限
		
		int measureType = info.getRole().equals("system") ? 0 : 1;     
		long languagePri = getLanguagePriority(info.getLanguage());    //发射台的权限
		long stationPri = getStationPriority(info.getStation());       //语言的权限  0
		
		TaskPriority task = new TaskPriority();
		task.setCollectPriority(calCollectPriority(taskTypePri, taskRolePri, taskProPri, baseProiority));
		task.setMeasurePriority(calMeasurePriority(taskProPri,languagePri, stationPri, measureType));
		
		return task;
	}
	
	/**
	 * 非实时任务优先级计算规则，此规则会带入
	 * @param type  配置文件优先级 级别
	 * @param role  发射台的权限
	 * @param pro   协议接口权限
	 * @return
	 */
	private static int calCollectPriority(int type, int role, int pro, int baseProiority) {
		String exp = type + "*1000+" + role + "*100+" + pro + "*10" + baseProiority;
		return (int) NPEvaluators.express(exp);
//		return (int) 65100;
	}
	
	/**
	 * 收测单元优先级计算规则,该规则只控制采集bundle，不控制采集机，采集机所有实时音频访问都采用一个优先级
	 * @param language
	 * @param station  语言权限
	 * @return
	 */
	private static long calMeasurePriority(int pro, long language, long station, int measureType) {
		String exp = (pro + measureType) + "*10000+" + language + "*1000+" + station + "*100+10";
		return (long) NPEvaluators.express(exp);
//		return (long) 65100;
	}
	
	private static long getLanguagePriority(long id) {
		Map<Long, Long> map = DataMgrCentreModel.getInstance().getLanguageMap();
		if (map.containsKey(id))
			return map.get(id);
		return 0;
	}
	
	private static long getStationPriority(long id) {
		Map<Long, Long> map = DataMgrCentreModel.getInstance().getStationMap();
		if (map.containsKey(id))
			return map.get(id);
		return 0;
	}
	
	private static int getTaskTypePriority(TaskType type) {
		if (priorityMap.containsKey(type.name().toString()))
			return Integer.parseInt(priorityMap.get(type.name()));
		return 0;
	}
	
	private static int getTaskRolePriority(String role) {
		if (priorityMap.containsKey(role))
			return Integer.parseInt(priorityMap.get(role));
		return 0;
	}
	
	private static int getTaskProtocolPriority(String pro) {
		if (priorityMap.containsKey(pro))
			return Integer.parseInt(priorityMap.get(pro));
		return 0;
	}
}
