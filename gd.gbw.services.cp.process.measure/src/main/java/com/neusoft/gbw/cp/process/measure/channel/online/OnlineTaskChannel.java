package com.neusoft.gbw.cp.process.measure.channel.online;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.process.measure.channel.AbstractTaskChannel;
import com.neusoft.gbw.cp.process.measure.channel.ChannelType;
import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.measure.context.MesureRecordModel;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.gbw.domain.evaluation.intf.dto.EvaluationGradeDTO;
import com.neusoft.gbw.domain.task.effect.intf.dto.OnlineListenerDTO;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.gbw.infrastructure.msgcenter.srv.GBWMsgConstant;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMServiceCentre;

public class OnlineTaskChannel extends AbstractTaskChannel {
	
	protected NMServiceCentre servicePool = null;
	private RecordDispatchTaskHandler dispatchhandler = null;
//	private RecordOnlineTaskHandler handler = null;
	private Thread thread = null;
	private MesureRecordModel model = null;
	
	public OnlineTaskChannel(ChannelType type) {
		super(type);
		model = MesureRecordModel.getInstance();
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
	}
	
	public void close() {
		clear();
		servicePool.stopAllThreads();
	}
	
	private void createRecordRecieveHandler() {
		dispatchhandler = new RecordDispatchTaskHandler(this);
		dispatchhandler.setServiceName(this.getType() + "_recordDispachThread");
		servicePool.addService(dispatchhandler);
	}
	
	protected void createRecordHandler(CollectData data) {
		thread = new Thread(new RecordOnlineTaskHandler(data, this));
		try{
			thread.start();
		}catch(Exception e){
			Log.debug(this.getClass().getName()+"线程启动报错", e);
		}
//		handler.setServiceName(this.getType() + "RecordStreamTaskThread#" + data.getCollectTask().getCollectTaskID());
//		servicePool.addService(handler);
	}
	
	public void createOnlineUnit(List<CollectTask> list) {
		updateOnlineCount(list);
	}
	
	private void updateOnlineCount(List<CollectTask> list) {
		CollectTask task = list.get(0);
		String key = getKey(task);
		OnlineListenerDTO onlineTask = (OnlineListenerDTO)task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		int taskSize = list.size();
		int recordLength = ProcessConstants.ONLINE_LISTENER_TASK_WAIT_TIME;
		//同步本次任务数量
		MesureRecordModel.getInstance().syncMesureTask(key, taskSize, createMeaUnitTimeOut(key, recordLength,onlineTask));
	}
	
	public Timer createMeaUnitTimeOut(String key, int recordLength, OnlineListenerDTO onlineTask) {
		Timer timer = new Timer();
		TimeOutTask task = new TimeOutTask(key, onlineTask);
		try{
		timer.schedule(task, recordLength);
		}catch(IllegalStateException e){
			Log.debug("IllegalStateException   在非法或不适当的时间调用方法时产生的异常      OnlineTaskChannel");
		}
		
		return timer;
	}
	
	public void sendWebMsg(OnlineListenerDTO onlineTask) {
		JMSDTO dto = new JMSDTO();
		dto.setTypeId(GBWMsgConstant.C_JMS_REAL_ONLINE_OVER_RESPONSE_MSG);
		dto.setObj(onlineTask);
		ARSFToolkit.sendEvent(ProcessConstants.JMS_SEND_MSG_TOPIC, dto);
		Log.info("在线监听任务完成，发送在线监听完成消息至前台,dto=" + dto.toString());
	}
	
	public void storeData(CollectData data,String radio_url, String eval_url) {
		StoreInfo info = OnlineRecordDataConver.converStore(data, radio_url ,eval_url);
		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
	}
	
	public void updateRecordCount(CollectData data) {
		CollectTask task = data.getCollectTask();
		String key = getKey(task);
		model.updateRecordCount(key);
	}
	
	public void sendEvaluationMsg(CollectData data) {
		String scoringId = data.getCollectTask().getBusTask().getTask_id() + "";
		Log.debug("[在线监听]发送只能评估打分消息，id=" + scoringId);
		EvaluationGradeDTO evalDto = new EvaluationGradeDTO();
		evalDto.setScoringId(scoringId);
		evalDto.setTableName(ProcessConstants.ONLINE_LISTENER_OPERATION_TABLE_NAME);
		JMSDTO dto = new JMSDTO();
		dto.setTypeId(ProcessConstants.C_JMS_EVAL_RECORD_REQUEST_MSG);
		dto.setObj(evalDto);
		ARSFToolkit.sendEvent(ProcessConstants.JMS_SEND_EVALUATION_MSG_TOPIC, dto);
	}

	private class TimeOutTask extends TimerTask {
		
		private String key = null;
		private OnlineListenerDTO onlineTask = null;
		
		public TimeOutTask(String key,OnlineListenerDTO onlineTask) {
			this.key = key;
			this.onlineTask = onlineTask;
		}

		@Override
		public void run() {
			sendWebMsg(onlineTask);
			model.remove(key);
		}
	}

	public String getKey(CollectTask task) {
		OnlineListenerDTO onlineTask = (OnlineListenerDTO)task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		String key =onlineTask.getFreq() + "_" + onlineTask.getListenerTaskId();
		return key;
	}
}
