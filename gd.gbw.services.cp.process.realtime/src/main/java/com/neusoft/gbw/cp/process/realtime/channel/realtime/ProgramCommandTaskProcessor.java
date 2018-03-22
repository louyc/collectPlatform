package com.neusoft.gbw.cp.process.realtime.channel.realtime;

import java.util.Map;

import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniControlDTO;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.conver.vo.Return;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;
import com.neusoft.gbw.domain.util.ConverterUtil;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NPJsonUtil;
import com.neusoft.np.arsf.net.core.NetEventProtocol;

public class ProgramCommandTaskProcessor extends SendTaskProcessor implements ITaskProcess {
	
	@Override
	public Object disposeV8(CollectData data) throws RealtimeDisposeException {
		Object obj = null;
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_success:
			obj = disposeV8Data(data);
			break;
		default:
			Log.warn("[实时处理服务]版本控制任务处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			obj = disposeError(data);
			break;
		}
//		Log.debug("[实时处理服务]版本控制任务处理消息发送至前台，msg=" + obj==null?null:obj.toString());
		if(obj == null) 
			Log.debug("[实时处理服务]版本控制任务处理消息发送至前台，msg=" + obj);
		else
			Log.debug("[实时处理服务]版本控制任务处理消息发送至前台，msg=" + obj.toString());
		sendTask(obj);
		return null;
	}

	@Override
	public Object disposeV7(CollectData data) throws RealtimeDisposeException {
		Object obj = null;
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_success:
			obj = disposeV7Data(data);
			break;
		default:
			Log.warn("[实时处理服务]版本控制任务处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			obj = disposeError(data);
			break;
		}
//		Log.debug("[实时处理服务]版本控制任务处理消息发送至前台，msg=" + obj==null?null:obj.toString());
		if(obj == null) 
			Log.debug("[实时处理服务]版本控制任务处理消息发送至前台，msg=" + obj);
		else
			Log.debug("[实时处理服务]版本控制任务处理消息发送至前台，msg=" + obj.toString());
		sendTask(obj);
		return null;
	}

	@Override
	public Object disposeV6(CollectData data) throws RealtimeDisposeException {
		Object obj = null;
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_success:
			obj = disposeV6Data(data);//V6沿用V5处理接口
			break;
		default:
			Log.warn("[实时处理服务]版本控制任务处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			obj = disposeError(data);
			break;
		}
//		Log.debug("[实时处理服务]版本控制任务处理消息发送至前台，msg=" + obj==null?null:obj.toString());
		if(obj == null) 
			Log.debug("[实时处理服务]版本控制任务处理消息发送至前台，msg=" + obj);
		else
			Log.debug("[实时处理服务]版本控制任务处理消息发送至前台，msg=" + obj.toString());
		sendTask(obj);
		return null;
	}

	@Override
	public Object disposeV5(CollectData data) throws RealtimeDisposeException {
		Object obj = null;
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_success:
			obj = disposeV5Data(data);
			break;
		default:
			Log.warn("[实时处理服务]版本控制任务处理异常，任务执行状态：" + status + "," + getLogMsg(data));
			obj = disposeError(data);
			break;
		}
//		Log.debug("[实时处理服务]版本控制任务处理消息发送至前台，msg=" + obj==null?null:obj.toString());
		if(obj == null) 
			Log.debug("[实时处理服务]版本控制任务处理消息发送至前台，msg=" + obj);
		else
			Log.debug("[实时处理服务]版本控制任务处理消息发送至前台，msg=" + obj.toString());
		sendTask(obj);
		return null;
	}

	@SuppressWarnings("unchecked")
	private Object disposeV8Data(CollectData data) {
		CollectTask task = data.getCollectTask();
		Map<String, String> syntMap = (Map<String, String>) task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		MoniControlDTO dto = null;
		try {
			dto = getDTO(syntMap);
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
		
		Report report = (Report) data.getData().getReportData();
		com.neusoft.gbw.cp.conver.v8.protocol.vo.other.ProgramCommandR pro = (com.neusoft.gbw.cp.conver.v8.protocol.vo.other.ProgramCommandR) report.getReport();
		if(pro == null) {
			dto = fillSuccess(dto, report);
			return restStr(syntMap, dto);
		}
		//1.重启、2.版本控制、3.授时、4.监控器状态
		int type = Integer.parseInt(dto.getFlg());
		switch(type) {
		case 1:
			dto = fillSuccess(dto, report);
			break;
		case 2:
			dto = fillProgramInfo(dto, pro);
			break;
		case 3:
			dto = fillSuccess(dto, report);
			break;
		case 4:
			dto = fillAliveInfo(dto, pro);
			break;
		}		
		
		return restStr(syntMap, dto);
	}
	
	@SuppressWarnings("unchecked")
	private Object disposeV7Data(CollectData data) {
		CollectTask task = data.getCollectTask();
		Map<String, String> syntMap = (Map<String, String>) task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		MoniControlDTO dto = null;
		try {
			dto = getDTO(syntMap);
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
		
		Report report = (Report) data.getData().getReportData();
		com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ProgramCommandR pro = (com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ProgramCommandR) report.getReport();
		if(pro == null) {
			dto = fillSuccess(dto, report);
			return restStr(syntMap, dto);
		}
		//1.重启、2.版本控制、3.授时
		int type = Integer.parseInt(dto.getFlg());
		switch(type) {
		case 1:
			dto = fillSuccess(dto, report);
			break;
		case 2:
			dto = fillProgramInfoV7(dto, pro);
			break;
		case 3:
			dto = fillSuccess(dto, report);
			break;
		}		
		
		return restStr(syntMap, dto);
	}
	
	@SuppressWarnings("unchecked")
	private Object disposeV5Data(CollectData data) {
		CollectTask task = data.getCollectTask();
		Map<String, String> syntMap = (Map<String, String>) task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		MoniControlDTO dto = null;
		try {
			dto = getDTO(syntMap);
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
		
		Report report = (Report) data.getData().getReportData();
		com.neusoft.gbw.cp.conver.v5.protocol.vo.other.ProgramCommandR pro = (com.neusoft.gbw.cp.conver.v5.protocol.vo.other.ProgramCommandR) report.getReport();
		if(pro == null) {
			dto = fillSuccess(dto, report);
			return restStr(syntMap, dto);
		}
		//1.重启、2.版本控制、3.授时
		int type = Integer.parseInt(dto.getFlg());
		switch(type) {
		case 1:
			dto = fillSuccess(dto, report);
			break;
		case 2:
			dto = fillProgramInfoV5(dto, pro);
			break;
		}		
		
		return restStr(syntMap, dto);
	}
	
	@SuppressWarnings("unchecked")
	private Object disposeV6Data(CollectData data) {
		CollectTask task = data.getCollectTask();
		Map<String, String> syntMap = (Map<String, String>) task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		MoniControlDTO dto = null;
		try {
			dto = getDTO(syntMap);
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
		
		Report report = (Report) data.getData().getReportData();
		com.neusoft.gbw.cp.conver.v6.protocol.vo.other.ProgramCommandR pro = (com.neusoft.gbw.cp.conver.v6.protocol.vo.other.ProgramCommandR) report.getReport();
		if(pro == null) {
			dto = fillSuccess(dto, report);
			return restStr(syntMap, dto);
		}
		//1.重启、2.版本控制、3.授时
		int type = Integer.parseInt(dto.getFlg());
		switch(type) {
		case 1:
			dto = fillSuccess(dto, report);
			break;
		case 2:
			dto = fillProgramInfoV6(dto, pro);
			break;
		}		
		
		return restStr(syntMap, dto);
	}
	
	@SuppressWarnings("unchecked")
	private Object disposeError(CollectData data) {
		CollectTask task = data.getCollectTask();
		Map<String, String> syntMap = (Map<String, String>) task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		MoniControlDTO dto = null;
		try {
			dto = getDTO(syntMap);
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
			return null;
		}
		dto.setIsSuccee("任务下发失败");
		return restStr(syntMap, dto);
	}
	
	private MoniControlDTO fillAliveInfo(MoniControlDTO dto, com.neusoft.gbw.cp.conver.v8.protocol.vo.other.ProgramCommandR pro) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.other.AliveInfo aInfo = pro.getAliveInfo().get(0);
		if (Integer.parseInt(aInfo.getValue()) == 0)
			dto.setAlive("广播监测软件正常");
		else
			dto.setAlive("监测软件无法收测");
		
		return dto;
	}
	
	private MoniControlDTO fillProgramInfo(MoniControlDTO dto, com.neusoft.gbw.cp.conver.v8.protocol.vo.other.ProgramCommandR pro) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.other.ProgramInfo proInfo = pro.getProgramInfo().get(0);
		dto.setCompany(proInfo.getCompany());
		dto.setName(proInfo.getName());
		dto.setVersion(proInfo.getVersion());
		return dto;
	}
	
	private MoniControlDTO fillProgramInfoV7(MoniControlDTO dto, com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ProgramCommandR pro) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.other.ProgramInfo proInfo = pro.getProgramInfo().get(0);
		dto.setCompany(proInfo.getCompany());
		dto.setName(proInfo.getName());
		dto.setVersion(proInfo.getVersion());
		return dto;
	}
	
	private MoniControlDTO fillProgramInfoV6(MoniControlDTO dto, com.neusoft.gbw.cp.conver.v6.protocol.vo.other.ProgramCommandR pro) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.other.ProgramInfo proInfo = pro.getProgramInfo().get(0);
		dto.setCompany(proInfo.getCompany());
		dto.setName(proInfo.getName());
		dto.setVersion(proInfo.getVersion());
		return dto;
	}
	
	private MoniControlDTO fillProgramInfoV5(MoniControlDTO dto, com.neusoft.gbw.cp.conver.v5.protocol.vo.other.ProgramCommandR pro) {
		com.neusoft.gbw.cp.conver.v5.protocol.vo.other.ProgramInfo proInfo = pro.getProgramInfo().get(0);
		dto.setCompany(proInfo.getCompany());
		dto.setName(proInfo.getName());
		dto.setVersion(proInfo.getVersion());
		return dto;
	}
	
	private MoniControlDTO fillSuccess(MoniControlDTO dto, Report report){
		Return info = report.getReportReturn();
		dto.setIsSuccee(info.getDesc());
//		if (Integer.parseInt(info.getValue()) == 0)
//			dto.setIsSuccee("成功");
//		else
//			dto.setIsSuccee("失败");
		return dto;
	}
	
	private String restStr(Map<String, String> syntMap, MoniControlDTO dto) {
		try {
			String respose = object2Str(dto);
			syntMap.put(NetEventProtocol.SYNT_RESPONSE, respose);
			String syntStr = NPJsonUtil.jsonValueString(syntMap);
			return syntStr;
		} catch (Exception e) {
			Log.error("", e);
			return null;
		}
	}
	
	private String object2Str(MoniControlDTO dto) throws Exception {
		return ConverterUtil.objToXml(dto);
	}
	
	private MoniControlDTO getDTO(Map<String, String> map) throws Exception {
		String request = map.get(NetEventProtocol.SYNT_REQUEST);
		MoniControlDTO dto = new MoniControlDTO();
		try {
			dto = (MoniControlDTO)ConverterUtil.xmlToObj(request);
		} catch (Exception e) {
			throw new Exception("JSON处理格式异常：" + request, e);
		}
		return dto;
	}
	
	private String getLogMsg(CollectData data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("monitorID=" + data.getCollectTask().getBusTask().getMonitor_id() + ",");
		return buffer.toString();
	}

}
