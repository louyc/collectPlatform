package com.neusoft.gbw.cp.process.alarm.dao;

import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.core.store.EquAlarm;

public interface CPMapper {
	//查询告警数据
	List<EquAlarm> selectEquAlarmList(Map<String, Long> param);
}

