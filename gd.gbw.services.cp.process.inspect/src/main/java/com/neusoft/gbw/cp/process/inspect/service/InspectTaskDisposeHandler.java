package com.neusoft.gbw.cp.process.inspect.service;

import java.util.Map;
import java.util.Timer;

import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.process.inspect.constants.InspectConstants;
import com.neusoft.gbw.cp.process.inspect.context.InspectTaskContext;
import com.neusoft.gbw.cp.process.inspect.exception.InspectDisposeException;
import com.neusoft.gbw.cp.process.inspect.process.EquConnectStatusProcessor;
import com.neusoft.gbw.cp.process.inspect.process.TaskStatusProcessor;
import com.neusoft.gbw.cp.process.inspect.vo.InspectTaskTimeOut;
import com.neusoft.gbw.domain.monitor.intf.dto.MoniInspectResultDTO;
import com.neusoft.np.arsf.base.bundle.Log;

public class InspectTaskDisposeHandler {
	
	private EquConnectStatusProcessor process = null;
	private InspectTaskContext context = null;
	private TaskStatusProcessor taskProcess = null;
	
	
	public InspectTaskDisposeHandler() {
		process = new EquConnectStatusProcessor();
		context = InspectTaskContext.getInstance();
		taskProcess = new TaskStatusProcessor();
	}
	
	@SuppressWarnings("unchecked")
	public void dispose(Map<String, Object> map) {
		MoniInspectResultDTO dto = (MoniInspectResultDTO)map.get("inspectDTO");
		//inspectkey= monitorId_timestamp
		String monitorId = dto.getMonitorId();
		String timestamp = dto.getTimeStamp();
		MonitorDevice device =  context.getMonitorDevice(monitorId);
		String ip = device.getDevice_ip();
		isNetConnect(ip, monitorId , timestamp); 
		timeOut(monitorId, timestamp, device.getVersion_id());
		Map<String, String> taskMap = (Map<String, String>)map.get("taskInfo");
		//缓存巡检的任务检查信息
//		cacheTask(monitorId, taskMap);
		try {
			for(String s: taskMap.keySet()){
				taskProcess.disposeTask(dto, taskMap.get(s),s);
			}
		} catch (InspectDisposeException e) {
			Log.error("", e);
		}
	}
	
	private void timeOut(String monitorId, String timestamp, int version_id) {
		//创建巡检超时
		InspectTaskTimeOut timeOut = new InspectTaskTimeOut();
		timeOut.setVersion_id(version_id);
		timeOut.setMonitor_id(Long.parseLong(monitorId));
		timeOut.setTimes_tamp(timestamp);
		Timer timer = new Timer(true);
		InspectTimeOutHandler task = new InspectTimeOutHandler(timeOut);
		timer.schedule(task, 30000);
		timeOut.setTimer(timer);
		context.initInpectTask(monitorId, timeOut);
		Log.debug("创建巡检超时，monitorId=" + monitorId);
	}
	
	private boolean isNetConnect(String monitorIp, String monitorId, String timestamp) {
		return process.isEquConnect(monitorIp,monitorId,timestamp);
	}
	
//	private void cacheTask(String monitorId, Map<String, String> map) {
//		context.setInspectTaskMap(monitorId,map);
//	}
	
}
