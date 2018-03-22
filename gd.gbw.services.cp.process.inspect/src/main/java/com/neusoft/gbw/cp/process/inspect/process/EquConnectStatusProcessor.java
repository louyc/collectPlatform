package com.neusoft.gbw.cp.process.inspect.process;

import java.util.ArrayList;
import java.util.List;

import com.neusoft.gbw.cp.process.inspect.constants.InspectConstants;
import com.neusoft.gbw.cp.process.inspect.util.InspectUtils;
import com.neusoft.gbw.cp.process.inspect.vo.InspectResultStore;
import com.neusoft.np.arsf.base.bundle.Log;

/**
 * 站点巡检处理
 * @author yanghao
 *
 */
public class EquConnectStatusProcessor extends SendTaskProcessor{
	
	public boolean isEquConnect(String monitorIp, String monitorId, String timestamp) {
		boolean isConnect = false;
		isConnect = InspectUtils.isConnect(monitorIp);
		disposeEquConn(monitorId, timestamp, isConnect);
		return isConnect;
	}
	
	public void disposeEquConn(String monitorId,String timeStemp, boolean isConn) {
		List<InspectResultStore> inspectList = new ArrayList<InspectResultStore>();
		InspectResultStore store = new InspectResultStore();
		store.setInspec_finish_time(getCurrentTime());
		store.setTime_stamp(timeStemp);
		store.setMonitor_id(Long.parseLong(monitorId));
		store.setInspec_code(InspectConstants.inspectProject.NETWORK_CONNECT_CODE);
		if(isConn) {
			store.setInspec_result(InspectConstants.INSPECT_TASK_SUCCESS);
			store.setInspec_message("站点连通性正常");
			store.setInspec_finish_status(1);
		}else {
			store.setInspec_result(InspectConstants.INSPECT_TASK_FAILURE);
			store.setInspec_message("站点连通性异常");
			store.setInspec_finish_status(0);
		}
		
		Log.debug("巡检向前台发送设备站点连通性消息，monitorid=" + store.getMonitor_id() + ",msg=" + store.getInspec_message());
		inspectList.add(store);
		disposeData(inspectList);
//		inspectList.add(store);
//		//发送至存储入库
//		storeInfo(inspectList);
//		//发送至前台
//		sendTask(inspectList);
	}
	
}
