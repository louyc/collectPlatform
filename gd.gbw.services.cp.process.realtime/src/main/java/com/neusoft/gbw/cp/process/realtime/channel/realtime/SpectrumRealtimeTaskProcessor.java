package com.neusoft.gbw.cp.process.realtime.channel.realtime;

import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.conver.vo.Return;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.gbw.infrastructure.msgcenter.srv.GBWMsgConstant;
import com.neusoft.gbw.infrastructure.sys.intf.dto.RecoveryMessageDTO;
import com.neusoft.np.arsf.base.bundle.Log;

public class SpectrumRealtimeTaskProcessor extends SendTaskProcessor implements ITaskProcess {
	
	@Override
	public Object disposeV8(CollectData data) throws RealtimeDisposeException {
		dispacherData(data);
		return null;
	}

	@Override
	public Object disposeV7(CollectData data) throws RealtimeDisposeException {
		dispacherData(data);
		return null;
	}

	@Override
	public Object disposeV6(CollectData data) throws RealtimeDisposeException {
		dispacherData(data);
		return null;
	}

	@Override
	public Object disposeV5(CollectData data) throws RealtimeDisposeException {
		dispacherData(data);
		return null;
	}
	
	private void dispacherData(CollectData data) {
		Object obj = null;
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_success:
			obj = dispose(data);
			break;
		default:
			Log.warn("[实时处理服务]频谱实时查询任务处理异常，，任务执行状态：" + status + "," + getLogMsg(data));
			obj = disposeError(data);
			break;
		}
//		Log.debug("[实时处理服务]指标实时查询任务处理消息发送至前台，msg=" + obj==null?null:obj.toString());
		if(obj == null) 
			Log.debug("[实时处理服务]频谱实时查询任务处理消息发送至前台，msg=" + obj);
		else
			Log.debug("[实时处理服务]频谱实时查询任务处理消息发送至前台，msg=" + obj.toString());
		sendTask(obj);
	}

	private Object dispose(CollectData data) {
		Report report = (Report) data.getData().getReportData();
		Return ret = report.getReportReturn();
		RecoveryMessageDTO dto = new RecoveryMessageDTO();
		dto.setSuccessType(ret.getValue());
		dto.setSuccessDicDesc(ret.getDesc());
		JMSDTO jms = createDTO(dto);
		return jms;
	}
	
	private Object disposeError(CollectData data) {
		RecoveryMessageDTO recover = new RecoveryMessageDTO();
		recover.setSuccessDicDesc(ProcessConstants.TASK_SEND_ERROR_DESC);
		recover.setSuccessType(ProcessConstants.TASK_SEND_ERROR_TYPE);
		JMSDTO jms = createDTO(recover);
		return jms;
	}
	
	private JMSDTO createDTO(RecoveryMessageDTO recover) {
		JMSDTO jms = new JMSDTO();
		jms.setTypeId(GBWMsgConstant.C_JMS_REAL_SPECTRUM_RESPONSE_MSG);
		jms.setObj(recover);
		return jms;
	}
	
	private String getLogMsg(CollectData data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("monitorID=" + data.getCollectTask().getBusTask().getMonitor_id() + ",");
		return buffer.toString();
	}
}
