package com.neusoft.gbw.cp.build.domain.build.equ;

import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniDeviceStatusDTO;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.constants.ConfigVariable;
import com.neusoft.gbw.cp.build.infrastructure.exception.BuildException;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentLogHistoryQuery;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.core.collect.TaskPriority;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.np.arsf.base.bundle.Log;

public class EquipmentHistoryQueryTaskBuilder {

	public static Query buildEquipmentHistoryQuery(Object obj, TaskPriority taskPriority) throws BuildException {
		String monitorId = null;
		String  startDateTime=null;
		String  endDateTime=null;
		MoniDeviceStatusDTO statusDto = (MoniDeviceStatusDTO) obj;
		monitorId = statusDto.getMonitorId();
//		startDateTime=statusDto.getExpireTime();
		startDateTime="2015-10-12 15:30:00";
//		endDateTime=statusDto.getReportInterval();
		endDateTime="2016-10-12 17:30:00";
		
		Query query = new Query();
		EquipmentLogHistoryQuery quert = new EquipmentLogHistoryQuery();
		quert.setStartDateTime(startDateTime);
		quert.setEndDateTime(endDateTime);
		query.setVersion(BuildConstants.XML_VERSION_8 + "");
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
	
}
