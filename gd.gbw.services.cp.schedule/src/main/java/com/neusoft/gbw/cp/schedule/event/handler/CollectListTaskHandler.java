package com.neusoft.gbw.cp.schedule.event.handler;

import java.util.List;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.schedule.service.TaskDispatchorService;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class CollectListTaskHandler implements BaseEventHandler{
	
	private TaskDispatchorService mDispatchorService = null;
	/**
	 * 接收List任务
	 * @param mDispatchorService
	 */
	public CollectListTaskHandler(TaskDispatchorService mDispatchorService) {
		this.mDispatchorService = mDispatchorService;
	}

	@Override
	public String getTopicName() {
		return EventServiceTopic.TaskControl.LIST_COLLECT_TASK_TOPIC;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean processEvent(Object arg0) {
		List<CollectTask> list = (List<CollectTask>)arg0;
		if(list == null || list.size() == 0) {
			return false;
		}
		Log.debug("schedule 接收到的list长度"+list.size());
		recieveData(list);
		return true;
	}
	
	private void recieveData(List<CollectTask> list) {
		for(CollectTask task : list) {
			mDispatchorService.recieveDate(task);
		}
	}
}
