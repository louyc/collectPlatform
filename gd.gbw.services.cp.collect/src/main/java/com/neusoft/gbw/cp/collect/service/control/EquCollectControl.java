package com.neusoft.gbw.cp.collect.service.control;

import java.util.HashMap;
import java.util.Map;
/**
 * 接收机控制模块
 * @author jh
 *
 */
public class EquCollectControl {

	//key: 接收机编码
	private Map<String, EquCollectCotrolMgr> equMap = null;
	
	public EquCollectControl() {
		equMap = new HashMap<String, EquCollectCotrolMgr>();
	}
	
	protected boolean containEquCode(String key) {
		return equMap.containsKey(key);
	}
	
	protected void addEquCollectCotrol(String key, EquCollectCotrolMgr equCollectMgr) {
		equMap.put(key, equCollectMgr);
	}
	
	protected EquCollectCotrolMgr getEquCollectCotrolMgr(String key) {
		return equMap.get(key);
	}
	
	protected void clear() {
		this.equMap.clear();
	}
}
