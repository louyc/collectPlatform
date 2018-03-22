package com.neusoft.gbw.cp.process.inspect.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.process.inspect.constants.InspectConstants;
import com.neusoft.gbw.cp.process.inspect.context.InspectTaskContext;
import com.neusoft.gbw.cp.process.inspect.vo.InspectResultStore;
import com.neusoft.gbw.cp.process.inspect.vo.InspectTaskTimeOut;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.gbw.domain.monitor.intf.dto.MoniInspectResultDTO;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.gbw.infrastructure.msgcenter.srv.GBWMsgConstant;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;

public class InspectTimeOutHandler extends TimerTask {
	
	private static String inspectNoCheckV8 = "deviceLog,taskExsit,taskRunning";
	private static String inspectNoCheckV7 = "deviceLog,programRunning,taskExsit,taskRunning";
	private static String inspectNoCheckV6 = "deviceLog,programRunning,taskExsit,taskRunning";
	private static String inspectNoCheckV5 = "deviceLog,programRunning,taskExsit,taskRunning";
	
	private InspectTaskTimeOut timeOut = null;
	
	public InspectTimeOutHandler(InspectTaskTimeOut timeOut) {
		this.timeOut = timeOut;
	}

	@Override
	public void run() {
		List<InspectResultStore> inspectList = new ArrayList<InspectResultStore>();
		InspectResultStore store = null;
		Log.debug("巡检任务到期....");
		//搜索本次巡检任务未完成的监测项 (版本不同，有不同的巡检项)
		int versionId = timeOut.getVersion_id();
		long monitorId = timeOut.getMonitor_id();
		String timesTamp = timeOut.getTimes_tamp();
		Map<String, Integer> inspectMap = timeOut.getStatus();
		for(String key :inspectMap.keySet()) {
			String inspectCode = key;
			int status = inspectMap.get(key);
			if(status == 1) //该巡检项已完成
				continue;
			//根据不同的版本过滤不需要检测的巡检项
			switch(versionId) {
			case 5:
				if(inspectNoCheckV5.contains(inspectCode))
					continue;
				break;
			case 6:
				if(inspectNoCheckV6.contains(inspectCode))
					continue;
				break;
			case 7:
				if(inspectNoCheckV7.contains(inspectCode))
					continue;
				break;
			case 8:
				if(inspectNoCheckV8.contains(inspectCode))
					continue;
				break;
			}
			//校验任务状态查询，如果没有任务，则返回该设备没有任务提示
			if(isTaskStatusCode(inspectCode))
				store =  getNoTaskDto(inspectCode,monitorId, timesTamp);
			else store =  getResultDto(inspectCode,monitorId, timesTamp);
			inspectList.add(store);
		}
		//存储巡检超时对象
		storeInfo(inspectList);
		//将超时信息发送至前台
		sendTask(inspectList);
		InspectTaskContext.getInstance().removeInspectTask(monitorId + "");
	}
	
	private boolean isTaskStatusCode(String inspectCode) {
		return inspectCode.equals(InspectConstants.inspectProject.TASK_EXSIT_STATE_CODE) || inspectCode.equals(InspectConstants.inspectProject.TASK_RUNNING_STATE_CODE);
	}
	
	public InspectResultStore getNoTaskDto(String inspectCode, long monitorId, String timesTamp) {
		InspectResultStore storeDTO = new InspectResultStore();
		storeDTO.setInspec_finish_time(getCurrentTime());
		storeDTO.setTime_stamp(timesTamp);
		storeDTO.setMonitor_id(monitorId);
		storeDTO.setInspec_code(inspectCode);
		storeDTO.setInspec_result(InspectConstants.INSPECT_TASK_SUCCESS);
		storeDTO.setInspec_message("该站点没有设置手动任务");
		storeDTO.setInspec_finish_status(2);
		storeDTO.setInspec_finish_score(0);
		return storeDTO;
	}
	
	public void sendTask(List<InspectResultStore> inspectList) {
		JMSDTO jms = null;
		for(InspectResultStore store : inspectList) {
			MoniInspectResultDTO dto = converDTO(store);
			jms = createjms(dto);
			ARSFToolkit.sendEvent(InspectConstants.JMS_SEND_MSG_TOPIC, jms);
			Log.debug("本批次站点巡检完成，发送至前台jms=" + jms.toString());
		}

	}
	
	public void storeInfo(List<InspectResultStore> inspectList) {
		Map<String, Object> mapSet = new HashMap<String, Object>(); 
		for(InspectResultStore store : inspectList) {
			if(null==mapSet.get(store.getInspec_code())|| store.getInspec_code().equals("equReceive") ){
				StoreInfo info = new StoreInfo();
				Map<String, Object> map = null;
				try {
					map = NMBeanUtils.getObjectField(store);
					info.setDataMap(map);
					info.setLabel(InspectConstants.INSERT_INSPECT_RESULT_STORE);
				} catch (NMBeanUtilsException e) {
					Log.error("", e);
				}
				ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
			}
		}
	}
	
	public InspectResultStore getResultDto(String inspectCode, long monitorId, String timesTamp) {
		InspectResultStore storeDTO = new InspectResultStore();
		storeDTO.setInspec_finish_time(getCurrentTime());
		storeDTO.setTime_stamp(timesTamp);
		storeDTO.setMonitor_id(monitorId);
		storeDTO.setInspec_code(inspectCode);
		storeDTO.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
		storeDTO.setInspec_message("巡检任务执行超时");
		return storeDTO;
	}
	
	private MoniInspectResultDTO converDTO(InspectResultStore store) {
		MoniInspectResultDTO dto = new MoniInspectResultDTO();
		dto.setInspecCode(store.getInspec_code());
		dto.setInspecFinishTime(store.getInspec_finish_time());
		dto.setInspecMessage(store.getInspec_message());
		dto.setInspecResult(store.getInspec_result() + "");
		dto.setMonitorId(store.getMonitor_id() + "");
		dto.setTimeStamp(store.getTime_stamp() + "");
		return dto;
	}
	
	
	private JMSDTO createjms(MoniInspectResultDTO dto) {
		JMSDTO jms = new JMSDTO();
		jms.setTypeId(GBWMsgConstant.C_JMS_MONITOR_INSPECT_RESPONSE_MSG);
		jms.setObj(dto);
		return jms;
	}
	
	private static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}
}
