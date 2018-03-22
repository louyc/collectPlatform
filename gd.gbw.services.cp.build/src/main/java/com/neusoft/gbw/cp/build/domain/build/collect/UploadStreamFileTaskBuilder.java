package com.neusoft.gbw.cp.build.domain.build.collect;

import java.util.HashMap;
import java.util.Map;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.core.business.BusinessTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;
import com.neusoft.gbw.domain.exception.NXmlException;
import com.neusoft.gbw.domain.task.mgr.intf.dto.DownLoadFileDTO;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskRecordDataDTO;
import com.neusoft.gbw.domain.util.ConverterUtil;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.net.core.NetEventProtocol;

public class UploadStreamFileTaskBuilder extends BasicTaskBuilder {
	
	@Override
	BusinessTask buildBusinessTask(BuildInfo info) {
//		CollectTask ctask = (CollectTask)((Map<String, Object>)info.getBuisness()).get("COLLECTTASK");
		BusinessTask task =  new BusinessTask();
		task.setType(info.getType().getBusTaskType());
		task.setTask_id(Integer.parseInt(DataUtils.getMsgID(info) + "")); //后面匹配回收任务超时时间用
		task.setMonitor_id(info.getDevice().getMonitor_id());
//		task.setFreq(task.getFreq());
		task.setMonitor_code(info.getDevice().getMonitor_code());
//		task.setTask_id(ctask.getBusTask().getTask_id());
		task.setIs_force(BuildConstants.NO_FORCE); //add by jiahao
		return task;
	}
	
	@Override
	Map<String, Object> buildExpandObj(BuildInfo info) {
		Map<String, Object> expandObj = new HashMap<String, Object>();
		expandObj.put(ExpandConstants.REPORT_CONTROL_KEY, info.getBuisness());
		expandObj.put(ExpandConstants.RECORD_FILE_RECOVER_KEY, createData(info.getBuisness()));
		
		return expandObj;
	}
	
	private Map<String, String> createData(Object task) {
		DownLoadFileDTO dto = getDTO(task);
		Map<String, String> map = new HashMap<String,String>();
		for(TaskRecordDataDTO taskDto: dto.getTaskRecordList()) {
			if(taskDto.getFilename().split("/").length>1){
				map.put(taskDto.getFilename().split("/")[1], taskDto.getId());
			}else{
				map.put(taskDto.getFilename(), taskDto.getId());
			}
		}
		return map;
	}
	@SuppressWarnings("unchecked")
	private static DownLoadFileDTO getDTO(Object task){
		DownLoadFileDTO dto = null;
		try {
			String request = ((Map<String, String>)task).get(NetEventProtocol.SYNT_REQUEST);
			dto = (DownLoadFileDTO)ConverterUtil.xmlToObj(request);
		} catch (NXmlException e) {
			Log.error("", e);
		}
		return dto;
	}
	
}
