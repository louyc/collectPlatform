package com.neusoft.gbw.cp.process.measure.event.handler;

import java.util.List;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.measure.context.CollectTaskModel;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class RecordFileRecoverlTaskHandler implements BaseEventHandler {
	
	@Override
	public String getTopicName() {
		return EventServiceTopic.MANUAL_RECOVER_RECORD_FILE_PROCESS_TOPIC;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean processEvent(Object arg0) {
		List<CollectTask> taskList = (List<CollectTask>)arg0;
		if(taskList == null || taskList.size() == 0) {
			return false;
		}
		Log.debug("开启录音文件回收，taskSize=" +  taskList.size());
		CollectTaskModel.getInstance().setRecordSize(taskList.get(0), taskList.size(), System.currentTimeMillis());
		return true;
	}
}
