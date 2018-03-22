package com.neusoft.gbw.cp.process.realtime.channel.realtime;

import java.util.Map;

import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniClientLinkDTO;
import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniLinkDTO;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;
import com.neusoft.gbw.domain.util.ConverterUtil;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NPJsonUtil;
import com.neusoft.np.arsf.net.core.NetEventProtocol;

public class ClientStreamStopTaskProcessor extends SendTaskProcessor implements ITaskProcess{
	
	@Override
	public Object disposeV8(CollectData data) throws RealtimeDisposeException {
		Object obj = null;
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_success:
			obj = dispose(data);
			break;
		default:
			Log.warn("[实时处理服务]客户端连接断开任务处理异常，，任务执行状态：" + status + "," + getLogMsg(data));
			obj = disposeError(data);
			break;
		}
		Log.debug("[实时处理服务]客户端连接断开消息发送至前台，msg=" + obj.toString());
		sendTask(obj);
		return null;
	}

	@Override
	public Object disposeV7(CollectData data) throws RealtimeDisposeException {
		//无V7版本接口
		return null;
	}

	@Override
	public Object disposeV6(CollectData data) throws RealtimeDisposeException {
		//无V6版本接口
		return null;
	}

	@Override
	public Object disposeV5(CollectData data) throws RealtimeDisposeException {
		//无V5版本接口
		return null;
	}

	@SuppressWarnings("unchecked")
	private Object dispose(CollectData data) {
		Map<String, String> syntMap = (Map<String, String>) data.getCollectTask().getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		String request = syntMap.get(NetEventProtocol.SYNT_REQUEST);
		Report report = (Report)data.getData().getReportData();
		
		MoniClientLinkDTO clientDto = null;
		try {
			clientDto = decodeMoniClientLinkDTO(request);
		} catch (Exception e) {
			Log.error("", e);
			return null;
		}
		
		if (report == null) {
			clientDto = failStopClient(clientDto);
		}
		
		int value = Integer.parseInt(report.getReportReturn().getValue());
		if (value == 0) {
			clientDto = successStopClient(clientDto);
		} else {
			clientDto = failStopClient(clientDto);
		}
		
		syntMap.put(NetEventProtocol.SYNT_RESPONSE, object2Str(clientDto));
		String syntStr = NPJsonUtil.jsonValueString(syntMap);
		
		return syntStr;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private Object disposeError(CollectData data) {
		Map<String, String> syntMap = (Map<String, String>) data.getCollectTask().getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		String request = syntMap.get(NetEventProtocol.SYNT_REQUEST);
		MoniClientLinkDTO clientDto = null;
		clientDto = failErrorStopClient(clientDto);
		syntMap.put(NetEventProtocol.SYNT_RESPONSE, object2Str(clientDto));
		String syntStr = NPJsonUtil.jsonValueString(syntMap);
		return syntStr;
		
	}
	
	private String object2Str(MoniClientLinkDTO clientDto) {
		return ConverterUtil.objToXml(clientDto);
	}
	
	private MoniClientLinkDTO decodeMoniClientLinkDTO(String task) throws Exception {
		MoniClientLinkDTO vo = (MoniClientLinkDTO)ConverterUtil.xmlToObj(task);
		return vo;
	}
	
	private MoniClientLinkDTO failStopClient(MoniClientLinkDTO clientDto) {
		if(clientDto != null && clientDto.getResList() != null) {
			for(MoniLinkDTO dto : clientDto.getResList()) {
				dto.setResult("Fail");
			}
		}
		return clientDto;
	}
	
	private MoniClientLinkDTO successStopClient(MoniClientLinkDTO clientDto) {
		if(clientDto != null && clientDto.getResList() != null) {
			for(MoniLinkDTO dto : clientDto.getResList()) {
				dto.setResult("Success");
			}
		}
		return clientDto;
	}
	
	private MoniClientLinkDTO failErrorStopClient(MoniClientLinkDTO clientDto) {
		if(clientDto != null && clientDto.getResList() != null) {
			for(MoniLinkDTO dto : clientDto.getResList()) {
				dto.setResult("Error");
			}
		}
		return clientDto;
	}
	
	private String getLogMsg(CollectData data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("monitorID=" + data.getCollectTask().getBusTask().getMonitor_id() + ",");
		return buffer.toString();
	}
}
