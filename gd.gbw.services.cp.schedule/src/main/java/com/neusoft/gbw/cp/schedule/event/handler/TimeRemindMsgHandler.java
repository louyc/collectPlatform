package com.neusoft.gbw.cp.schedule.event.handler;

import com.neusoft.gbw.cp.schedule.constants.ScheduleConstants;
import com.neusoft.gbw.cp.schedule.service.TimeMsgDisposeProcess;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
/**
 * 调度返回消息提醒，提醒构建
 * @author yanghao
 *
 */
public class TimeRemindMsgHandler  implements BaseEventHandler{

	@Override
	public String getTopicName() {
		return ScheduleConstants.ScheduleTopic.RECIEVE_TIME_REMIND_MSG_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if(arg0 == null) {
			return false;
		}
		if(arg0 instanceof String) {
			TimeMsgDisposeProcess.getInstance().disposeTimeRemindMsg(arg0.toString());
			
		}
		return true;
	}

}
