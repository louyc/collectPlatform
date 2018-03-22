package com.neusoft.gbw.cp.build.domain.build.collect;

import com.neusoft.gbw.cp.core.business.BusinessTask;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;
import com.neusoft.gbw.domain.task.effect.intf.dto.RealTimeStreamDTO;
import com.neusoft.np.arsf.base.bundle.Log;

public class StreamRealtimeQueryTaskBuilder extends BasicTaskBuilder {

	@Override
	BusinessTask buildBusinessTask(BuildInfo info) {
		String firstEnCode = info.getDevice().getFirstEnCode(); //add yanghao
		RealTimeStreamDTO streamDTO = (RealTimeStreamDTO)info.getBuisness();
		BusinessTask task = null;
		
		try {
			streamDTO = getDTO(streamDTO,firstEnCode);
			task = new BusinessTask();
			task.setCurrentTime(streamDTO.getCurrentTime());
			task.setType(info.getType().getBusTaskType());
			task.setManufacturer_id(info.getDevice().getManufacturer_id());
			task.setFreq(streamDTO.getFreq());
			task.setMonitor_code(info.getDevice().getMonitor_code());
			task.setMonitor_id(Long.parseLong(streamDTO.getMonitorId()));
			//有前台控制是否进行强制播放，目前由采集平台自己定义为强制播放，控制采集是否下发音频任务
			task.setIs_force(streamDTO.getForce());
//			task.setIs_force(BuildConstants.IS_FORCE);
			task.setRecordLength(streamDTO.getTimeOut().equals("") ? 25 : Integer.parseInt(streamDTO.getTimeOut()));
		} catch (Exception e) {
			Log.error("", e);
		}
		
		return task;
		
	}
	
	private RealTimeStreamDTO getDTO(RealTimeStreamDTO streamDTO,String firstEnCode) throws Exception {
		String equCode = streamDTO.getEquCode();
		if(equCode == null || equCode.equals("")) {
			streamDTO.setEquCode(firstEnCode);
		}
		return streamDTO;
	}
	
}
