package com.neusoft.gbw.cp.store.service;

import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.store.dao.SqlDataAccess;
import com.neusoft.gbw.cp.store.dao.SqlSessionFactoryHolder;
import com.neusoft.gbw.cp.store.vo.StoreOperType;
import com.neusoft.np.arsf.base.bundle.Log;

public class StoreDBExecutor {
	
	private SqlDataAccess sqlAccess = null;

	public StoreDBExecutor() {
	
		try {
			SqlSessionFactoryHolder.getInstance().init();
			sqlAccess = new SqlDataAccess(SqlSessionFactoryHolder.getInstance().getSqlSessionFactory());
		} catch (Exception e) {
			Log.error("SqlSessionFactoryHolder初始化错误", e);
		}
		
	}
	
	/**
	 * 存储方法
	 * @param label 标签
	 * @param type  存储类型
	 * @param infoList 存储数据集合
	 */
	public void store(String label, StoreOperType type,  List<Map<String,Object>> infoList) {
		switch(type) {
		case INSERT:
			sqlAccess.insertRecords(label, infoList);
			break;
		case UPDATE:
			sqlAccess.updateRecords(label, infoList);
			break;
		case DELETE:
			sqlAccess.deleteRecords(label, infoList);
			break;
		}
	}
}
