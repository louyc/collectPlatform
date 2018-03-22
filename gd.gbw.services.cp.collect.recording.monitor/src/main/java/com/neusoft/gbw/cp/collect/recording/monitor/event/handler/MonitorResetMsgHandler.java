package com.neusoft.gbw.cp.collect.recording.monitor.event.handler;

import java.security.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.neusoft.gbw.cp.collect.recording.monitor.comstant.ConfigVariable;
import com.neusoft.gbw.cp.collect.recording.monitor.comstant.MoniContants;
import com.neusoft.gbw.cp.collect.recording.monitor.mode.MonitorMode;
import com.neusoft.gbw.cp.collect.recording.monitor.utils.FileUtil;
import com.neusoft.gbw.cp.collect.recording.monitor.utils.MonitorUtil;
import com.neusoft.gbw.cp.collect.recording.monitor.vo.MonitorStatus;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class MonitorResetMsgHandler  implements BaseEventHandler{

	private MonitorMode mode = MonitorMode.getInstance();
	private Map timeMap = new HashMap();
	
	@Override
	public String getTopicName() {
		return MoniContants.JMS_RECEIVE_RECORD_MONITOR_MSG_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		String monitorCode = arg0.toString();
		Log.debug("接收采集平台发送的重启监控站点命令，monitorCode=" + monitorCode);
		dispose(monitorCode);
		return true;
	}
	
	private void dispose(String monitorCode) {
		//避免频繁重启，30秒内不做重复重启
		if (timeMap.containsKey(monitorCode)){
			long a = (long) timeMap.get(monitorCode);
			if (System.currentTimeMillis()-a <30000){
				return ;
			}
		}
		Log.debug("采集平台需要重启的站点"+monitorCode+"    "+timeMap.get(monitorCode));
		timeMap.put(monitorCode,System.currentTimeMillis());		
		
		Map<String, String> batMap = null;
		//Map<String, MonitorStatus> statusMap = null;
		//获取bat文件地址
		batMap = mode.getBatMap();
		//获取平台状态
		//statusMap = mode.getStatusMap();
		//录音状态
		//MonitorStatus status = statusMap.get(monitorCode);
		
		long lastTime = FileUtil.readMonitorHeartbit(monitorCode);
		if (lastTime > 0 && (System.currentTimeMillis() - lastTime) < 60000) {
			Log.info("录音平台心跳正常，忽略本次重启操作，monitorCode=" + monitorCode);
			return;
		}
		
		MonitorStatus status = FileUtil.readMonitorStatus(monitorCode);
		//bat快捷方式
		String batPath = batMap.get(monitorCode);
		if(status == null || batPath == null) {
			Log.debug("未找到该站点的录音平台，monitorCode=" + monitorCode);
			return;
		}
		//重启平台
		MonitorUtil.reSetThread("采集平台的重启消息", status, batPath);
		Log.debug("采集平台命令关闭结束");
	}
	
}
