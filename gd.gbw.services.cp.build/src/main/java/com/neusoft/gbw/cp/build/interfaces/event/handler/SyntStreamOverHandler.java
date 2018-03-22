package com.neusoft.gbw.cp.build.interfaces.event.handler;

import com.neusoft.gbw.cp.build.domain.build.runplan.TaskStreamBuilder;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class SyntStreamOverHandler implements BaseEventHandler{

	@Override
	public String getTopicName() {
		return EventServiceTopic.STREAM_TASK_OVER_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if (arg0 instanceof CollectTask) {
			dispose((CollectTask)arg0);
		}
		return true;
	}
	
	private void dispose(CollectTask task) {
		CollectTask overTask = TaskStreamBuilder.buildTaskStreamOver(task);
		ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.SINGLE_COLLECT_TASK_TOPIC, overTask);
	}
}
