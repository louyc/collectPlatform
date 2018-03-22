package com.neusoft.gbw.cp.process.measure.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.neusoft.gbw.cp.process.measure.vo.auto.MeaUnitCalculate;
import com.neusoft.np.arsf.base.bundle.Log;

public class MesureRecordModel {
	private static class MesureUnitContextHolder {
		private static final MesureRecordModel INSTANCE = new MesureRecordModel();
	}
	
	private MesureRecordModel(){
		calMap = new HashMap<String, MeaUnitCalculate>();
		unitMap = new HashMap<String, Map<String, Boolean>>();
		recordStatusMap = new HashMap<String, Integer>();
	}
	
	public static MesureRecordModel getInstance() {
		return MesureUnitContextHolder.INSTANCE;
	}
	//遥控站的手动和自动任务不再统计数量，不再应用；
	private Map<String, MeaUnitCalculate> calMap = null;
	//key unitbegin value Map<key,Boolean> key=mode + "_" + unitTime + "_" + beginTime + "_" + runType;
	private Map<String, Map<String, Boolean>> unitMap = null;
	//key:monitorCode,value:count 
	private Map<String, Integer> recordStatusMap = null;
	private Lock lock = new ReentrantLock();
	
	public boolean checkMonitorRecordCount(String monitorCode) {
		lock.lock();
		boolean status = false;
		try {//连续录音次数，如果连续录音次数超过两次没录上，则给监控程序发消息重启平台(为了准确性)
			if(!recordStatusMap.containsKey(monitorCode)) {
				recordStatusMap.put(monitorCode, 1);
				return status;
			}else if(recordStatusMap.get(monitorCode) >= 2) {
				recordStatusMap.remove(monitorCode);
				return true;
			}else {
				recordStatusMap.put(monitorCode, 2);
			}
				
		} finally {
			lock.unlock();
		}
		return status;
	}
	
	public void syncUnitInfo(String beginTime, String unitKey) {
		lock.lock();
		Map<String, Boolean> map = null;
		try {//如果新的收测单元出现，则清除原有收测单元
			if (!unitMap.containsKey(beginTime)) 
				 unitMap.clear();
			if (unitMap.get(beginTime) == null) {
				map = new HashMap<String,Boolean>();
				map.put(unitKey, false);
				unitMap.put(beginTime, map);
			} else unitMap.get(beginTime).put(unitKey, false);
		} finally {
			lock.unlock();
		}
	}
	
	public boolean isFinishedStatus(String beginTime, String unitKey) {
		boolean flag = false;
		lock.lock();
		try {
			if (!unitMap.containsKey(beginTime)) 
				 return flag;
			unitMap.get(beginTime).put(unitKey, true);
			Map<String, Boolean> map = unitMap.get(beginTime);
			for(boolean value : map.values()) {
				if(!value) 
					break;
				flag = true;
			}
		} finally {
			lock.unlock();
		}
		return flag;
	}
	
	public void syncMesureTask(String key, int size, Timer timer) {
		lock.lock();
		try {
			if (calMap.containsKey(key)) {
				calMap.remove(key);
			}
			MeaUnitCalculate cal = new MeaUnitCalculate();
			cal.setCollectTaskCount(size);
			cal.setRecordDataCount(0);
			cal.setTimer(timer);
			calMap.put(key, cal);
		} finally {
			lock.unlock();
		}
	}
	
	public boolean isEmpty() {
		return calMap.isEmpty();
	}
	
	public void updateRecordCount(String key) {
		lock.lock();
		try {
			if (!calMap.containsKey(key)) {
				return;
			}
			MeaUnitCalculate cal = calMap.get(key);
			int count = cal.getRecordDataCount() + 1;
			calMap.get(key).setRecordDataCount(count);
			Log.info("完成本次录音收集，录音数量=" + count);
		} finally {
			lock.unlock();
		}
	}
	
	public Set<String> getKey() {
		return calMap.keySet();
	}
	
	public boolean isOverTask(String key) {
		lock.lock();
		try {
			if (!calMap.containsKey(key)) {
				return false;
			}
			MeaUnitCalculate cal = calMap.get(key);
			int collectTaskCount = cal.getCollectTaskCount();
			int recordDataCount = cal.getRecordDataCount();
			if(collectTaskCount != 0 && collectTaskCount == recordDataCount) {
				return true;
			} 
			return false;
		} finally {
			lock.unlock();
		}
	}
	
	public void remove(String key) {
		lock.lock();
		try {
			if (!calMap.containsKey(key)) {
				return;
			}
			MeaUnitCalculate cal = calMap.get(key);
			cal.getTimer().cancel();
			calMap.remove(key);
		} finally {
			lock.unlock();
		}
	}
}
