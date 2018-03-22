package com.neusoft.gbw.cp.build.domain.build.equ;

import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniClientLinkDTO;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.constants.ConfigVariable;
import com.neusoft.gbw.cp.build.infrastructure.exception.BuildException;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.StreamRealtimeClientQuery;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.core.collect.TaskPriority;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;

public class StreamClientTaskBuilder {

	public static Query buildRealTimeStreamClientQuery(MoniClientLinkDTO clientDto, TaskPriority taskPriority) throws BuildException {
		Query query = new Query();
		StreamRealtimeClientQuery srcq = buildStreamRealtimeClientQuery();
		query.setVersion(BuildConstants.XML_VERSION_8+"");
		Long msgID = DataUtils.getMsgID(srcq);
		query.setMsgID(msgID+"");
		query.setType(BuildConstants.MSG_DOWN_TYPE);
		query.setDateTime(DataUtils.getCurrentTime());
		query.setSrcCode(ConfigVariable.MSG_SRC_CODE);
		query.setPriority(taskPriority.getCollectPriority()+"");
		query.setQuery(srcq);
		long monitorID = Long.parseLong(clientDto.getMonitorId());
		MonitorDevice info = null;
		info = DataMgrCentreModel.getInstance().getMonitorDevice(monitorID);
		if (info == null) {
			throw new BuildException("获取设备信息为空 ID=" + monitorID);
		} else {
			query.setDstCode(info.getMonitor_code());
		}
		return query;
	}

	private static StreamRealtimeClientQuery buildStreamRealtimeClientQuery() {
		StreamRealtimeClientQuery srcq = new StreamRealtimeClientQuery();
		srcq.setEquCode("");
		return srcq;

	}
}
