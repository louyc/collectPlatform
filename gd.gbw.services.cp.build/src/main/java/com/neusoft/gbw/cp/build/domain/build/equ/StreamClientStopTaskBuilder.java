package com.neusoft.gbw.cp.build.domain.build.equ;

import java.util.List;

import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniClientLinkDTO;
import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniLinkDTO;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.constants.ConfigVariable;
import com.neusoft.gbw.cp.build.infrastructure.exception.BuildException;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.StreamRealtimeClientStop;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.StreamRealtimeClientStopClientInfo;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.core.collect.TaskPriority;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;

public class StreamClientStopTaskBuilder {

	public static Query buildRealTimeStreamClientStopQuery(MoniClientLinkDTO clientDto, TaskPriority taskPriority) throws BuildException {
		Query query = new Query();
		StreamRealtimeClientStop srcs = buildStreamRealtimeClientStop(clientDto);
		query.setVersion(BuildConstants.XML_VERSION_8+"");
		Long msgID = DataUtils.getMsgID(srcs);
		query.setMsgID(msgID+"");
		query.setType(BuildConstants.MSG_DOWN_TYPE);
		query.setDateTime(DataUtils.getCurrentTime());
		query.setSrcCode(ConfigVariable.MSG_SRC_CODE);
		query.setPriority(taskPriority.getCollectPriority()+"");
		query.setQuery(srcs);
		int monitorID = Integer.parseInt(clientDto.getMonitorId());
		MonitorDevice info = null;
		info = DataMgrCentreModel.getInstance().getMonitorDevice(monitorID);
		if (info == null) {
			throw new BuildException("获取设备信息为空 ID=" + monitorID);
		} else {
			query.setDstCode(info.getMonitor_code());
		}
		
		return query;
	}

	private static StreamRealtimeClientStop buildStreamRealtimeClientStop(MoniClientLinkDTO clientDto) {
		StreamRealtimeClientStop srcs = new StreamRealtimeClientStop();
		if (null!=clientDto.getResList()) {
			List<MoniLinkDTO> list = clientDto.getResList();
		if (!list.isEmpty()) {
			for (MoniLinkDTO moniLinkDTO : list) {
				StreamRealtimeClientStopClientInfo clientInfo = new StreamRealtimeClientStopClientInfo();
				clientInfo.setEquCode(moniLinkDTO.getCode());
				clientInfo.setClientIP(moniLinkDTO.getClientIP());
				srcs.addStreamRealtimeClientStop_ClientInfo(clientInfo);
			}
		}
			
		}
		return srcs;
	}
}
