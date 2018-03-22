package com.neusoft.gbw.cp.collect.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.neusoft.gbw.cp.collect.vo.CollectTimeOutInfo;

public class CollectTaskModel {
	
	private static class CollectTaskTimeOutModelHolder {
		private static final CollectTaskModel INSTANCE = new CollectTaskModel();
	}

	private CollectTaskModel() {
		collectMap = new HashMap<String,CollectTimeOutInfo>();
	}
	
	public static CollectTaskModel getModel() {
		return CollectTaskTimeOutModelHolder.INSTANCE;
	}

	private Map<String, CollectTimeOutInfo> collectMap = null;
	public Map<String, CollectTimeOutInfo> getCollectMap() {
		return collectMap;
	}

	private Lock lock = new ReentrantLock();
	
	public void add(CollectTimeOutInfo info) {
		lock.lock();
		try {
			collectMap.put(info.getCollectTaskID(), info);
		} finally {
			lock.unlock();
		}
	}
	
	public CollectTimeOutInfo get(String id) {
		lock.lock();
		try {
			return collectMap.get(id);
		} finally {
			lock.unlock();
		}
	}
	
	public void changeTimeout(String id) {
		lock.lock();
		try {
			collectMap.get(id).setTimeOut(true);
		} finally {
			lock.unlock();
		}
	}
	
	public void cancelTimer(String id) {
		lock.lock();
		try {
			if(null !=collectMap.get(id).getTimer()){
				collectMap.get(id).getTimer().cancel();
			}
		} finally {
			lock.unlock();
		}
	}
	
	public boolean constainKey(String id) {
		lock.lock();
		try {
			if(collectMap.containsKey(id))
				return true;
		} finally {
			lock.unlock();
		}
		return false;
	}
	
	public void delete(String id) {
		lock.lock();
		try {
				collectMap.remove(id);
		} finally {
			lock.unlock();
		}
	}
	
	public void clear() {
		collectMap.clear();
	}
}
