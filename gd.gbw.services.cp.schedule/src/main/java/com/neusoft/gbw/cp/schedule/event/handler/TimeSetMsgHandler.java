package com.neusoft.gbw.cp.schedule.event.handler;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.schedule.service.TimeMsgDisposeProcess;
import com.neusoft.gbw.cp.schedule.vo.TimeSetMsg;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
/**
 * 设置调度消息 9小时 ，8分钟
 * @author yanghao
 *
 */
public class TimeSetMsgHandler  implements BaseEventHandler{

	@Override
	public String getTopicName() {
		return EventServiceTopic.TaskControl.TIME_REMIND_SET_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if(arg0 == null) {
			return false;
		}
		if(arg0 instanceof TimeSetMsg) {
			TimeMsgDisposeProcess.getInstance().disposeTimeSetMsg((TimeSetMsg)arg0);
		}
		return true;
	}

}
