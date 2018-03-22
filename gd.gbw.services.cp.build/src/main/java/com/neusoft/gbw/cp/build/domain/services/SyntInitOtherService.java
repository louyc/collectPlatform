package com.neusoft.gbw.cp.build.domain.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.constants.ConfigVariable;
import com.neusoft.gbw.cp.build.infrastructure.util.BusinessUtils;
import com.neusoft.gbw.cp.build.infrastructure.util.TimeUtils;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.load.data.build.domain.model.LoadContext;
import com.neusoft.gbw.cp.load.data.build.domain.vo.MeasureUnitTime;
import com.neusoft.gbw.cp.schedule.vo.TimeSetMsg;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.core.transfer.vo.TransferConstants;
import com.neusoft.np.arsf.core.transfer.vo.conf.JMSConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferConfig;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferDataType;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferModelType;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferType;

public class SyntInitOtherService {

	private static Map<String,String> addrMap = new HashMap<String,String>();
	
	public Map<String, String> getAddrMap() {
		return addrMap;
	}
	public void initJMSReceiveConfig() {
		TransferConfig config = new TransferConfig();
		config.setTopicName(BuildConstants.JMS_RECEIVE_MSG_TOPIC);
		config.setType(TransferType.RECEIVE);
		config.setModeType(TransferModelType.JMS);
		JMSConfig jmsConf = new JMSConfig();
		jmsConf.setConfigType(ConfigVariable.TRANSFER_APP_TYPE);//配置类型，集群：1  单机：0
		switch(ConfigVariable.TRANSFER_APP_TYPE) {
		case 1:
			jmsConf.setUrl(ConfigVariable.TRANSFER_APP);
			break;
		case 0:
			jmsConf.setIp(ConfigVariable.TRANSFER_APP);
		}
		jmsConf.setPort(61616);
		jmsConf.setUsername("neusoft");
		jmsConf.setPassword("neusoft");
		jmsConf.setSessionName("cp");
		jmsConf.setTopicName("msg2cp");
		jmsConf.setAsyncDispatch(false);
		jmsConf.setDeliveryMode(false);
		jmsConf.setJmsType(1);
		jmsConf.setThreadNum(1);
		jmsConf.setDataType(TransferDataType.OBJECT);
		config.setConfig(jmsConf);
		ARSFToolkit.sendEvent(TransferConstants.TRANSFER_INIT_CONF_TOPIC, config);
	}
	/**
	 * 初始化设备站点信息
	 */
	public void initMonitorMachine() {
		Collection<MonitorDevice> monitorList = DataMgrCentreModel.getInstance().getMonitorDeviceList();
		Collection<MonitorDevice> newList = new ArrayList<MonitorDevice>();
		for(MonitorDevice device : monitorList) {
			if(BusinessUtils.judePartformIsDone(String.valueOf(device.getMonitor_id()))){
				newList.add(device);
			}
		}
		Log.debug("本采集平台可以采集的站点个数有：：："+newList.size());
		ARSFToolkit.sendEvent(EventServiceTopic.SYNT_MONITOR_DEVICE_TOPIC, newList);
	}
	/**
	 * 初始化数据库轮巡提醒
	 */
	public void initSyntDB() {
		int time = ConfigVariable.SYNT_DB_TIME;//单位分钟
		TimeSetMsg msg = new TimeSetMsg();
		msg.setRemindTopic(EventServiceTopic.MANUAL_TASK_SYNT_DB_TOPIC);
		msg.setTimeinterval(time);
		msg.setRemindTime(TimeUtils.getCurrentTime());
		Log.debug("数据库更新消息设置提醒。" + msg.toString());
		ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.TIME_REMIND_SET_TOPIC, msg);
	}
	/**
	 * 删除三满任务（in_use =0） 提醒
	 */
	public void initDeleteTask() {
		int time = ConfigVariable.SYNT_DELETE_TASK_TIME;//单位分钟
		TimeSetMsg msg = new TimeSetMsg();
		msg.setRemindTopic(EventServiceTopic.MANUAL_AUTO_DELETE_TASK_TOPIC);
		msg.setTimeinterval(time);
		msg.setRemindTime(TimeUtils.getCurrentTime());
		Log.debug("删除设置类任务消息设置提醒。" + msg.toString());
		ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.TIME_REMIND_SET_TOPIC, msg);
	}
	/**
	 * 初始化站点连通及软件存活提醒
	 */
	public void initCheckTransfer() {
		int switchNum = ConfigVariable.SITE_CHECK_TRANSFER_SWITCH;
		if(switchNum == 0) {
			Log.debug("站点连通性以及软件存活校验功能已关闭");
			return;
		}
		
		int time = ConfigVariable.SITE_CHECK_TRANSFER_TIME;//单位分钟
		TimeSetMsg msg = new TimeSetMsg();
		msg.setRemindTopic(EventServiceTopic.SITE_CHECK_TRANSFER_TOPIC);
		msg.setTimeinterval(time);
		msg.setRemindTime(TimeUtils.getCurrentTime());
		Log.debug("站点连通性以及软件存活校验消息设置提醒。" + msg.toString());
		ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.TIME_REMIND_SET_TOPIC, msg);
	}
	/**
	 * 质量任务自动回收提醒
	 */
	public void initRecoverTaskData() {
		int switchNum = ConfigVariable.RECOVER_DATA_TIME_SWITCH;
		if(switchNum == 0) {
			Log.debug("采集平台采集任务自动回收功能已关闭");
			return;
		}
		
		int time = ConfigVariable.RECOVER_DATA_TIME; //数据量太大，10分钟执行一次
		TimeSetMsg msg = new TimeSetMsg();
		msg.setRemindTopic(EventServiceTopic.RECOVER_TASK_DATA_TOPIC);
		msg.setTimeinterval(time);
		msg.setTimeUnit(ConfigVariable.RECOVER_DATA_UNIT); //时间间隔单位：1代表小时，0代表分钟
		try {
			msg.setRemindTime(TimeUtils.getCurrentTime());
		} catch (Exception e) {
			Log.error("", e);
		}
		Log.debug("任务回收消息设置提醒。" + msg.toString());
		ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.TIME_REMIND_SET_TOPIC, msg);
	}
	/**
	 * 初始化告警类型
	 */
	public void initAlarmType() {
		Map<Integer, String> map = DataMgrCentreModel.getInstance().getEquAlarmTypeMap();
		Map<Integer, String> typeMap = new HashMap<Integer, String>();
		try{
		for(Integer key : map.keySet())
			typeMap.put(key, map.get(key));
		}catch(Exception e){
			Log.debug("SyntInitOtherService 类抛出：  * NullPointerException  ClassCastException  IllegalArgumentException ");
		}
		ARSFToolkit.sendEvent(EventServiceTopic.AlARM_TYPE_TOPIC, typeMap);
	}
	/**
	 * 初始化指标类型
	 */
	public void initQualityType() {
		Map<String, String> map = DataMgrCentreModel.getInstance().getQualityTypeMap();
		Map<String, String> typeMap = new HashMap<String, String>();
		try{
		for(String key : map.keySet())
			typeMap.put(key, map.get(key));
		}catch(Exception e){
			Log.debug("SyntInitOtherService 类抛出：  * NullPointerException  ClassCastException  IllegalArgumentException ");
		}
		ARSFToolkit.sendEvent(EventServiceTopic.QUALITY_TYPE_TOPIC, typeMap);
	}
	/**
	 * 初始化ftp信息（ftp 用户，密码）
	 */
	public void initFtpServer() {
		Map<String, String> map = DataMgrCentreModel.getInstance().getFtpServerMap();
		Map<String, String> addrMap = new HashMap<String, String>();
		try{
		for(String key : map.keySet())
			addrMap.put(key, map.get(key));
		ARSFToolkit.sendEvent(EventServiceTopic.FTP_SERVER_TOPIC, addrMap);
		}catch(Exception e){
			Log.debug("SyntInitOtherService 类抛出：  * NullPointerException  ClassCastException  IllegalArgumentException ");
		}
	}
	/**
	 * 初始化文件路径
	 */
	public void initRecordAddr() {
		Map<String, String> map = DataMgrCentreModel.getInstance().getRecordStoreAddr();
		try{
			addrMap.clear();
			for(String key : map.keySet())
				addrMap.put(key, map.get(key));
			ARSFToolkit.sendEvent(EventServiceTopic.RECORD_ADDRESS_TOPIC, addrMap);
		}catch(Exception  e){
			Log.debug("SyntInitOtherService 类抛出：  * NullPointerException  ClassCastException  IllegalArgumentException ");
		}
	}
	/**
	 * 初始化实时收测站点信息
	 */
	public void initRealMeasureSite() {
		Map<String, String> map = DataMgrCentreModel.getInstance().getRealMeasureSite();
		Map<String, String> siteMap = new HashMap<String, String>();
		try{
		for(String key : map.keySet())
			siteMap.put(key, map.get(key));
		ARSFToolkit.sendEvent(EventServiceTopic.REAL_MEASURE_SITE_TOPIC, siteMap);
		}catch(Exception e){
			Log.debug("SyntInitOtherService 类抛出：  * NullPointerException  ClassCastException  IllegalArgumentException ");
		}
	}
	/**
	 * 初始化回收时间
	 */
	public void initRecoverTimeData() {
		DataMgrCentreModel.getInstance().initRecoverTimeData();
	}
	/**
	 * 初始化收测时间间隔
	 * @return
	 */
	public Map<Integer, Integer> initTaskTimeInterval() {
		Map<Integer, List<MeasureUnitTime>> boradIntervalMap = new HashMap<Integer, List<MeasureUnitTime>>();
		Map<Integer, List<MeasureUnitTime>> exporIntervalMap = new HashMap<Integer, List<MeasureUnitTime>>();
		Map<Integer, Integer> intervalMap = new HashMap<Integer, Integer>();
		List<MeasureUnitTime> broadList = null;
		List<MeasureUnitTime> experList = null;
		try{
		for(MonitorDevice device : DataMgrCentreModel.getInstance().getMonitorDeviceList()) {
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
		}catch(Exception  e){
			Log.debug("SyntInitOtherService 类抛出：  * NullPointerException  ClassCastException  IllegalArgumentException ");
		}
		//封装实验间隔对象和广播间隔对象
		LoadContext.getInstance().setBroadIntervalMap(boradIntervalMap);
		LoadContext.getInstance().setExporIntervalMap(exporIntervalMap);
		
		return intervalMap;
	}
	//创建收测时间对象
	private MeasureUnitTime buildMeasureUnitTime(MonitorDevice device, int interval, int type) {
		MeasureUnitTime unit = new MeasureUnitTime();
		unit.setMonitorID(device.getMonitor_id());
		unit.setTimeInterval(interval);
		unit.setUnitType(type);
		return unit;
	}
	/**
	 * 加载运行图发射台信息
	 */
	public void initStationInfo() {
		Map<String, String> map = DataMgrCentreModel.getInstance().getRealMeasureStation();
		Map<String, String> stationMap = new HashMap<String, String>();
		try{
		for(String key : map.keySet())
			stationMap.put(key, map.get(key));
		ARSFToolkit.sendEvent(EventServiceTopic.REAL_MEASURE_STATION_TOPIC, stationMap);
		}catch(NullPointerException e){
			Log.debug("NullPointerException 空指针   SyntInitOtherService");
		}
	}
	
	
	
}
