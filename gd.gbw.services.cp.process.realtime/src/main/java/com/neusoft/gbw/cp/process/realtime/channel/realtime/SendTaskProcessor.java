package com.neusoft.gbw.cp.process.realtime.channel.realtime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.process.realtime.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.realtime.vo.ManualTaskStore;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;
import com.neusoft.np.arsf.net.core.NetEventType;

public class SendTaskProcessor {

	public void sendTask(Object syntObj) {
		if (syntObj instanceof JMSDTO) {
			ARSFToolkit.sendEvent(ProcessConstants.JMS_SEND_MSG_TOPIC, syntObj);
		} else if (syntObj instanceof String) {
			ARSFToolkit.sendEvent(NetEventType.REST_SYNT.name(), (String)syntObj);
		} else {
			Log.warn("实时任务处理类型不存在，" + syntObj);
		}
	}
	
	public void sendDelTask(ManualTaskStore store) {
		List<StoreInfo> infoList = new ArrayList<StoreInfo>();
		//任务删除 改成 修改任务数据状态---》data_statue 3		0 当前有效、1 预设、2 过期、3 删除
		infoList.add(buildStoreInfo("updateTaskDataStatus", store));
		/*infoList.add(buildStoreInfo("deleteSetTask", store));
		infoList.add(buildStoreInfo("deleteSetTaskFreq", store));
		infoList.add(buildStoreInfo("deleteSetTaskMonitor", store));
		infoList.add(buildStoreInfo("deleteSetTaskConf", store));
		infoList.add(buildStoreInfo("deleteSetTaskFreqSchedule", store));
		infoList.add(buildStoreInfo("deleteSetTaskRecover", store));*/
		sendStore(infoList);
	}
	
	public void sendReSetTask(ManualTaskStore store) {
		List<StoreInfo> infoList = new ArrayList<StoreInfo>();
		infoList.add(buildStoreInfo("updateReSetDeleteTask", store));
		sendStore(infoList);
	}
	
	public void sendUpdateSetTaskStatus(ManualTaskStore store) {
		List<StoreInfo> infoList = new ArrayList<StoreInfo>();
		infoList.add(buildStoreInfo("updateSetTaskStatus", store));
		sendStore(infoList);
	}
	
	public StoreInfo sendUpdateBigTaskSetStatus(ManualTaskStore store) {
		List<StoreInfo> infoList = new ArrayList<StoreInfo>();
		StoreInfo info = new StoreInfo();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("task_id", store.getTask_id());
		dataMap.put("issue_user_id", "system");
		dataMap.put("issue_time", getCurrentTime());
		dataMap.put("task_status_id", store.getSet_status());
		//然后返回StoreInfo对象，一个collectData产生一个StoreInfo，
		info.setLabel("updateTaskSyncStatusRecords");
		info.setDataMap(dataMap);
		infoList.add(info);
		sendStore(infoList);
		return info;
	}
	
	private StoreInfo buildStoreInfo(String label, Object qData) {
		Map<String, Object> map = null;
		StoreInfo info = new StoreInfo();
		try {
			map = NMBeanUtils.getObjectField(qData);
			info.setDataMap(map);
			info.setLabel(label);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
		}
		return info;
	}
	
	private String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}

	private void sendStore(List<StoreInfo> infoList) {
		if(infoList != null && !infoList.isEmpty()) {
			for(StoreInfo info : infoList) {
				ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
			}
		}
	}
}
