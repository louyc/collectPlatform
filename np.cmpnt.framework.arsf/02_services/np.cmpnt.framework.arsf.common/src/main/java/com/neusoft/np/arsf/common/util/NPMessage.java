package com.neusoft.np.arsf.common.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.stream.XMLStreamException;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App Layer 采集处理平台<br>
 * 功能描述: NP消息格式：与Service层交互<br>
 * 创建日期: 2013年8月27日 <br>
 * 版权信息: Copyright (c) 2013<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2013年8月27日       黄守凯        创建
 * </pre>
 */
public class NPMessage {

	private String syncType;

	private String serial;

	private Map<String, String> data;

	// -------------------- 构造体 -------------------- 

	public NPMessage() {
		this.data = new HashMap<String, String>();
	}

	public NPMessage(String syncType, String serial) {
		this.serial = serial;
		this.syncType = syncType;
		this.data = new HashMap<String, String>();
	}

	public NPMessage(String syncType) {
		this.serial = getSerialCode();
		this.syncType = syncType;
		this.data = new HashMap<String, String>();
	}

	public NPMessage(Map<String, String> data) {
		this.data = data;
	}

	// -------------------- 操作方法 -------------------- 

	/**
	 * 针对消息数据进行填充，不包括：syncType与serial
	 */
	public String put(String key, String value) {
		return this.data.put(key, value);
	}

	/**
	 * 针对消息数据进行获取，不包括：syncType与serial
	 */
	public String get(String key) {
		return data.get(key);
	}

	/**
	 * 针对消息数据进行遍历
	 */
	public Iterator<Entry<String, String>> iterator() {
		return data.entrySet().iterator();
	}

	public void putAllObject(Map<String, Object> properties) {
		Iterator<Entry<String, Object>> propertiesIter = properties.entrySet().iterator();
		while (propertiesIter.hasNext()) {
			Entry<String, Object> entry = propertiesIter.next();
			data.put(entry.getKey(), String.valueOf(entry.getValue()));
		}
	}

	public void putAll(Map<String, String> properties) {
		data.putAll(properties);
	}

	public String getSyncType() {
		return syncType;
	}

	public void setSyncType(String syncType) {
		this.syncType = syncType;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "NPMessage [syncType=" + syncType + ", serial=" + serial + ", data=" + data + "]";
	}

	public String toXml() {
		String strXml = "";
		try {
			strXml = NPXmlStream.marshal(this);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return strXml;
	}

	private static AtomicInteger ser = new AtomicInteger(0);

	private static String className = NPMessage.class.getName();

	private static String getSerialCode() {
		int count = ser.getAndIncrement();
		String ser = className + System.currentTimeMillis() + "_" + count;
		return String.valueOf(ser.hashCode());
	}

}
