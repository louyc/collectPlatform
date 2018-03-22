package com.neusoft.gbw.cp.process.measure.channel.online;

import java.util.HashMap;

import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.measure.vo.online.ManualOnlineUnitStore;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;

public class OnlineRecordDataConver {
	
	public static StoreInfo converStore(CollectData data,String radio_url, String eval_url) {
		StoreInfo info = null;
		info = converOnlineStore(data, radio_url, eval_url);
		return info;
	}

	
	public static StoreInfo converOnlineStore(CollectData data,String radio_url, String eval_url) {
		StoreInfo info = new StoreInfo();
		ManualOnlineUnitStore online = new ManualOnlineUnitStore();
		online.setId(data.getCollectTask().getBusTask().getTask_id());
		online.setRadio_url(radio_url==null?"":radio_url);
		online.setEval_url(eval_url==null?"":eval_url);
		online.setMonitor_id(data.getCollectTask().getBusTask().getMonitor_id());
		online.setCollect_time(data.getCollectTime());
		HashMap<String, Object> map;
		try {
			map = (HashMap<String, Object>) NMBeanUtils.getObjectField(online);
			info.setDataMap(map);
			info.setLabel(ProcessConstants.StoreTopic.UPDATE_MEASURE_ONLINE_UNIT_URL_RECORDS);
		} catch (NMBeanUtilsException e) {
			Log.error("", e);
		}
		
		return info;
	}
}
