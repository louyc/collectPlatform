package com.neusoft.gbw.cp.collect.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.neusoft.gbw.cp.collect.service.control.StationControlMgr;
import com.neusoft.gbw.cp.collect.service.transfer.ICollect;
import com.neusoft.gbw.cp.collect.vo.SiteConfig;
import com.neusoft.gbw.cp.core.collect.TransferType;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.core.site.MonitorMachine;
import com.neusoft.np.arsf.base.bundle.Log;

public class CollectTaskContext {

	private static class CollectTaskContextHolder {
		private static final CollectTaskContext INSTANCE = new CollectTaskContext();
	}

	private CollectTaskContext() {
		siteConfMap = new HashMap<String, SiteConfig>();
		CodeConfMap = new HashMap<String, Integer>();
		siteMgrMap = new HashMap<String, StationControlMgr>();
		collectMap = new HashMap<TransferType, ICollect>();
		equOccupTimeMap = new HashMap<String, Timer>();
	}
	
	public static CollectTaskContext getModel() {
		return CollectTaskContextHolder.INSTANCE;
	}
	
	private Map<String, SiteConfig> siteConfMap = null;
	private Map<String, Integer> CodeConfMap = null;
	private Map<String, StationControlMgr> siteMgrMap = null;
	private Map<TransferType, ICollect> collectMap = null;
	private Map<String, Timer> equOccupTimeMap = null;
	private Lock lock = new ReentrantLock();
	
	/**
	 * 获取所有站点数据，进行数据缓存，方便以后做站点虚拟管道创建
	 * @param list
	 */
	public void load(Collection<MonitorDevice> list) {
		lock.lock();
		try {
			for(MonitorDevice device : list) {
				SiteConfig config = new SiteConfig();
				config.setMonitorID(device.getMonitor_id()+"");
				if (device.getMachineList().isEmpty()) {
					Log.warn("站点信息不包含接收机信息，" + device.toString());
					continue;
				}
				config.setEquSize(device.getMachineList().size());
				for(MonitorMachine machine : device.getMachineList()) {
					config.addEquCode(machine.getMachine_code());
				}
				config.setMoDevice(device);
				siteConfMap.put(config.getMonitorID(), config);
				CodeConfMap.put(config.getMoDevice().getMonitor_code(), 1);
			}
			Log.debug("[采集服务]初始化监控设备信息，size=" + list.size());
		} finally {
			lock.unlock();
		}
	}
	
	public ICollect getCollectProcessor(TransferType type) {
		if (collectMap.containsKey(type)) 
			return collectMap.get(type);
		return null;
	}
	
	public SiteConfig getSiteConfig(String key) {
		if (siteConfMap.containsKey(key)) 
			return siteConfMap.get(key);
		return null;
	}
	public int getCodeConfMap(String key) {
		if (CodeConfMap.containsKey(key)) 
			return CodeConfMap.get(key);
		return 0;
	}
	
	public StationControlMgr getStationControlMgr(String key) {
		if (siteMgrMap.containsKey(key)) 
			return siteMgrMap.get(key);
		return null;
	}
	
	public Timer getTimer(String key) {
		if (equOccupTimeMap.containsKey(key)) 
			return equOccupTimeMap.get(key);
		return null;
	}
	
	public void add(StationControlMgr mgr) {
		siteMgrMap.put(mgr.getMonitorID(), mgr);
	}
	
	public void add(TransferType type, ICollect collect) {
		collectMap.put(type, collect);
	}
	
	public void add(String key, Timer time) {
		equOccupTimeMap.put(key, time);
	}
	
	public void removeTime(String key) {
		equOccupTimeMap.remove(key);
	}
	
	public void clear() {
		siteConfMap.clear();
		CodeConfMap.clear();
		siteMgrMap.clear();
		collectMap.clear();
		equOccupTimeMap.clear();
	}
}
