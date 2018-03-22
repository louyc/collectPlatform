package com.neusoft.gbw.cp.process.measure.channel.auto;

import java.util.Date;
import java.util.List;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.measure.channel.AbstractTaskChannel;
import com.neusoft.gbw.cp.process.measure.channel.ChannelType;
import com.neusoft.gbw.cp.process.measure.channel.MeaUnitChannel;
import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.gbw.infrastructure.msgcenter.srv.GBWMsgConstant;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMServiceCentre;

public class AutoTaskChannel extends AbstractTaskChannel implements MeaUnitChannel {
	
	protected NMServiceCentre servicePool = null;
	private RecordDispatchTaskHandler dispatchhandler = null;
	private Thread unitProcessThread = null;
//	private RecordCountListenHandler listenHandler = null;
	private Thread recordThread = null;
//	private MesureRecordModel model = null;
	
	public AutoTaskChannel(ChannelType type) {
		super(type);
//		model = MesureRecordModel.getInstance();
	}
	
	@Override
	public void init() {
		super.init();
		this.servicePool = new NMServiceCentre();
	}
	
	@Override
	public void open() {
		//启动录音线程
		createRecordRecieveHandler();
		//统计数量线程,同时后续处理，更新收测单元表，最后知知前台；//统计数量的载体可以存储在MesureUnitContext//统计数量监听线程，以上操作都在监听中进行
		//现有逻辑不再统计整个录音的数量，进行超时处理同时推送消息，现在指针对每一个录音进行单独的超时处理
//		createListenHandler();
	}
	
	@Override
	public void close() {
		clear();
		servicePool.stopAllThreads();
	}
	
	private void createRecordRecieveHandler() {
		dispatchhandler = new RecordDispatchTaskHandler(this);
		dispatchhandler.setServiceName(this.getType() + "_recordRecieveThread");
		servicePool.addService(dispatchhandler);
	}
	
	protected void createRecordHandler(CollectData data) {
		recordThread = new Thread(new RecordStreamTaskHandler(data, this));
		recordThread.setName(this.getType() + "RecordStreamTaskThread#" + data.getCollectTask().getCollectTaskID());
		try{
			recordThread.start();
		}catch(Exception e){
			Log.debug(this.getClass().getName()+"IllegalThreadStateException    在不合法时间调用方法       AutoTaskChannel    ");
		}
	}
	
//	private void createListenHandler() {
//		listenHandler = new RecordCountListenHandler(this);
//		listenHandler.setServiceName(this.getType() + "_listenThread");
//		servicePool.addService(listenHandler);
//	}
	
	@Override
	public void createMeasureUnit(List<CollectTask> list) {
		unitProcessThread = new Thread(new MeasureUnitProcessHandler(list));
		unitProcessThread.setName(this.getType() + "_unitProcessHandler_" + new Date().getTime());
		try{
			unitProcessThread.start();
		}catch(IllegalThreadStateException e){
			Log.debug("IllegalThreadStateException    在不合法时间调用方法       AutoTaskChannel    ");
		}
	}
	
	@Override
	public void storeData(CollectData data,String radio_url, String eval_url, String collect_desc) {
		StoreInfo info = RecordDataConver.converStore(data, radio_url ,eval_url, collect_desc);
		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
	}
	
	@Override
	public void storeSafeAssessData(CollectData data) {
		StoreInfo info = RecordDataConver.converSafeAccessStore(data);
		if(null!=info)
		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
	}
	
	@Override
	public void storeLeakageReason(CollectData data, String reason) {
		StoreInfo info = RecordDataConver.converLeakageReasonStore(data, reason);
		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
	}
	
	
//	@Override
//	public Timer createMeaUnitTimeOut(String key,int autoType, int orgType, int recordLength) {
//		Timer timer = new Timer();
//		TimeOutTask task = new TimeOutTask(key, autoType, orgType);
//		timer.schedule(task, recordLength*1000);
//		
//		return timer;
//	}
	
	@Override
	public void sendWebMsg(int autoType, int orgType) {
		Log.debug("[收测单元]收测单元生成，向前台发送提醒消息，autoType=" + autoType + ",orgType=" + orgType);
		switch(autoType) {
		case 0:
			switch(orgType) {
			case 3:
				sendJmsDto(GBWMsgConstant.C_JMS_BROADCAST_MSG);// 发广播测评完成消息
				break;
			case 4:
				sendJmsDto(GBWMsgConstant.C_JMS_TEST_MSG);// 发实验测评完成消息
				break;
			}
			break;
		case 1:
			switch(orgType) {
			case 3:
				sendJmsDto(GBWMsgConstant.C_JMS_MANUAL_TEST_MSG);// 发广播测评完成消息
				break;
			case 4:
				sendJmsDto(GBWMsgConstant.C_JMS_MANUAL_BROADCAST_MSG);// 发实验测评完成消息
				break;
			}
			break;
		}
	}
	
//	@Override
//	public void updateRecordCount(CollectData data) {
//		int mode = data.getCollectTask().getBusTask().getTask_build_mode();
//		int unitTime = data.getCollectTask().getBusTask().getMeasure_unit_time();
//		String beginTime = data.getCollectTask().getBusTask().getUnit_begin();
//		int orgId = data.getCollectTask().getBusTask().getTask_origin_id();
//		String key = mode + "_" + unitTime + "_" + beginTime + "_" + orgId;
//		model.updateRecordCount(key);
//	}
	
	public void sendJmsDto(int type_id) {
		JMSDTO dto = new JMSDTO();
		dto.setTypeId(type_id);
		dto.setObj("OK");
		ARSFToolkit.sendEvent(ProcessConstants.JMS_SEND_MSG_TOPIC, dto);
	}

//	private class TimeOutTask extends TimerTask {
//		
//		private String key = null;
//		private int orgType = 0;
//		private int autoType = 0;
//		
//		public TimeOutTask(String key,int autoType, int orgType) {
//			this.key = key;
//			this.orgType = orgType;
//			this.autoType = autoType;
//		}
//
//		@Override
//		public void run() {
//			Log.debug("收测单元到期");
//			sendWebMsg(autoType,orgType);
//			model.remove(key);
//		}
//	}
}
