package com.neusoft.gbw.cp.collect.recording.monitor.mode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.neusoft.gbw.cp.collect.recording.monitor.vo.MonitorStatus;


public class MonitorMode {

	private static class MonitorModeHolder {
		private static final MonitorMode INSTANCE = new MonitorMode();
	}

	private MonitorMode() {
		batMap = new HashMap<String, String>();
		statusMap = new HashMap<String, MonitorStatus>();
	}

	public static MonitorMode getInstance() {
		return MonitorModeHolder.INSTANCE;
	}
	
	//key:monitor_code,value:batPath
	private Map<String, String> batMap = null;
	//key:monitor_code,value:batPath
	private Map<String, MonitorStatus> statusMap = null;
	
	private Lock lock = new ReentrantLock();
	
	public void addStatusMap(String monitorCode, MonitorStatus status) {
		try{
			lock.lock();
			statusMap.put(monitorCode, status);
		}finally {
			lock.unlock();
		}
	}

	public void addBatMap(String monitorCode, String path) {
		try{
			lock.lock();
			batMap.put(monitorCode, path);
		}finally {
			lock.unlock();
		}
	}
	
	public Map<String, MonitorStatus> getStatusMap() {
		try{
			lock.lock();
			return this.statusMap;
		}finally {
			lock.unlock();
		}
	}
	
	public Map<String, String> getBatMap() {
		try{
			lock.lock();
			return this.batMap;
		}finally {
			lock.unlock();
		}
	}
	
}
