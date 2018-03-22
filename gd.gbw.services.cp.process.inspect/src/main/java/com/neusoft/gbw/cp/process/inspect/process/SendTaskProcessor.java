package com.neusoft.gbw.cp.process.inspect.process;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.process.inspect.constants.InspectConstants;
import com.neusoft.gbw.cp.process.inspect.context.InspectTaskContext;
import com.neusoft.gbw.cp.process.inspect.vo.InspectResultStore;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.gbw.domain.monitor.intf.dto.MoniInspectResultDTO;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.gbw.infrastructure.msgcenter.srv.GBWMsgConstant;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;

public class SendTaskProcessor {
	
	public void disposeData(List<InspectResultStore> inspectList) {
		updateInspectScore(inspectList);
		storeInfo(inspectList);
		for(InspectResultStore ins: inspectList){
			if(ins.getInspec_code().equals("equUPS")){
				Log.debug("****************************"+ins.getMonitor_id()+"        "+ins.getInspec_finish_score());
				InspectTaskContext.getInstance().removeInspectTask(ins.getMonitor_id() + "");
			}
		}
		updateInspectStatus(inspectList);
		sendTask(inspectList);
	}
	
	public void updateInspectScore(List<InspectResultStore> inspectList) {
		int inspec_finish_status = 0;
		for(InspectResultStore store : inspectList) {
			inspec_finish_status = store.getInspec_finish_status();
			if(1 == inspec_finish_status)
				store.setInspec_finish_score(10);
			else store.setInspec_finish_score(0);
		}
	}

	public void sendTask(List<InspectResultStore> inspectList) {
		for(InspectResultStore store : inspectList) {
			MoniInspectResultDTO dto = converDTO(store);
			JMSDTO jms = createjms(dto);
			ARSFToolkit.sendEvent(InspectConstants.JMS_SEND_MSG_TOPIC, jms);
		}
	}
	
	public void storeInfo(List<InspectResultStore> inspectList) {
		Map<String, Object> mapSet = new HashMap<String, Object>(); 
		for(InspectResultStore store : inspectList) {
			StoreInfo info = new StoreInfo();
			if(null==mapSet.get(store.getInspec_code())|| store.getInspec_code().equals("equReceive") ){
				mapSet.put(store.getInspec_code(), store.getInspec_code());
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
	
	public InspectResultStore getResultDto(MoniInspectResultDTO inspectDTO,String type) {
		InspectResultStore storeDTO = new InspectResultStore();
		storeDTO.setInspec_finish_time(getCurrentTime());
		storeDTO.setTime_stamp(inspectDTO.getTimeStamp());
		storeDTO.setMonitor_id(Long.parseLong(inspectDTO.getMonitorId()));
		storeDTO.setInspec_code(type);
		return storeDTO;
	}
	
	public void updateInspectStatus(List<InspectResultStore> inspectList) {
		for(InspectResultStore store : inspectList) {
			String monitorId = store.getMonitor_id() + "";
			String inspectCode = store.getInspec_code();
			InspectTaskContext.getInstance().updateInspectStata(monitorId, inspectCode);
		}
	}
	
	public boolean isNetSuccess(CollectData data) {
		long monitorId = data.getCollectTask().getBusTask().getMonitor_id();
		return InspectTaskContext.getInstance().isNetConn(monitorId + "");
	}
	
	private MoniInspectResultDTO converDTO(InspectResultStore store) {
		MoniInspectResultDTO dto = new MoniInspectResultDTO();
		dto.setInspecCode(store.getInspec_code());
		dto.setInspecFinishTime(store.getInspec_finish_time());
		dto.setInspecMessage(store.getInspec_message());
		dto.setInspecResult(store.getInspec_result() + "");
		dto.setMonitorId(store.getMonitor_id() + "");
		dto.setTimeStamp(store.getTime_stamp() + "");
		dto.setInspecScore(store.getInspec_finish_score() + "");
		return dto;
	}
	
	
	private JMSDTO createjms(MoniInspectResultDTO dto) {
		JMSDTO jms = new JMSDTO();
		jms.setTypeId(GBWMsgConstant.C_JMS_MONITOR_INSPECT_RESPONSE_MSG);
		jms.setObj(dto);
		return jms;
	}
	
	public static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}

}
