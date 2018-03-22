package com.neusoft.gbw.cp.build.interfaces.event.handler;

import com.neusoft.gbw.cp.build.application.TaskReceiveMgr;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.load.data.build.domain.vo.MessageTask;
import com.neusoft.gbw.cp.load.data.build.domain.vo.MessageType;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class JMSTaskHandler implements BaseEventHandler{

	private TaskReceiveMgr mgr = null;
	
	public JMSTaskHandler(TaskReceiveMgr mgr) {
		this.mgr = mgr;
	}
	
	@Override
	public String getTopicName() {
		return BuildConstants.JMS_RECEIVE_MSG_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if (arg0 instanceof JMSDTO) {
			dispose((JMSDTO)arg0);
		}
		return true;
	}
	
	private void dispose(JMSDTO dto) {
		Log.debug("接收JMS消息，dto = " + dto);
		MessageTask task = new MessageTask();
		task.setObject(dto);
		task.setType(MessageType.jms);
		try {
			mgr.put(task);
		} catch (InterruptedException e) {
			Log.error("", e);
		}
//		TaskProcessCentre.getInstance().newJMSTaskProcess().taskProcess(dto);
	}
}