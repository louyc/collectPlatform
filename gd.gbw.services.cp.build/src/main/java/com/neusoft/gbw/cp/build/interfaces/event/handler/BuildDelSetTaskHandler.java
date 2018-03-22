package com.neusoft.gbw.cp.build.interfaces.event.handler;

import com.neusoft.gbw.cp.build.domain.mode.BuildContext;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.schedule.vo.TimeRemindMsg;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class BuildDelSetTaskHandler implements BaseEventHandler{
	
	@Override
	public String getTopicName() {
		return EventServiceTopic.MANUAL_AUTO_DELETE_TASK_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if (arg0 instanceof TimeRemindMsg) {
			dispose((TimeRemindMsg)arg0);
		}
		return true;
	}
	
	private void dispose(TimeRemindMsg msg) {
		BuildContext.getInstance().getTaskBuildMgr().startDelSetTaskBuild(msg);
	}
}