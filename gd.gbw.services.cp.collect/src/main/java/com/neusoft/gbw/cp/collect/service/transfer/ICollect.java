package com.neusoft.gbw.cp.collect.service.transfer;

import com.neusoft.gbw.cp.core.collect.CollectAttrInfo;

public interface ICollect {
	/**
	 * 校验通讯信息 成功 true 失败 false
	 * @param attrInfo
	 * @return
	 */
	boolean checkInfo(CollectAttrInfo attrInfo);
	/**
	 * 发送采集信息  成功 true 失败 false
	 * @param attrInfo
	 * @param xml
	 * @return
	 */
	boolean collect(CollectAttrInfo attrInfo, String xml);
}
