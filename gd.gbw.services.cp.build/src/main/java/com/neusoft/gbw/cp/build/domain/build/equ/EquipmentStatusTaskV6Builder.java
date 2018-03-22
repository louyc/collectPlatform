package com.neusoft.gbw.cp.build.domain.build.equ;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.constants.ConfigVariable;
import com.neusoft.gbw.cp.build.infrastructure.exception.BuildException;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.conver.util.Time;
import com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentStatusRealtimeQuery;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.core.collect.TaskPriority;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.np.arsf.base.bundle.Log;

public class EquipmentStatusTaskV6Builder {
	
	private static final String reportInterval = "00:00:03";
	private static final String sampleInterval = "00:00:03";
	private static final String expirTime = "00:00:05";

	public static Query buildRealTimeStreamEquipmentStatusQuery(String monitorId, TaskPriority taskPriority) throws BuildException {
		Query query = new Query();
		EquipmentStatusRealtimeQuery quert = buildEquipmentStreamQuery();
		query.setVersion(BuildConstants.XML_VERSION_6 + "");
		Long msgID = DataUtils.getMsgID(quert);
		query.setMsgID(msgID+"");
		query.setType(BuildConstants.MSG_DOWN_TYPE);
		query.setDateTime(DataUtils.getCurrentTime());
		query.setSrcCode(ConfigVariable.MSG_SRC_CODE);
		query.setPriority(taskPriority.getCollectPriority()+"");
		query.setQuery(quert);
		long monitorID = Long.parseLong(monitorId);
		MonitorDevice info = null;
		info = DataMgrCentreModel.getInstance().getMonitorDevice(monitorID);
		if (info == null) {
			throw new BuildException("获取设备信息为空 ID=" + monitorID);
		} else {
			Log.debug("[任务构建]设备状态任务获取 monitorCode=" + info.getMonitor_code());
			query.setDstCode(info.getMonitor_code());
		}
		return query;
	}
	
	private static EquipmentStatusRealtimeQuery buildEquipmentStreamQuery() {
		EquipmentStatusRealtimeQuery quert = new EquipmentStatusRealtimeQuery();
		quert.setReportInterval(new Time(reportInterval)+"");
		quert.setSampleInterval(new Time(sampleInterval)+"");
		quert.setExpireTime(new Time(expirTime)+"");
		return quert;
	}

}
