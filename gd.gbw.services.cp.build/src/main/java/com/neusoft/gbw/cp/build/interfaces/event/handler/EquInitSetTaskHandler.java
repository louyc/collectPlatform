package com.neusoft.gbw.cp.build.interfaces.event.handler;

import com.neusoft.gbw.cp.build.application.TaskReceiveMgr;
import com.neusoft.gbw.cp.build.domain.services.SyntInitOtherService;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.load.data.build.domain.vo.MessageTask;
import com.neusoft.gbw.cp.load.data.build.domain.vo.MessageType;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class EquInitSetTaskHandler implements BaseEventHandler{
	
	private TaskReceiveMgr mgr = null;
	private SyntInitOtherService othService = null;
	
	public EquInitSetTaskHandler(TaskReceiveMgr mgr) {
		this.mgr = mgr;
		othService = new SyntInitOtherService();
	}
	@Override
	public String getTopicName() {
		return EventServiceTopic.RestTopic.REST_SYNT_EQU_INIT_SET;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if (arg0 instanceof String) {
			syntMonitor();
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
	
	/**
	 * 初始化首先按重新加载一下站点信息
	 */
	private void syntMonitor() {
		DataMgrCentreModel.getInstance().syntDevice();
		othService.initMonitorMachine();
		
	}
}
