package com.neusoft.gbw.cp.load.data.build.domain.dao;

import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.core.store.EquAlarm;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.FtpServerInfo;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.MeasureUnitTable;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.MonMachineTable;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.MonitorInfo;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.QualityTypeTable;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.RunplanView;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.TaskConfTable;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.TaskFreqTable;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.TaskMonitorTable;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.TaskScheduleTable;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.TaskTable;
import com.neusoft.gbw.cp.load.data.build.domain.dao.bean.TaskTimeIntervalTable;

public interface CPMapper {

	//加载收测任务表
	List<TaskTable> selectMeasureTaskList();
	//加载任务项表
	List<TaskFreqTable> selectTaskFreqList();
	//加载任务配置表
	List<TaskConfTable> selectTaskConfList();
	//加载任务的周期时间配置表
	List<TaskScheduleTable> selectTaskScheduleList();
	//加载收测设备表
	List<MonitorInfo> selectMonitorInfoList();
	//加载任务下发站点表
	List<TaskMonitorTable> selectTaskMonitorList();
	//加载收测任务的时间分隔表
	List<TaskTimeIntervalTable> selectTimeIntervalList();
	//加载设备包含的接收机
	List<MonMachineTable> selectMonMachineList();
	//加载存储采集点手动任务的数据对应的表名称
	List<QualityTypeTable> selectQualityTypeList();
	//加载Ftp服务器的用户名密码
	List<FtpServerInfo> selectFtpServerInfoList();
	//加载switch表信息(录音文件路径)
	List<Map<String, String>> selectRecordStoreAddrList();
	//加载巡检遥控站任务
	List<Map<String, String>> selectStreamList();
	//加载运行图对应台名信息
	List<Map<String, Object>> selectMeasureStationList();
	//加载当前收测单元信息(用于系统崩溃重启后去除重复生成收测单元)
	List<MeasureUnitTable> selectMeasureAutoUnitList();
	//加载当前收测单元信息(用于系统崩溃重启后去除重复生成收测单元)
	List<MeasureUnitTable> selectMeasureManualUnitList();
	//删除未完成的自动收测单元
	void deleteMeasureUnitRecords(List<Map<String, Object>> list);
	//删除未完成的手动收测单元
	void deleteManualUnitRecords(List<Map<String, Object>> list);
	//加载实时收测站点信息
	List<Map<String, Object>> selectRealMeasureSiteList();
	//加载站点和采集关系信息
	List<Map<String, Object>> selectMonPartFormList();
	//加载任务回收时间戳数据
	List<Map<String, Object>> selectRecoverTimeList();
	//加载任务回收数据
	List<Map<String, Object>> selectRecoverDateList();
	//加载遥控站录音巡检数据
	List<Map<String, Object>> selectMonitorInspectVadioList();
	//查询运行图与语言和发射台
	List<RunplanView> selectRunplanViewList();
	//查询语言的权值
	List<Map<String, Object>> selectLanguageList();
	//查询发射台的权值
	List<Map<String, Object>> selectStationList();
	//查询告警类型
	List<Map<String, Object>> selectEquAlarmTypeList();
	//查询告警数据
	List<EquAlarm> selectEquAlarmList(Map<String, Integer> param);
}

