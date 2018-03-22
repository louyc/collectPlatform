package com.neusoft.gbw.cp.process.realtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neusoft.gbw.cp.process.realtime.event.handler.ManuaAutoSetlTaskHandler;
import com.neusoft.gbw.cp.process.realtime.event.handler.ManuaSetlTaskHandler;
import com.neusoft.gbw.cp.process.realtime.event.handler.ReceiveAlarmTypeHandler;
import com.neusoft.gbw.cp.process.realtime.event.handler.ReceiveEquAlarmHandler;
import com.neusoft.gbw.cp.process.realtime.event.handler.ReceiveRecordAddrHandler;
import com.neusoft.gbw.cp.process.realtime.event.handler.RecieveCollectDataHandler;
import com.neusoft.gbw.cp.process.realtime.event.handler.RecordVoiceStreamHandler;
import com.neusoft.gbw.cp.process.realtime.event.handler.RecoverTaskDataHandler;
import com.neusoft.gbw.cp.process.realtime.event.handler.StreamTaskHandler;
import com.neusoft.gbw.cp.process.realtime.event.handler.SubscribeFtpServerHandler;
import com.neusoft.gbw.cp.process.realtime.event.handler.SubscribeQualTypeHandler;
import com.neusoft.gbw.cp.process.realtime.event.handler.SyntMonitorDeviceHandler;
import com.neusoft.gbw.cp.process.realtime.service.ControlService;
import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.service.event.NMSSubject;

public class Activator extends BaseActivator {
	
	private ControlService controlService;

	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		List<BaseEventHandler> list = new ArrayList<BaseEventHandler>();
		list.add(new RecieveCollectDataHandler(controlService.getDataDispatcher())); //采集数据业务处理主题
		list.add(new SyntMonitorDeviceHandler());//站点设备信息同步主题
		list.add(new ReceiveAlarmTypeHandler());//同步告警类型字典数据主题
		list.add(new StreamTaskHandler());   //播音任务处理主题
		list.add(new ManuaAutoSetlTaskHandler());//自动设置任务处理服务 
		list.add(new ManuaSetlTaskHandler());   //手动设置任务处理服务
		list.add(new SubscribeQualTypeHandler());   //同步采集点手动任务的code对应表名称主题
		list.add(new ReceiveRecordAddrHandler());   //同步录音存储路径主题
		list.add(new SubscribeFtpServerHandler());  //同步FTP服务器相关信息主题
		list.add(new RecoverTaskDataHandler());   //任务定时回收，回收数据比对去重主题（数据库中存储昨天的数据）
		list.add(new RecordVoiceStreamHandler());  //实时服务采集音频的应答主题
		list.add(new ReceiveEquAlarmHandler(controlService.getAlarmMgr())); //故障告警发送给NP5.3采集平台主题
		return list;
	}

	@Override
	public void init() {
		bindCoreServices();
		bindService(NMSSubject.class);
		
		if(controlService == null) {
			controlService = new ControlService();
			controlService.start();
		}
//		stopCollect();
	}
	
//	private void stopCollect() {
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//		}
//		ARSFToolkit.sendEvent(NPBaseConstant.EventTopic.THREAD_THROWABLE_TOPIC, "stop");
//		Log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>stop Collect>>>>>>>>>>>");
//	}

	@Override
	public void stop() {
		unbindService(NMSSubject.class);
		unbindCoreServices();
		
		if(controlService != null) {
			controlService.stop();
		}
	}
}
