package com.neusoft.gbw.cp.process.realtime.channel.realtime;

import java.util.Map;

import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.conver.vo.Return;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.process.inspect.util.InspectUtils;
import com.neusoft.gbw.cp.process.inspect.vo.InspectMonStat;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;
import com.neusoft.gbw.domain.util.ConverterUtil;
import com.neusoft.gbw.infrastructure.sys.intf.dto.RecoveryMessageDTO;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NPJsonUtil;
import com.neusoft.np.arsf.net.core.NetEventProtocol;


public class ParamInitSetProcessor extends SendTaskProcessor implements ITaskProcess {

	@Override
	public Object disposeV8(CollectData data) throws RealtimeDisposeException {
		dispatchData(data);
		return null;
	}
	

	@Override
	public Object disposeV7(CollectData data) throws RealtimeDisposeException {
		dispatchData(data);
		return null;
	}

	@Override
	public Object disposeV6(CollectData data) throws RealtimeDisposeException {
		dispatchData(data);
		return null;
	}

	@Override
	public Object disposeV5(CollectData data) throws RealtimeDisposeException {
		dispatchData(data);
		return null;
	}
	
	private void dispatchData(CollectData data) {
		Object obj = null;
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_success:
			obj = dispose(data);
			break;
		default:
			Log.warn("[实时处理服务]参数初始化任务处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			obj = disposeError(data);
			break;
		}
		if(obj == null) 
			Log.debug("[实时处理服务]参数初始化任务处理消息发送至前台，1msg=" + obj);
		else
			Log.debug("[实时处理服务]参数初始化任务处理消息发送至前台，2msg=" + obj.toString());
		sendTask(obj);
	}
	
	private Object dispose(CollectData data) {
		Report report = (Report) data.getData().getReportData();
		Return ret = report.getReportReturn();
		
		//消息返回成功，站点软件为开启状态 4
//		int monitorStatus = 4;    暂时改为1    20170424  lyc
		int monitorStatus = 1;
		long monitorId = data.getCollectTask().getBusTask().getMonitor_id();
		InspectMonStat stat = new InspectMonStat();
		stat.setMonitor_id(monitorId);
		stat.setOnline_state(monitorStatus);
		//更改站点在线状态
		InspectUtils.sendStore(stat);
		
		RecoveryMessageDTO recover = new RecoveryMessageDTO();
		recover.setSuccessDicDesc(ret.getDesc());
		recover.setSuccessType(ret.getValue());
		
		String syntStr = getRestStr(recover, data);
		return syntStr;
	}
	
	private Object disposeError(CollectData data) {
		RecoveryMessageDTO recover = new RecoveryMessageDTO();
		recover.setSuccessType(ProcessConstants.TASK_SEND_ERROR_TYPE);
		recover.setSuccessDicDesc(ProcessConstants.TASK_SEND_ERROR_DESC);
		String syntStr = getRestStr(recover, data);
		return syntStr;
	}
	
	@SuppressWarnings("unchecked")
	private String getRestStr(RecoveryMessageDTO recover, CollectData data) {
		Map<String, String> syntMap = (Map<String, String>) data.getCollectTask().getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		String tmpStr = ConverterUtil.objToXml(recover);
		syntMap.put(NetEventProtocol.SYNT_RESPONSE, tmpStr);
		return NPJsonUtil.jsonValueString(syntMap);
	}
	
	private String getLogMsg(CollectData data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("monitorID=" + data.getCollectTask().getBusTask().getMonitor_id() + ",");
		return buffer.toString();
	}

}
