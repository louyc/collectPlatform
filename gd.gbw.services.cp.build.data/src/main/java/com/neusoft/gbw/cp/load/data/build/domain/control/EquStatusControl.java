package com.neusoft.gbw.cp.load.data.build.domain.control;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.load.data.build.domain.vo.EquOccupStatus;
import com.neusoft.gbw.cp.load.data.build.domain.vo.EquRunStatus;

public class EquStatusControl {

	//key: 接收机编码
	private Map<String, EquOccupStatus> equStatusMap = null;
	private Lock lock = new ReentrantLock();
	
	public EquStatusControl() {
		equStatusMap = new HashMap<String, EquOccupStatus>();
	}
	
	
	public void addEquStatusCotrol(String key) {
		//启动添加接收机状态
		lock.lock();
		try {
			EquOccupStatus status = new EquOccupStatus();
			status.setEquCode(key);
			status.setStatus(EquRunStatus.free);
			equStatusMap.put(key, status);
		} finally {
			lock.unlock();
		}
	}
	
	public void addCurrentCollectTask(String code, CollectTask task) {
		lock.lock();
		try {
			if (equStatusMap.containsKey(code)) {
//				equStatusMap.get(code).setCurrentTask(task);
				equStatusMap.get(code).setOccupKey(getCollectOccupID(task));
			}
		} finally {
			lock.unlock();
		}
	}
	
	private String getCollectOccupID(CollectTask task) {
		Object id = task.getExpandObject(ExpandConstants.COLLECT_OCCUP_KEY);
		return id == null ? null : id.toString();
	}
	
	public EquOccupStatus getEquOccupStatus(String code) {
		if (equStatusMap.containsKey(code))
			return equStatusMap.get(code);
		return null;
	}
	
	public void removeCollectTaskByEquCode(String code) {
		lock.lock();
		try {
			if (equStatusMap.containsKey(code)) {
//				equStatusMap.get(code).setCurrentTask(null);
				equStatusMap.get(code).setOccupKey(null);
			}
		} finally {
			lock.unlock();
		}
	}
	/**
	 * 接收机是否空闲，空闲 true， 占用 false
	 * @param code
	 * @return
	 */
	public boolean isFreeByEquCode(String code) {
		if (equStatusMap.containsKey(code)) {
			return equStatusMap.get(code).getStatus().equals(EquRunStatus.free);
		}
		return false;
	}
	
	public void setFreeStatusByEquCode(String code) {
		lock.lock();
		try {
			if (equStatusMap.containsKey(code)) {
				equStatusMap.get(code).setStatus(EquRunStatus.free);
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void setRunStatusByEquCode(String code) {
		lock.lock();
		try {
			if (equStatusMap.containsKey(code)) {
				equStatusMap.get(code).setStatus(EquRunStatus.run);
			}
		} finally {
			lock.unlock();
		}
	}
	
	public void clear() {
		this.equStatusMap.clear();
	}
}
