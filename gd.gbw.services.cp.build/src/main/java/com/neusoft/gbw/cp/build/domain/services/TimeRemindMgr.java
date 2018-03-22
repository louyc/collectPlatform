package com.neusoft.gbw.cp.build.domain.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.build.domain.mode.BuildContext;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.util.TimeUtils;
import com.neusoft.gbw.cp.build.interfaces.event.handler.BuildTaskHandler;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.load.data.build.domain.model.LoadContext;
import com.neusoft.gbw.cp.load.data.build.domain.vo.MeasureUnitTime;
import com.neusoft.gbw.cp.schedule.vo.TimeSetMsg;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

public class TimeRemindMgr {
	
	public void start() {
		TimeSetMsg msg = null;
		Map<Integer, Integer> map = loadTaskTimeInterval();
		LoadContext.getInstance().setIntervalMap(map);
		Log.info("获取需要加载分隔时间配置信息，并且合并相同时间间隔的信息。" + map);
		
		Map<Integer, TimeSetMsg> msgMap = new HashMap<Integer, TimeSetMsg>();
		BuildTaskHandler handler = null;
		for(Integer time : map.keySet()){
			//构建下一个或是接下来提醒的收测单元的拆分
			msg = buildRemindMsg(time);
			handler = new BuildTaskHandler();
			handler.setTopicName(msg.getRemindTopic());
			BuildContext.getInstance().addEventList(handler);
			ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.TIME_REMIND_SET_TOPIC, msg);
			Log.debug("消息设置提醒。" + msg.toString());
			msgMap.put(time, msg);
		}
		LoadContext.getInstance().setRemindMap(msgMap);
	}
	
	private Map<Integer, Integer> loadTaskTimeInterval() {
		Map<Integer, List<MeasureUnitTime>> boradIntervalMap = new HashMap<Integer, List<MeasureUnitTime>>();
		Map<Integer, List<MeasureUnitTime>> exporIntervalMap = new HashMap<Integer, List<MeasureUnitTime>>();
		Map<Integer, Integer> intervalMap = new HashMap<Integer, Integer>();
		List<MeasureUnitTime> broadList = null;
		List<MeasureUnitTime> experList = null;
		for(MonitorDevice device : DataMgrCentreModel.getInstance().getMonitorDeviceList()) {
			//原逻辑
//			int interval = device.getMeasureUnitTime();
//			List<MeasureUnitTime> timeList = null;
//			if (intervalMap.containsKey(interval)) {
//				timeList = intervalMap.get(interval);
//			} else {
//				timeList = new ArrayList<MeasureUnitTime>();
//			}
//			timeList.add(buildMeasureUnitTime(device));
//			intervalMap.put(interval, timeList);
			//新逻辑
//			int interval = device.getMeasureUnitTime();//去掉了收测单元间隔，增加实验收测单元和广播收测单元间隔
			int broadInterval = device.getBroadMeasureUnitTime();//广播收测单元间隔
			int experInterval = device.getExperMeasureUnitTime();//实验收测单元间隔
			
			//广播
			if (boradIntervalMap.containsKey(broadInterval)) {
				broadList = boradIntervalMap.get(broadInterval);
			} else {
				broadList = new ArrayList<MeasureUnitTime>();
			}
			broadList.add(buildMeasureUnitTime(device, broadInterval, 3));
			boradIntervalMap.put(broadInterval, broadList);
			//实验
			if (exporIntervalMap.containsKey(experInterval)) {
				experList = exporIntervalMap.get(experInterval);
			} else {
				experList = new ArrayList<MeasureUnitTime>();
			}
			
			experList.add(buildMeasureUnitTime(device,experInterval, 4));
			exporIntervalMap.put(experInterval, experList);

			//封装间隔时间对象
			intervalMap.put(broadInterval, broadInterval);
			intervalMap.put(experInterval, experInterval);
		}
		//封装实验间隔对象和广播间隔对象
		LoadContext.getInstance().setBroadIntervalMap(boradIntervalMap);
		LoadContext.getInstance().setExporIntervalMap(exporIntervalMap);
		
		return intervalMap;
	}
	
//	private MeasureUnitTime buildMeasureUnitTime(MonitorDevice device) {
//		MeasureUnitTime unit = new MeasureUnitTime();
//		unit.setMonitorID(device.getMonitor_id());
//		unit.setTimeInterval(device.getMeasureUnitTime());
//		return unit;
//	}
	
	private MeasureUnitTime buildMeasureUnitTime(MonitorDevice device, int interval, int type) {
		MeasureUnitTime unit = new MeasureUnitTime();
		unit.setMonitorID(device.getMonitor_id());
		unit.setTimeInterval(interval);
		unit.setUnitType(type);
		return unit;
	}
	
	private TimeSetMsg buildRemindMsg(Integer time) {
		TimeSetMsg msg = new TimeSetMsg();
		msg.setRemindTopic(BuildConstants.REMIND_TIME_TOPIC + "_" + time);
		msg.setTimeinterval(time);
		msg.setRemindTime(TimeUtils.getRemindTime(time));
		return msg;
	}
}
