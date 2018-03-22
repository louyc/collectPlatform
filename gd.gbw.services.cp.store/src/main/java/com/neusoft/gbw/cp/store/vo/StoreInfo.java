package com.neusoft.gbw.cp.store.vo;

import java.util.Map;

public class StoreInfo {

	private String label;
	private StoreOperType operType;
	private Map<String, Object> dataMap = null;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
		if (label.startsWith("insert")) {
			this.operType = StoreOperType.INSERT;
		}else if (label.startsWith("update")) {
			this.operType = StoreOperType.UPDATE;
		}else if(label.startsWith("delete")) {
			this.operType = StoreOperType.DELETE;
		}
	}
	public StoreOperType getOperType() {
		return operType;
	}
	public Map<String, Object> getDataMap() {
		return dataMap;
	}
	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}
}
