package com.neusoft.gbw.cp.store.dao;

import java.util.HashMap;
import java.util.List;


public interface BatchMapper {

	void insertRecords(List<HashMap<String, String>> list);
	
	void updateRecords(List<HashMap<String, String>> list);
	
	void deleteRecords(List<HashMap<String, String>> list);
}