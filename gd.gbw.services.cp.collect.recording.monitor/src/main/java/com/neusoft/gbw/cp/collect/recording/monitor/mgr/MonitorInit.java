package com.neusoft.gbw.cp.collect.recording.monitor.mgr;

import java.util.Map;

import com.neusoft.gbw.cp.collect.recording.monitor.comstant.ConfigVariable;
import com.neusoft.gbw.cp.collect.recording.monitor.mode.MonitorMode;
import com.neusoft.gbw.cp.collect.recording.monitor.utils.FileUtil;
import com.neusoft.gbw.cp.collect.recording.monitor.vo.MonitorStatus;
import com.neusoft.np.arsf.base.bundle.Log;

public class MonitorInit {
	
	private MonitorMode mode = MonitorMode.getInstance();
	
	public void init() {
		//获取录音文件路径
		initBatFile();
		//获取录音平台状态
		initMonitorStatus();
	}
	
	public void initBatFile() {
		 Map<String, String> fileMap =  getFileMap(ConfigVariable.MONITOR_BAT_FILE_PATH);
		 for(String fileName : fileMap.keySet()) {
			 String monitorCode =  fileName.substring(0, fileName.indexOf("."));
			 mode.addBatMap(monitorCode, fileMap.get(fileName));
		 }
	}
	
	public void initMonitorStatus() {
	     String monitorCode = null;
		 Map<String, String> fileMap =  getFileMap(ConfigVariable.MONITOR_STATUS_FILE_PATH);
		 for(String fileName : fileMap.keySet()) {
			 String filePath = fileMap.get(fileName);
			 if(!filePath.endsWith(".txt"))
				 continue;
			 String value = FileUtil.readFile(filePath);
			 monitorCode =  fileName.substring(0, fileName.indexOf("."));
			 MonitorStatus status = conver(monitorCode, value);
			 mode.addStatusMap(monitorCode, status);
		 }
	}
	
	/**
	 * 获取存在站点号和线程关系MAP
	 * @param path
	 * @return
	 */
	public Map<String, MonitorStatus> queryMonitorStatus() {
		 return mode.getStatusMap();
	}
	
	private Map<String, String> getFileMap(String path) {
		Map<String, String> fileMap =  FileUtil.readAllFilePath(path);
		 if(fileMap.isEmpty()) {
			 Log.warn("目前没有录音平台启动.......");
		 }
		 return fileMap;
	}
	
	private MonitorStatus conver(String code, String value) {
		 MonitorStatus status = new MonitorStatus();
		 String[] args = value.split("#");
		 status.setMonitorCode(code);
		 status.setMonitorTime(args[0]);
		 status.setThreadId(args[1]);
		 return status;
	}
}