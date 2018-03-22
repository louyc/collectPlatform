package com.neusoft.gbw.cp.process.measure.channel.auto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.measure.context.CollectTaskModel;
import com.neusoft.gbw.cp.process.measure.vo.auto.AutoMesureUnitStore;
import com.neusoft.gbw.cp.process.measure.vo.auto.ManualMeasureUnitStore;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;

public class MesureUnitProcessor {

	private static final String SPLIT = "_";
	/**
	 * 修改存储自动收测单元
	 * @param list
	 * @return
	 */
	public boolean updateAndStoreMesureAutoUnit(List<CollectTask> list) {
		if (!list.isEmpty()) {
			//获取实时收测单元站点Id
			Map<String, String> siteMap = CollectTaskModel.getInstance().getRealSiteMap();
			//			if(monitorIds != null && monitorIds.length() > 0)
			updateMesureAutoUnitStore(ProcessConstants.StoreTopic.UPDATE_MEASURE_AUTO_UNIT_STATUS,list,siteMap);
			//			updateMesureScoreStore(ProcessConstants.StoreTopic.UPDATE_MEASURE_AUTO_SCORE_STATUS);
			for (CollectTask collectTask : list) {
				insertMesureAutoUnitStore(collectTask);
			}
			sleep();
		}
		return true;
	}

	public boolean updateAndStoreMesureManualUnit(List<CollectTask> list) {
		if (!list.isEmpty()) {
			updateMesureManualUnitStore(ProcessConstants.StoreTopic.UPDATE_MEASURE_MANUAL_UNIT_STATUS, list);
			sleep();
			for (CollectTask collectTask : list) {
				insertMesureManualUnitStore(collectTask);
			}
		}
		return true;
	}

	private void updateMesureAutoUnitStore(String label, List<CollectTask> list,Map<String, String> siteMap) {
		for(String key : siteMap.keySet()) {
			if(siteMap.get(key).equals("2"))//2为实时采集
				continue;

			String[] args = key.split(SPLIT); //key为monitor_id,runplan_id,monitor_status
			StoreInfo info = new StoreInfo();
			HashMap<String, Object> map = new HashMap<String, Object>();
			//将以前的收测单元更改为最终状态4，收测单元过期状态
			map.put("unit_status_id", 4);
			map.put("unit_begin", list.get(0).getBusTask().getUnit_begin());
			map.put("unit_end", list.get(0).getBusTask().getUnit_end());
			map.put("unit_interval", list.get(0).getBusTask().getMeasure_unit_time());
			map.put("monitor_id", Long.parseLong(args[0]));
			map.put("runplan_type_id", Integer.parseInt(args[1]));
			info.setDataMap(map);
			info.setLabel(label);
			ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
		}
	}

	private void updateMesureManualUnitStore(String label, List<CollectTask> list) {
		StoreInfo info = new StoreInfo();
		HashMap<String, Object> map = new HashMap<String, Object>();
		//将以前的收测单元更改为最终状态4，收测单元过期状态
		map.put("unit_status_id", 4);
		map.put("unit_begin", list.get(0).getBusTask().getUnit_begin());
		map.put("unit_interval", list.get(0).getBusTask().getMeasure_unit_time());
		info.setDataMap(map);
		info.setLabel(label);
		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
	}

	//	private void updateMesureScoreStore(String label) {
	//		StoreInfo info = new StoreInfo();
	//		HashMap<String, Object> map = new HashMap<String, Object>();
	//		map.put("is_manual_interv", 3);
	//		info.setDataMap(map);
	//		info.setLabel(label);
	//		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
	//	}

	private void insertMesureManualUnitStore(CollectTask collectTask) {
		ManualMeasureUnitStore mus = new ManualMeasureUnitStore();
		mus.setMonitor_id(collectTask.getBusTask().getMonitor_id());
		mus.setReciever_code(collectTask.getAttrInfo().getFirstEquCode());
		mus.setUnit_create_time(((Query) collectTask.getData().getQuery()).getDateTime());
		mus.setRunplan_id(collectTask.getBusTask().getRunplan_id());
		mus.setTask_id(collectTask.getBusTask().getTask_id());
		mus.setRunplan_type_id(collectTask.getBusTask().getTask_origin_id());
		mus.setUnit_begin(collectTask.getBusTask().getUnit_begin());
		mus.setUnit_end(collectTask.getBusTask().getUnit_end());
		mus.setUnit_interval(collectTask.getBusTask().getMeasure_unit_time());
		mus.setUnit_marking_type(collectTask.getBusTask().getUnit_marking_type());
		//状态为3表示收测单元的生成
		mus.setUnit_status_id(3);
		mus.setFreq(collectTask.getBusTask().getFreq());
		mus.setMonitor_alias(collectTask.getBusTask().getAlias_name());
		mus.setMonitor_alias_code(collectTask.getBusTask().getAlias_code());
		StoreInfo info = new StoreInfo();
		try {
			Map<String, Object> map = NMBeanUtils.getObjectField(mus);
			info.setDataMap(map);
			info.setLabel(ProcessConstants.StoreTopic.INSERT_MEASURE_MANUAL_UNIT_STORE);
			ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
		}
	}

	private void insertMesureAutoUnitStore(CollectTask collectTask) {
		AutoMesureUnitStore mus = new AutoMesureUnitStore();
		mus.setMonitor_id(collectTask.getBusTask().getMonitor_id());
		mus.setReceiver_code(collectTask.getAttrInfo().getFirstEquCode());
		mus.setUnit_create_time(((Query) collectTask.getData().getQuery()).getDateTime());
		mus.setRunplan_id(collectTask.getBusTask().getRunplan_id());
		mus.setTask_id(collectTask.getBusTask().getTask_id());
		mus.setRunplan_type_id(collectTask.getBusTask().getTask_origin_id());
		mus.setUnit_begin(collectTask.getBusTask().getUnit_begin());
		mus.setUnit_end(collectTask.getBusTask().getUnit_end());
		mus.setUnit_interval(collectTask.getBusTask().getMeasure_unit_time());
		//状态为3表示收测单元的生成
		mus.setUnit_status_id(3);
		mus.setUnit_marking_type(collectTask.getBusTask().getUnit_marking_type());
		mus.setBand(collectTask.getBusTask().getRunplan().getBand());
		mus.setDirection(collectTask.getBusTask().getRunplan().getDirection());
		mus.setEnd_time(collectTask.getBusTask().getRunplan().getEnd_time());
		mus.setFreq(collectTask.getBusTask().getRunplan().getFreq());
		mus.setLanguage_id(collectTask.getBusTask().getRunplan().getLanguage_id());
		mus.setProgram_id(collectTask.getBusTask().getRunplan().getProgram_id());
		mus.setRadio_type_id(collectTask.getBusTask().getRunplan().getRadio_type_id());
		mus.setStart_time(collectTask.getBusTask().getRunplan().getStart_time());
		mus.setStation_id(collectTask.getBusTask().getRunplan().getStation_id());
		mus.setTime_type_id(collectTask.getBusTask().getRunplan().getTime_type_id());
		mus.setTransmitter_no(collectTask.getBusTask().getRunplan().getTransmitter_no());
		mus.setBroadcast_type_id(collectTask.getBusTask().getRunplan().getBroadcast_type_id());
		mus.setTask_type_id(collectTask.getBusTask().getRunplan().getTask_type_id());
		mus.setProgram_type_id(collectTask.getBusTask().getRunplan().getProgram_type_id());
		mus.setCenter_id(collectTask.getBusTask().getCenter_id());
		mus.setMonitor_alias(collectTask.getBusTask().getAlias_name());
		mus.setMonitor_alias_code(collectTask.getBusTask().getAlias_code());
		mus.setService_area(collectTask.getBusTask().getRunplan().getService_area());
		StoreInfo info = new StoreInfo();
		try {
			Map<String, Object> map = NMBeanUtils.getObjectField(mus);
			info.setDataMap(map);
			info.setLabel(ProcessConstants.StoreTopic.INSERT_MEASURE_AUTO_UNIT_STORE);
			ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
//			insertUnit("insertMeasureAutoUnitStore",map);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
		}
	}
//	private SqlDataAccess sqlAccess = null;
//	public void insertUnit(String name,Map<String,Object> m){
//		try {
//			SqlSessionFactoryHolder.getInstance().init();
//			sqlAccess = new SqlDataAccess(SqlSessionFactoryHolder.getInstance().getSqlSessionFactory());
//			sqlAccess.insertRecords(name,m);
//		} catch (Exception e) {
//			Log.error("SqlSessionFactoryHolder初始化错误", e);
//		}
//	}
	private void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
