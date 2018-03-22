package com.neusoft.gbw.cp.process.realtime.service.stream;

import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.process.realtime.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.realtime.context.StreamTaskMode;
import com.neusoft.gbw.cp.process.realtime.vo.TaskTimeOutInfo;
import com.neusoft.gbw.domain.task.effect.intf.dto.RealTimeStreamDTO;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

public abstract class AbstractStreamTaskProcess implements IStreamTaskProcess{
	
	
	public RealTimeStreamDTO getDto(CollectTask task) {
		RealTimeStreamDTO dto = (RealTimeStreamDTO)task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		return dto;
	}
	
	public void send(int type,RealTimeStreamDTO streamDTO) {
		JMSDTO jms = new JMSDTO();
		jms.setTypeId(type);
		jms.setObj(streamDTO);
		ARSFToolkit.sendEvent(ProcessConstants.JMS_SEND_MSG_TOPIC, jms);
	}
	
	public boolean isV8TS(CollectTask colTask) {
		boolean flag = false;
		int proVersion = colTask.getData().getProVersion();
		int manufacturer_id = colTask.getBusTask().getManufacturer_id();
		if(8 == proVersion && 2 == manufacturer_id) {
			flag = true;
		}
		return flag;
	}
	
	public void stopRecord(CollectTask colTask) {
		RealTimeStreamDTO dto = getDto(colTask);
		String key = dto.getFreq() + "_" + dto.getLastUrl();
		Log.debug("[实时录音]手动停止录音， " + getLogContent(colTask) + ", url=" + dto.getUrl());
		//停止录音
		StreamTaskMode.getInstance().stopRecord(key);
	}
	
	public void sycnStreamTimeOut(CollectTask task) {
		Log.debug("[实时播音]创建音频超时， " + getLogContent(task));
		RealTimeStreamDTO dto = getDto(task);
		String url = dto.getUrl();
		//有超时，重新创建
		StreamTaskMode.getInstance().cancelStreamOut(url);
		StreamTaskMode.getInstance().removeStreamTask(url);
		TaskTimeOutInfo info = new TaskTimeOutInfo();
		info.setTask(task);
		info.setTimer(StreamAssistant.createStreamTimer(task));
		StreamTaskMode.getInstance().putStream(url, info);
	}
	
	public void sycnTaskTimeOut(CollectTask task) {
		Log.debug("[实时播音录音]创建通信超时， " + getLogContent(task));
		String msgId = ((Query)task.getData().getQuery()).getMsgID() + "";
		TaskTimeOutInfo info = new TaskTimeOutInfo();
		info.setTask(task);
		info.setTimer(StreamAssistant.createTransferTimer(task));
		StreamTaskMode.getInstance().putTransfer(msgId, info);
	}
	
	private String getLogContent(CollectTask task) {
		StringBuffer buffer = new StringBuffer();
//		buffer.append("TaskID=" + task.getBusTask().getTask_id() + ",");
		buffer.append("TaskFreq=" + task.getBusTask().getFreq()+ ",");
		buffer.append("MonitorID=" + task.getBusTask().getMonitor_id()+ ",");
		buffer.append("EquCode=" + task.getAttrInfo().getFirstEquCode());
		return buffer.toString();
	}
}
