package com.neusoft.gbw.cp.process.measure.channel;

import java.util.List;

import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;

public interface MeaUnitChannel extends Channel {
	/**
	 * 创建收测单元
	 * @param list
	 */
	void createMeasureUnit(List<CollectTask> list);
	
//	Timer createMeaUnitTimeOut(String key, int autoType, int orgType, int recordLength);
	/**
	 * 给web发送消息
	 * @param autoType
	 * @param orgType
	 */
	void sendWebMsg(int autoType, int orgType);
	
//	void updateRecordCount(CollectData data);
	/**
	 * 存储数据
	 * @param data
	 * @param radio_url
	 * @param eval_url
	 * @param collect_desc
	 */
	void storeData(CollectData data,String radio_url, String eval_url, String collect_desc);
	/**
	 * 校验监测智能评估打分是否完成
	 * @param data
	 */
	void storeSafeAssessData(CollectData data);
	/**
	 * 存在采集失败原因
	 * @param data
	 * @param reason
	 */
	void storeLeakageReason(CollectData data, String reason);
}
