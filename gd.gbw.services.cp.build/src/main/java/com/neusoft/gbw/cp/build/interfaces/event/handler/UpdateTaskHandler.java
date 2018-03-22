package com.neusoft.gbw.cp.build.interfaces.event.handler;

import com.neusoft.gbw.cp.build.application.TaskReceiveMgr;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.load.data.build.domain.vo.MessageTask;
import com.neusoft.gbw.cp.load.data.build.domain.vo.MessageType;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class UpdateTaskHandler implements BaseEventHandler{

	private TaskReceiveMgr mgr = null;
	
	public UpdateTaskHandler(TaskReceiveMgr mgr) {
		this.mgr = mgr;
	}

	@Override
	public String getTopicName() {
		return EventServiceTopic.RestTopic.REST_SYNT_UPDATE_TASK;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if (arg0 instanceof String) {
			dispose((String)arg0);
		}
		return true;
	}
	
	private void dispose(String msg) {
		MessageTask task = new MessageTask();
		task.setObject(msg);
		task.setType(MessageType.rest);
		try {
			mgr.put(task);
		} catch (InterruptedException e) {
			Log.error("", e);
		}
//		TaskProcessCentre.getInstance().newRestTskProcess().taskProcess(msg);
	}
	
}
