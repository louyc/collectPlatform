package com.neusoft.gbw.cp.schedule.event.handler;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.schedule.constants.ScheduleConstants;
import com.neusoft.gbw.cp.schedule.context.ScheduleTaskContext;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class TaskScheduleMsgHandler implements BaseEventHandler {

	public TaskScheduleMsgHandler() {
	}

	@Override
	public String getTopicName() {
		return ScheduleConstants.ScheduleTopic.RECIEVE_DELAY_SCHEDULE_MSG_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if (arg0 == null) {
			return false;
		}

		if (arg0 instanceof String) {
			CollectTask task = ScheduleTaskContext.getInstance().getCollectTask(arg0.toString());
			if (task == null) {
				return false;
			}
			Log.info("ARSF RECEIVE PeriodJob(INSTANT):" +task.getBusTask().getTask_id()+"   "+task.getBusTask().getMonitor_code()+
					"   "+task.getBusTask().getFreq()+"   "+task.getBusTask().getTask_type_id()
					+"  arg0  "+arg0.toString()+"    "+ task.getCollectTaskID());
			sendTask(task);
		}
		return true;
	}
	
	private void sendTask(CollectTask data) {
		ARSFToolkit.sendEvent(EventServiceTopic.COLLECT_TASK_TOPIC, data);
	}

}
