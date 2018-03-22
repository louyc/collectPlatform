package com.neusoft.gbw.cp.load.data.build.domain.control;

import java.util.HashMap;
import java.util.Map;

public class EquStatusControlMode {
	
	private static class EquStatusControlModeModeHolder {
		private static final EquStatusControlMode INSTANCE = new EquStatusControlMode();
	}

	private EquStatusControlMode() {
		init();
	}

	private void init() {
		stationConMap = new HashMap<Long, EquStatusControl>();
	}

	public static EquStatusControlMode getInstance() {
		return EquStatusControlModeModeHolder.INSTANCE;
	}
	
	private Map<Long, EquStatusControl> stationConMap = null;
	
	public void addStation(long monitorId, EquStatusControl status) {
		if(!stationConMap.containsKey(monitorId))
			stationConMap.put(monitorId, status);
	}
	
	public EquStatusControl getStationControl(long monitorId) {
		return stationConMap.get(monitorId);
	}
	
}
