package com.neusoft.gbw.cp.build.interfaces.event.handler;

import com.neusoft.gbw.cp.build.application.TaskReceiveMgr;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildPrepareInfo;
import com.neusoft.gbw.cp.load.data.build.domain.vo.MessageTask;
import com.neusoft.gbw.cp.load.data.build.domain.vo.MessageType;
import com.neusoft.gbw.cp.load.data.build.domain.vo.PlatformBuildType;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class UploadStreamFileHandler implements BaseEventHandler{

	private TaskReceiveMgr mgr = null;
	
	public UploadStreamFileHandler(TaskReceiveMgr mgr) {
		this.mgr = mgr;
	}
	
	@Override
	public String getTopicName() {
		return EventServiceTopic.UPLOAD_STREAM_FILE_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if (arg0 instanceof CollectTask) {
			dispose((CollectTask)arg0);
		}
		return true;
	}
	
//	private void dispose(CollectTask task) {
//		BuildPrepareInfo info = new BuildPrepareInfo();
//		info.setPlatBuildType(PlatformBuildType.upload_stream);
//		info.setExpandObj(task);
//		TaskProcessCentre.getInstance().newManualAndAutoTskProcess().taskProcess(info);
//	}
	
	private void dispose(CollectTask task) {
		
		BuildPrepareInfo info = new BuildPrepareInfo();
		info.setPlatBuildType(PlatformBuildType.upload_stream);
		info.setExpandObj(task);
		
		MessageTask msg = new MessageTask();
		msg.setObject(info);
		msg.setType(MessageType.system);
		try {
			mgr.put(msg);
		} catch (InterruptedException e) {
			Log.error("", e);
		}
//		TaskProcessCentre.getInstance().newManualAndAutoTskProcess().taskProcess(info);
	}
}
