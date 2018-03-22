package com.neusoft.gbw.cp.load.data.build.domain.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.load.data.build.domain.vo.MeasureUnitTime;
import com.neusoft.gbw.cp.schedule.vo.TimeSetMsg;

public class LoadContext {

	private static class BuildContextHolder {
		private static final LoadContext INSTANCE = new LoadContext();
	}

	private LoadContext() {
		init();
	}

	private void init() {
		broadIntervalMap = new HashMap<Integer, List<MeasureUnitTime>>();
		exporIntervalMap = new HashMap<Integer, List<MeasureUnitTime>>();
		intervalMap = new HashMap<Integer, Integer>();
		remindMap = new HashMap<Integer, TimeSetMsg>();
	}

	public static LoadContext getInstance() {
		return BuildContextHolder.INSTANCE;
	}

	private Map<Integer, List<MeasureUnitTime>> broadIntervalMap = null;
	private Map<Integer, List<MeasureUnitTime>> exporIntervalMap = null;
	private Map<Integer, Integer> intervalMap = null;
	private Map<Integer, TimeSetMsg> remindMap = null;
	private Map<Integer, Object> queryVerifierMap = null;
	
	public Map<Integer, Integer> getIntervalMap() {
		return intervalMap;
	}

	public void setIntervalMap(Map<Integer, Integer> intervalMap) {
		this.intervalMap = intervalMap;
	}

	public Map<Integer, List<MeasureUnitTime>> getBroadIntervalMap() {
		return this.broadIntervalMap;
	}

	public void setBroadIntervalMap(Map<Integer, List<MeasureUnitTime>> broadIntervalMap) {
		this.broadIntervalMap = broadIntervalMap;
	}
	
	public Map<Integer, List<MeasureUnitTime>> getExporIntervalMap() {
		return this.exporIntervalMap;
	}
	
	public void setExporIntervalMap(Map<Integer, List<MeasureUnitTime>> exporIntervalMap) {
		this.exporIntervalMap = exporIntervalMap;
	}

	public Map<Integer, TimeSetMsg> getRemindMap() {
		return remindMap;
	}

	public void setRemindMap(Map<Integer, TimeSetMsg> remindMap) {
		this.remindMap = remindMap;
	}

	public Map<Integer, Object> getQueryVerifierMap() {
		return queryVerifierMap;
	}
}
