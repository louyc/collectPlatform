package com.neusoft.gbw.cp.process.realtime.channel.realtime;

import java.util.ArrayList;
import java.util.List;

import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniClientLinkDTO;
import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniLinkDTO;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.conver.vo.Return;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.ClientInfo;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.StreamRealtime;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.StreamRealtimeClientReport;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.process.realtime.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.realtime.exception.RealtimeDisposeException;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.gbw.infrastructure.msgcenter.srv.GBWMsgConstant;
import com.neusoft.np.arsf.base.bundle.Log;

public class ClientStreamTaskProcessor extends SendTaskProcessor implements ITaskProcess{
	
	@Override
	public Object disposeV8(CollectData data) throws RealtimeDisposeException {
		Object obj = null;
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_success:
			obj = dispose(data);
			break;
		default:
			Log.warn("[实时处理服务]客户端连接查询消息任务处理异常，，任务执行状态：" + status + "," + getLogMsg(data));
			obj = disposeError(data);
			break;
		}
		if(obj == null) 
			Log.debug("[实时处理服务]客户端连接查询消息发送至前台，msg=" + obj);
		else
			Log.debug("[实时处理服务]客户端连接查询消息发送至前台，msg=" + obj.toString());
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

	private Object dispose(CollectData data) {
		MoniClientLinkDTO clientDto = getClient(data);
		StreamRealtimeClientReport srcr = null;
		List<StreamRealtime> liststream = null;
		List<MoniLinkDTO> list = null;
		Report report = (Report)data.getData().getReportData();
		//return 非主动上报
		Return ret = report.getReportReturn();
		int success = Integer.parseInt(ret.getValue());
		srcr = (StreamRealtimeClientReport)report.getReport();
		if (success!=0) {
			list=null;
		} else {
			list = buildClient(srcr, liststream);
		}
		clientDto.setResList(list);
		JMSDTO dto = getDTO(clientDto);
		return dto;
	}
	
	private Object disposeError(CollectData data) {
		MoniClientLinkDTO clientDto = getClient(data);
		JMSDTO dto = getDTO(clientDto);
		return dto;
	}

	private List<MoniLinkDTO> buildClient(StreamRealtimeClientReport srcr,
			List<StreamRealtime> liststream) {
		List<ClientInfo> listclient = null;
		List<MoniLinkDTO> list = new ArrayList<MoniLinkDTO>();
		if(null!=srcr){
			liststream = srcr.getStreamRealtime();
		}else {
			throw new IllegalArgumentException("无音频数据");
		}
		
		if (null==liststream) {
			return list;
		}
		for (StreamRealtime streamRealtime : liststream) {
			listclient = streamRealtime.getClientInfos();
			if (!listclient.isEmpty()) {
				
				for (ClientInfo clientInfo : listclient) {
					MoniLinkDTO moniLinkDTO = new MoniLinkDTO();
					moniLinkDTO.setReceive_name(null);
					moniLinkDTO.setCode(streamRealtime.getEquCode());
					moniLinkDTO.setUrl(streamRealtime.getuRL());
					moniLinkDTO.setFreq(streamRealtime.getFreq());
					moniLinkDTO.setBps(streamRealtime.getBps()+"");
					moniLinkDTO.setClientIP(clientInfo.getIp());
//						moniLinkDTO.setResult("成功");
					list.add(moniLinkDTO);
				}
			} else {
				MoniLinkDTO moniLinkDTO = new MoniLinkDTO();
				moniLinkDTO.setReceive_name(null);
				moniLinkDTO.setCode(streamRealtime.getEquCode());
				moniLinkDTO.setUrl(streamRealtime.getuRL());
				moniLinkDTO.setFreq(streamRealtime.getFreq());
				moniLinkDTO.setBps(streamRealtime.getBps()+"");
				moniLinkDTO.setClientIP(null);
//					moniLinkDTO.setResult("成功");
				list.add(moniLinkDTO);
			}
		}
		return list;
	}

	private MoniClientLinkDTO getClient(CollectData data) {
		MoniClientLinkDTO clientDto = (MoniClientLinkDTO)data.getCollectTask().getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		return clientDto;
	}

	private JMSDTO getDTO(MoniClientLinkDTO clientDto) {
		JMSDTO dto = new JMSDTO();
		dto.setObj(clientDto);
		dto.setTypeId(GBWMsgConstant.C_JMS_MONITOR_LINK_RESPONSE_MSG);
		return dto;
	}
	
	private String getLogMsg(CollectData data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("monitorID=" + data.getCollectTask().getBusTask().getMonitor_id() + ",");
		return buffer.toString();
	}
}