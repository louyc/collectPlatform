package com.neusoft.gbw.cp.build.domain.build.collect;

import java.util.HashMap;
import java.util.Map;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.build.infrastructure.util.TimeUtils;
import com.neusoft.gbw.cp.core.business.BusinessTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskOnlineListener;
import com.neusoft.np.arsf.base.bundle.Log;

public class OnlineListenerTaskBuilder extends BasicTaskBuilder {

	@Override
	BusinessTask buildBusinessTask(BuildInfo info) {
		String firstEnCode = info.getDevice().getFirstEnCode(); //add yanghao
		((TaskOnlineListener)info.getBuisness()).setEquCode(firstEnCode);
		TaskOnlineListener onlineTask = (TaskOnlineListener)info.getBuisness();
		BusinessTask task = null;
		
		try {
//			streamDTO = getDTO(streamDTO,firstEnCode);
			task = new BusinessTask();
			task.setType(info.getType().getBusTaskType());
			task.setManufacturer_id(info.getDevice().getManufacturer_id());
			task.setFreq(onlineTask.getFreq());
			task.setMonitor_code(onlineTask.getMonitorCode());
			task.setMonitor_id(onlineTask.getMonitorId());
			task.setIs_force(BuildConstants.NO_FORCE);
			task.setTask_origin_id(onlineTask.getScordTypeId());
			task.setRecordLength(getRecordLength(onlineTask.getMonitorId()));
			task.setTask_id(onlineTask.getId());
			
			task.setTask_build_mode(0); //0 自动评估， 1 手动评估
		} catch (Exception e) {
			Log.error("", e);
		}
		
		return task;
		
	}
	
	private int getRecordLength(long monitorId) {
		int recordLength = 30;
		MonitorDevice info = DataMgrCentreModel.getInstance().getMonitorDevice(monitorId);
		if (info == null) {
			Log.warn("[任务构建]未找到指定的站点信息, monitorId=" + monitorId);
		} else {
			recordLength = Integer.parseInt(info.getTaskRecordLength_listen().equals("")?"30":info.getTaskRecordLength_listen());
		}
		return recordLength;
		
	}
	
	@Override
	Map<String, Object> buildExpandObj(BuildInfo info) {
		TaskOnlineListener task = (TaskOnlineListener)info.getBuisness();
		Map<String, Object> expandObj = new HashMap<String, Object>();
		expandObj.put(ExpandConstants.REPORT_CONTROL_KEY, task.getExpandObj());
		expandObj.put(ExpandConstants.COLLECT_OCCUP_KEY, DataUtils.getMsgID(info));
		return expandObj;
	}
	
//	private RealTimeStreamDTO getDTO(RealTimeStreamDTO streamDTO,String firstEnCode) throws Exception {
//		String equCode = streamDTO.getEquCode();
//		if(equCode == null || equCode.equals("")) {
//			streamDTO.setEquCode(firstEnCode);
//		}
//		return streamDTO;
//	}
//	
}
