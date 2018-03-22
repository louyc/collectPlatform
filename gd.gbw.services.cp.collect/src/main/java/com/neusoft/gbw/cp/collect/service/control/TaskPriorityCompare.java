package com.neusoft.gbw.cp.collect.service.control;

import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.TaskPriority;

public class TaskPriorityCompare {

	/**
	 * Type=1 表示需要进行顶掉当前运行任务
	 * Type=0 表示需要进行等待当前运行任务完成
	 * @param curTask
	 * @param lastTask
	 * @return
	 */
	public static int compare(CollectTask curTask, CollectTask lastTask) {
		int result = 0;
		TaskPriority curTaskPriority = curTask.getTaskPriority();
		TaskPriority lastTaskPriority = lastTask.getTaskPriority();
		
		long curColPri = curTaskPriority.getMeasurePriority();
		long lastColPri = lastTaskPriority.getMeasurePriority();
		
		//表示临时任务, 比较优先级
		if (curColPri > lastColPri) 
			result = 1;
		
		return result;
	}
}
