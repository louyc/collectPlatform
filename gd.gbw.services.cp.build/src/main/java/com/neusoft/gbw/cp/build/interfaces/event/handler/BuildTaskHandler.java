package com.neusoft.gbw.cp.build.interfaces.event.handler;

import com.neusoft.gbw.cp.build.domain.mode.BuildContext;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.schedule.vo.TimeRemindMsg;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class BuildTaskHandler implements BaseEventHandler{
	
	private String topicName;

	@Override
	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if (arg0 instanceof TimeRemindMsg) {
			dispose((TimeRemindMsg)arg0);
		}
		return true;
	}
	
	private void dispose(TimeRemindMsg msg) {
		BuildContext.getInstance().getTaskBuildMgr().startTimeTaskBuild(msg, BuildConstants.TaskBuild.START_PERIOD_BUILD);
	}
}