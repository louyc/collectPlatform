package com.neusoft.gbw.cp.process.measure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neusoft.gbw.cp.process.measure.event.handler.ManuaRecoverlTaskHandler;
import com.neusoft.gbw.cp.process.measure.event.handler.MeasureStationHandler;
import com.neusoft.gbw.cp.process.measure.event.handler.ReceiveEvalMsgHandler;
import com.neusoft.gbw.cp.process.measure.event.handler.ReceiveMesureTaskHandler;
import com.neusoft.gbw.cp.process.measure.event.handler.ReceiveRecordAddrHandler;
import com.neusoft.gbw.cp.process.measure.event.handler.ReceiveSingleCollectTaskHandler;
import com.neusoft.gbw.cp.process.measure.event.handler.RecieveCollectDataHandler;
import com.neusoft.gbw.cp.process.measure.event.handler.RecordFileRecoverlTaskHandler;
import com.neusoft.gbw.cp.process.measure.event.handler.RecordVoiceStreamHandler;
import com.neusoft.gbw.cp.process.measure.event.handler.RecoverTaskDataHandler;
import com.neusoft.gbw.cp.process.measure.event.handler.SubscribeFtpServerHandler;
import com.neusoft.gbw.cp.process.measure.event.handler.SubscribeQualTypeHandler;
import com.neusoft.gbw.cp.process.measure.event.handler.SyntMonitorDeviceHandler;
import com.neusoft.gbw.cp.process.measure.event.handler.SyntRealMeasureSiteHandler;
import com.neusoft.gbw.cp.process.measure.service.ControlService;
import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.service.event.NMSSubject;
import com.neusoft.np.arsf.service.transfer.NPTransferService;

public class Activator extends BaseActivator {
	
	private ControlService controlService;

	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		List<BaseEventHandler> list = new ArrayList<BaseEventHandler>();
		list.add(new RecieveCollectDataHandler(controlService.getDataDispatchor()));//采集数据业务处理主题
		//  收测， 自动,手动 统计数量
		list.add(new ReceiveMesureTaskHandler());  // 针对效果任务控制分发服务主题   LIST_COLLECT_EFFECT_TASK_TOPIC 
		list.add(new RecordFileRecoverlTaskHandler());//手动录音文件回收任务处理服务
		//CollectTaskDispatcher     收测， 自动,手动 统计数量
		list.add(new ReceiveSingleCollectTaskHandler()); //任务控制分发服务主题    SINGLE_COLLECT_TASK_TOPIC  
		list.add(new SyntMonitorDeviceHandler());//站点设备信息同步主题
		list.add(new ReceiveEvalMsgHandler());  //JMS_RECEIVE_EVALUATION_MSG_TOPIC  ProcessConstants
		list.add(new ReceiveRecordAddrHandler()); //同步录音存储路径主题
		list.add(new SubscribeQualTypeHandler()); //同步采集点手动任务的code对应表名称主题
		list.add(new SyntRealMeasureSiteHandler());//同步实时收测单元更新完成状态的站点信息主题
		list.add(new RecoverTaskDataHandler()); //任务定时回收，回收数据比对去重主题（数据库中存储昨天的数据）
		list.add(new ManuaRecoverlTaskHandler()); 	//手动回收任务处理服务
		list.add(new SubscribeFtpServerHandler()); //同步FTP服务器相关信息主题
		list.add(new RecordVoiceStreamHandler());//收测服务采集音频的应答主题
		list.add(new MeasureStationHandler()); //同步实时收测单元运行图关联台名信息主题
		return list;
	}

	@Override
	public void init() {
		bindCoreServices();
		bindService(NMSSubject.class);
		bindService(NPTransferService.class);
		
		if(controlService == null) {
			controlService = new ControlService();
			controlService.start();
		}
	}
	
	@Override
	public void stop() {
		unbindService(NMSSubject.class);
		unbindCoreServices();
		
		if(controlService != null) {
			controlService.stop();
		}
	}
	
}
