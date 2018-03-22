package com.neusoft.gbw.cp.process.measure.channel.auto;

import java.util.HashMap;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.measure.vo.auto.AutoMesureUnitStore;
import com.neusoft.gbw.cp.process.measure.vo.auto.ManualMeasureUnitStore;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;

public class RecordDataConver {

	//1：待评估状态；   0：完成评估状态。   -1: 评估失败    2 : 评估中    3：生成收测单元    4： 收测单元完成
	/**
	 * 2次 补采   评估失败 -1
	 * @param data
	 * @param reason
	 * @return
	 */
	public static StoreInfo converLeakageReasonStore(CollectData data, String reason) {
		int mode = data.getCollectTask().getBusTask().getTask_build_mode();
		StoreInfo info = null;
		switch(mode) {
		case 0:
			info = converLeakageReasonAutoStore(data, reason);
			break;
		case 1:
			info = converLeakageReasonManualStore(data, reason);
			break;
		case 2:  //lyc  遥控站巡检
			reason = "录音文件生成失败";
			info = converAutoInpsectStore(data, reason);
			updateMonitorState(data,2,ProcessConstants.StoreTopic.UPDATE_MONITOR_FAIL_STATE);
			updateInspectResult(data, "","1");
			break;
		}

		return info;
	}
	
	/**
	 * 更新成 -1  评估失败
	 * @param data
	 * @return
	 */
	public static StoreInfo converSafeAccessStore(CollectData data) {
		int mode = data.getCollectTask().getBusTask().getTask_build_mode();
		StoreInfo info = null;
		switch(mode) {
		case 0:
			info = converSafeAccessAutoStore(data);
			break;
		case 1:
			info = converSafeAccessManualStore(data);
			break;
		case 2: //lyc 遥控站巡检
//			info = converInspectAutoStore(data);
			break;
		}

		return info;
	}

	private static StoreInfo converLeakageReasonAutoStore(CollectData data, String reason) {
		StoreInfo info = new StoreInfo();
		AutoMesureUnitStore munit = new AutoMesureUnitStore();
		munit.setTask_id(data.getCollectTask().getBusTask().getTask_id());
		munit.setMonitor_id(data.getCollectTask().getBusTask().getOrgMonitorId());
		munit.setRunplan_id(data.getCollectTask().getBusTask().getRunplan_id());
		munit.setRunplan_type_id(data.getCollectTask().getBusTask().getTask_origin_id());
		munit.setUncollect_reason(reason);
		munit.setUnit_collect_time(data.getCollectTime());
		munit.setUnit_status_id(-1);
		munit.setUnit_begin(data.getCollectTask().getBusTask().getUnit_begin());
		munit.setUnit_end(data.getCollectTask().getBusTask().getUnit_end());
		munit.setOrgMonitorId(data.getCollectTask().getBusTask().getOrgMonitorId());

		HashMap<String, Object> map;
		try {
			map = (HashMap<String, Object>) NMBeanUtils.getObjectField(munit);
			info.setDataMap(map);
			info.setLabel(ProcessConstants.StoreTopic.UPDATE_MEASURE_AUTO_LEAKAGE_REASON_STATUS);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
		}

		return info;
	}

	private static StoreInfo converLeakageReasonManualStore(CollectData data, String reason) {
		StoreInfo info = new StoreInfo();
		ManualMeasureUnitStore munit = new ManualMeasureUnitStore();
		munit.setTask_id(data.getCollectTask().getBusTask().getTask_id());
		munit.setMonitor_id(data.getCollectTask().getBusTask().getOrgMonitorId());
		munit.setRunplan_id(data.getCollectTask().getBusTask().getRunplan_id());
		munit.setRunplan_type_id(data.getCollectTask().getBusTask().getTask_origin_id());
		munit.setUnit_collect_time(data.getCollectTime());
		munit.setUncollect_reason(reason);
		munit.setUnit_status_id(-1);
		munit.setUnit_begin(data.getCollectTask().getBusTask().getUnit_begin());
		munit.setUnit_end(data.getCollectTask().getBusTask().getUnit_end());
		munit.setOrgMonitorId(data.getCollectTask().getBusTask().getOrgMonitorId());

		HashMap<String, Object> map;
		try {
			map = (HashMap<String, Object>) NMBeanUtils.getObjectField(munit);
			info.setDataMap(map);
			info.setLabel(ProcessConstants.StoreTopic.UPDATE_MEASURE_MANUAL_LEAKAGE_REASON_STATUS);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
		}

		return info;
	}
	private static StoreInfo converAutoInpsectStore(CollectData data, String reason) {
		StoreInfo info = new StoreInfo();
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("status_id",-1);
		map.put("monitor_id", data.getCollectTask().getBusTask().getMonitor_id());
		map.put("reason", reason);
		map.put("radio_url"," ");
		map.put("eval_url"," ");
		try {
			info.setDataMap(map);
			info.setLabel(ProcessConstants.StoreTopic.UPDATE_AUTO_STREAM_REASON_STATUS);
		} catch (Exception e) {
			Log.error("", e);
		}
		return info;
	}

	private static StoreInfo converSafeAccessAutoStore(CollectData data) {
		StoreInfo info = new StoreInfo();
		AutoMesureUnitStore munit = new AutoMesureUnitStore();
		munit.setTask_id(data.getCollectTask().getBusTask().getTask_id());
		munit.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
		munit.setRunplan_id(data.getCollectTask().getBusTask().getRunplan_id());
		munit.setRunplan_type_id(data.getCollectTask().getBusTask().getTask_origin_id());
		munit.setUnit_status_id(-1);
		munit.setUnit_collect_time(data.getCollectTime());
		munit.setOrgMonitorId(data.getCollectTask().getBusTask().getOrgMonitorId());

		HashMap<String, Object> map;
		try {
			map = (HashMap<String, Object>) NMBeanUtils.getObjectField(munit);
			info.setDataMap(map);
			info.setLabel(ProcessConstants.StoreTopic.UPDATE_MEASURE_AUTO_SAFE_ASSESS_STATUS);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
		}

		return info;
	}

	private static StoreInfo converSafeAccessManualStore(CollectData data) {
		StoreInfo info = new StoreInfo();
		ManualMeasureUnitStore munit = new ManualMeasureUnitStore();
		munit.setTask_id(data.getCollectTask().getBusTask().getTask_id());
		munit.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
		munit.setRunplan_id(data.getCollectTask().getBusTask().getRunplan_id());
		munit.setRunplan_type_id(data.getCollectTask().getBusTask().getTask_origin_id());
		munit.setUnit_status_id(-1);
		munit.setUnit_collect_time(data.getCollectTime());
		munit.setOrgMonitorId(data.getCollectTask().getBusTask().getOrgMonitorId());

		HashMap<String, Object> map;
		try {
			map = (HashMap<String, Object>) NMBeanUtils.getObjectField(munit);
			info.setDataMap(map);
			info.setLabel(ProcessConstants.StoreTopic.UPDATE_MEASURE_MANUAL_SAFE_ASSESS_STATUS);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
		}

		return info;
	}
	private static StoreInfo converInspectAutoStore(CollectData data) {
		StoreInfo info = new StoreInfo();
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("unit_status_id",4);
		map.put("monitor_id", data.getCollectTask().getBusTask().getMonitor_id());
		try {
			info.setDataMap(map);
			info.setLabel(ProcessConstants.StoreTopic.UPDATE_AUTO_STREAM_STATUS);
		} catch (Exception e) {
			Log.error("", e);
		}
		
		return info;
	}
	/**
	 * 
	 * 更新成待评估    1
	 * @param data
	 * @param radio_url
	 * @param eval_url
	 * @param collect_desc
	 * @return
	 */
	public static StoreInfo converStore(CollectData data,String radio_url, String eval_url, String collect_desc) {
		int mode = data.getCollectTask().getBusTask().getTask_build_mode();
		StoreInfo info = null;
		switch(mode) {
		case 0:
			info = converAutoStore(data, radio_url, eval_url, collect_desc);
			break;
		case 1:
			info = converManualStore(data, radio_url,eval_url, collect_desc);
			break;
		case 2:
			if(null !=eval_url){
				collect_desc ="录音文件生成成功";
				info = converAutoSteamStore(data, radio_url,eval_url, collect_desc,1);
				updateMonitorState(data,1,ProcessConstants.StoreTopic.UPDATE_MONITOR_SUC_STATE);
				updateInspectResult(data, radio_url,"0");
			}else{
				collect_desc ="录音文件生成失败";
				updateInspectResult(data, radio_url,"1");
				updateMonitorState(data,2,ProcessConstants.StoreTopic.UPDATE_MONITOR_FAIL_STATE);
				info = converAutoSteamStore(data, radio_url,eval_url, collect_desc,-1);
			}
			break;
		}

		return info;
	}

	private static StoreInfo converManualStore(CollectData data,String radio_url, String eval_url, String collect_desc) {
		StoreInfo info = new StoreInfo();
		ManualMeasureUnitStore munit = new ManualMeasureUnitStore();
		munit.setTask_id(data.getCollectTask().getBusTask().getTask_id());
		munit.setRadio_url(radio_url==null?"null":radio_url);
		munit.setEval_url(eval_url==null?"null":eval_url);
		munit.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
		munit.setUnit_collect_time(data.getCollectTime());
		munit.setRunplan_id(data.getCollectTask().getBusTask().getRunplan_id());
		munit.setRunplan_type_id(data.getCollectTask().getBusTask().getTask_origin_id());
		munit.setFreq(data.getCollectTask().getBusTask().getFreq());
		munit.setUncollect_reason(collect_desc);
		munit.setUnit_begin(data.getCollectTask().getBusTask().getUnit_begin());
		munit.setUnit_end(data.getCollectTask().getBusTask().getUnit_end());
		//采集数据后状态为1,表示待评估
		//效果类任务   更新站点  接收机信息
		munit.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
		munit.setOrgMonitorId(data.getCollectTask().getBusTask().getOrgMonitorId());
		munit.setReciever_code(data.getCollectTask().getAttrInfo().getFirstEquCode());
		munit.setMonitor_alias(data.getCollectTask().getBusTask().getAlias_name());
		munit.setMonitor_alias_code(data.getCollectTask().getBusTask().getAlias_code());
		munit.setUnit_status_id(1);
		HashMap<String, Object> map;
		try {
			map = (HashMap<String, Object>) NMBeanUtils.getObjectField(munit);
			info.setDataMap(map);
			info.setLabel(ProcessConstants.StoreTopic.UPDATE_MEASURE_MANUAL_UNIT_URL_RECORDS);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
		}

		return info;
	}
	private static StoreInfo converAutoSteamStore(CollectData data,String radio_url, String eval_url, String collect_desc,
			int state) {
		StoreInfo info = new StoreInfo();
		ManualMeasureUnitStore munit = new ManualMeasureUnitStore();
		munit.setTask_id(data.getCollectTask().getBusTask().getTask_id());
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("radio_url",radio_url==null?" ":radio_url);
		map.put("eval_url",eval_url==null?" ":eval_url);
		map.put("reason",collect_desc);
		map.put("status_id",state);
		map.put("monitor_id", data.getCollectTask().getBusTask().getMonitor_id());
		try {
			info.setDataMap(map);
			info.setLabel(ProcessConstants.StoreTopic.UPDATE_AUTO_STREAM_URL_RECORDS);
		} catch (Exception e) {
			Log.error("", e);
		}
		return info;
	}

	private static void updateMonitorState(CollectData data,int state,String sqlTopic){
		StoreInfo info = new StoreInfo();
		ManualMeasureUnitStore munit = new ManualMeasureUnitStore();
		munit.setTask_id(data.getCollectTask().getBusTask().getTask_id());
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("online_state",state);
		map.put("monitor_id", data.getCollectTask().getBusTask().getMonitor_id());
		try {
			info.setDataMap(map);
			info.setLabel(sqlTopic);
		} catch (Exception e) {
			Log.error("", e);
		}
		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
	}
	private static void updateInspectResult(CollectData data,String url,String result){
		StoreInfo info = new StoreInfo();
		ManualMeasureUnitStore munit = new ManualMeasureUnitStore();
		munit.setTask_id(data.getCollectTask().getBusTask().getTask_id());
		HashMap<String, Object> map = new HashMap<String,Object>();
		map.put("inspec_message",url);
		map.put("inspec_result",result);
		map.put("monitor_id", data.getCollectTask().getBusTask().getMonitor_id());
		try {
			info.setDataMap(map);
			info.setLabel(ProcessConstants.StoreTopic.UPDATE_INSPECT_RESULT);
		} catch (Exception e) {
			Log.error("", e);
		}
		ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
	}
	
	private static StoreInfo converAutoStore(CollectData data,String radio_url, String eval_url, String collect_desc) {
		StoreInfo info = new StoreInfo();
		AutoMesureUnitStore munit = new AutoMesureUnitStore();
		munit.setTask_id(data.getCollectTask().getBusTask().getTask_id());
		munit.setRadio_url(radio_url==null?"null":radio_url);
		munit.setEval_url(eval_url==null?"null":eval_url);
		munit.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
		munit.setUnit_collect_time(data.getCollectTime());
		munit.setRunplan_id(data.getCollectTask().getBusTask().getRunplan_id());
		munit.setRunplan_type_id(data.getCollectTask().getBusTask().getTask_origin_id());
		munit.setUncollect_reason(collect_desc);
		munit.setUnit_begin(data.getCollectTask().getBusTask().getUnit_begin());
		munit.setUnit_end(data.getCollectTask().getBusTask().getUnit_end());
		//采集数据后状态为1,表示待评估
		munit.setUnit_status_id(1);
		//效果类任务   更新站点  接收机信息
		munit.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
		munit.setOrgMonitorId(data.getCollectTask().getBusTask().getOrgMonitorId());
		munit.setReceiver_code(data.getCollectTask().getAttrInfo().getFirstEquCode());
		munit.setMonitor_alias(data.getCollectTask().getBusTask().getAlias_name());
		munit.setMonitor_alias_code(data.getCollectTask().getBusTask().getAlias_code());
		//		munit.setUnit_begin(data.getCollectTask().getBusTask().getUnit_begin());
		//		munit.setUnit_end(data.getCollectTask().getBusTask().getUnit_end());
		HashMap<String, Object> map;
		try {
			map = (HashMap<String, Object>) NMBeanUtils.getObjectField(munit);
			info.setDataMap(map);
			info.setLabel(ProcessConstants.StoreTopic.UPDATE_MEASURE_AUTO_UNIT_URL_RECORDS);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
		}

		return info;
	}
}
