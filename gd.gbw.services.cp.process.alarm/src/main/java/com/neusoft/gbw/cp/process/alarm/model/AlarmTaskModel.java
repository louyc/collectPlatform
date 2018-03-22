package com.neusoft.gbw.cp.process.alarm.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.neusoft.gbw.cp.process.alarm.vo.AlarmInfo;

public class AlarmTaskModel {

	private static class Holder {
		private static final AlarmTaskModel INSTANCE = new AlarmTaskModel();
	}
	
	private AlarmTaskModel(){
		euqAlarm = new HashMap<String, AlarmInfo>();
	}
	
	public static AlarmTaskModel getInstance() {
		return Holder.INSTANCE;
	}
	
	private Map<String, AlarmInfo> euqAlarm = null;
	
	private Lock lock = new ReentrantLock();
	
	public void cacheAlarm(Object obj) {
		lock.lock();
		try {
			String key = "";
			if(euqAlarm.containsKey(key)) 
				return;
			AlarmInfo alarm = new AlarmInfo();
			euqAlarm.put(key, alarm);
		}finally {
			lock.unlock();
		}
	}
	
	public void removeAlarm(String key) {
		lock.lock();
		try {
			if(euqAlarm.containsKey(key))
				euqAlarm.remove(key);
		}finally {
			lock.unlock();
		}
	}
	
	public int getAlarmConut(String key) {
		lock.lock();
		try {
			if(euqAlarm.containsKey(key))
				return euqAlarm.remove(key).getAlarmConut();
			return -1;
		}finally {
			lock.unlock();
		}
	}
	
	public void addAlarmConut(String key) {
		lock.lock();
		try {
			if(euqAlarm.containsKey(key))
				euqAlarm.get(key).addAlarmConut();;
		}finally {
			lock.unlock();
		}
	}
	
}