package com.neusoft.gbw.cp.build.interfaces.event.handler;

import com.neusoft.gbw.cp.build.domain.services.SyntInitOtherService;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.schedule.vo.TimeRemindMsg;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class SyntDBHandler implements BaseEventHandler{
	
	private SyntInitOtherService service = null;

	public SyntDBHandler() {
		service = new SyntInitOtherService();
	}

	@Override
	public String getTopicName() {
		return EventServiceTopic.MANUAL_TASK_SYNT_DB_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if (arg0 instanceof String || arg0 instanceof TimeRemindMsg) {
			dispose(arg0);
			//发送同步其他服务准备数据消息
			syntUpdateServiceData();
		}
		return true;
	}
	
	private void dispose(Object msg) {
		Log.debug("[构建服务]接收同步数据库消息，msg=" + msg);
		DataMgrCentreModel.getInstance().synt();
	}
	
	private void syntUpdateServiceData() {
		service.initMonitorMachine();
		service.initQualityType();
		service.initAlarmType();
		service.initFtpServer();
		service.initRecordAddr();
		service.initRealMeasureSite();
		//刷新时间间隔配置信息
		service.initTaskTimeInterval();
		
	}
	
}
