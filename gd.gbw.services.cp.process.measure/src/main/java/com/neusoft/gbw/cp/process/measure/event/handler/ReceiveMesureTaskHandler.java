package com.neusoft.gbw.cp.process.measure.event.handler;

import java.util.List;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.measure.service.CollectTaskDispatcher;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class ReceiveMesureTaskHandler implements BaseEventHandler {
	
	private CollectTaskDispatcher dispatcher = null;
	
	public ReceiveMesureTaskHandler() {
		dispatcher = new CollectTaskDispatcher();
	}

	@Override
	public String getTopicName() {
		return EventServiceTopic.TaskControl.LIST_COLLECT_EFFECT_TASK_TOPIC;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean processEvent(Object arg0) {
		List<CollectTask> taskList = (List<CollectTask>)arg0;
		if(taskList == null || taskList.size() == 0) {
			return false;
		}
		dispatcher.taskDispatch(taskList);
		Log.debug("measure收到的效果任务请求list"+taskList.size());
		return true;
	}
}
