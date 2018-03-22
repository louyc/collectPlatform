package com.neusoft.gbw.cp.build.interfaces.event.handler;

import com.neusoft.gbw.cp.build.application.TaskReceiveMgr;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildPrepareInfo;
import com.neusoft.gbw.cp.load.data.build.domain.vo.MessageTask;
import com.neusoft.gbw.cp.load.data.build.domain.vo.MessageType;
import com.neusoft.gbw.cp.load.data.build.domain.vo.PlatformBuildType;
import com.neusoft.gbw.cp.schedule.vo.TimeRemindMsg;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class AutoRecoverTaskHandler implements BaseEventHandler{

	private TaskReceiveMgr mgr = null;
	
	public AutoRecoverTaskHandler(TaskReceiveMgr mgr) {
		this.mgr = mgr;
	}
	@Override
	public String getTopicName() {
		return EventServiceTopic.RECOVER_TASK_DATA_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if (arg0 instanceof TimeRemindMsg) {
			Log.debug("任务定时回收，TimeRemindMsg= " + arg0.toString());
			initRecoverTimeData();
			dispose((TimeRemindMsg)arg0);
		}
		return true;
	}
	
//	private void dispose(TimeRemindMsg msg) {
//		BuildPrepareInfo info = new BuildPrepareInfo();
//		info.setPlatBuildType(PlatformBuildType.auto_recover_data);
//		info.setMsg(msg);
//		TaskProcessCentre.getInstance().newManualAndAutoTskProcess().taskProcess(info);
//	}
	
	private void dispose(TimeRemindMsg msg) {
		
		BuildPrepareInfo info = new BuildPrepareInfo();
		info.setPlatBuildType(PlatformBuildType.auto_recover_data);
		info.setMsg(msg);
		
		MessageTask task = new MessageTask();
		task.setObject(info);
		task.setType(MessageType.system);
		try {
			mgr.put(task);
		} catch (InterruptedException e) {
			Log.error("", e);
		}
//		TaskProcessCentre.getInstance().newManualAndAutoTskProcess().taskProcess(info);
	}
	
	private void initRecoverTimeData() {
		DataMgrCentreModel.getInstance().initRecoverTimeData();
	}
}
