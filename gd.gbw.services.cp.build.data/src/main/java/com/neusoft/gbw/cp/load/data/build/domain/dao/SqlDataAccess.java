package com.neusoft.gbw.cp.load.data.build.domain.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

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

public class SqlDataAccess {

	private SqlSessionFactory sqlSessionFactory;
	
	public SqlDataAccess(SqlSessionFactory sqlSessionFactory) {
		setSqlSessionFactory(sqlSessionFactory);
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	//编写针对CPMapper接口对应方法
	//加载收测任务表
	public List<TaskTable> selectMeasureTaskList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectMeasureTaskList();
		} finally {
			sqlSession.close();
		}
	}
	
	//加载任务项表
	public List<TaskFreqTable> selectTaskFreqList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectTaskFreqList();
		} finally {
			sqlSession.close();
		}
	}
	
	//加载任务配置表
	public List<TaskConfTable> selectTaskConfList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectTaskConfList();
		} finally {
			sqlSession.close();
		}
	}
	
	//加载任务的周期时间配置表
	public List<TaskScheduleTable> selectTaskScheduleList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectTaskScheduleList();
		} finally {
			sqlSession.close();
		}
	}
	
	public List<MonitorInfo> selectMonitorInfoList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectMonitorInfoList();
		} finally {
			sqlSession.close();
		}
	}
	
	//加载任务下发站点表
	public List<TaskMonitorTable> selectTaskMonitorList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectTaskMonitorList();
		} finally {
			sqlSession.close();
		}
	}
	
	//加载收测任务的时间分隔表
	public List<TaskTimeIntervalTable> selectTimeIntervalList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectTimeIntervalList();
		} finally {
			sqlSession.close();
		}
	}
	
	//加载设备包含的接收机
	public List<MonMachineTable> selectMonMachineList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectMonMachineList();
		} finally {
			sqlSession.close();
		}
	}
	//加载站点和采集关系
	public List<Map<String, Object>> selectMonPartFormList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectMonPartFormList();
		} finally {
			sqlSession.close();
		}
	}
	
	public List<QualityTypeTable> selectQualityTypeList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectQualityTypeList();
		} finally {
			sqlSession.close();
		}
	}
	
	public List<FtpServerInfo> selectFtpServerInfoList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectFtpServerInfoList();
		} finally {
			sqlSession.close();
		}
	}
	
	public List<Map<String, String>> selectRecordStoreAddrList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectRecordStoreAddrList();
		} finally {
			sqlSession.close();
		}
	}
	
	public List<Map<String, String>> selectStreamList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectStreamList();
		} finally {
			sqlSession.close();
		}
	}
	
	public List<Map<String, Object>> selectMeasureStationList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectMeasureStationList();
		} finally {
			sqlSession.close();
		}
	}
	
	public List<Map<String, Object>> selectRealMeasureSiteList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectRealMeasureSiteList();
		} finally {
			sqlSession.close();
		}
	}
	
	public List<MeasureUnitTable> selectMeasureAutoUnitList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectMeasureAutoUnitList();
		} finally {
			sqlSession.close();
		}
	}
	
	public List<MeasureUnitTable> selectMeasureManualUnitList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectMeasureManualUnitList();
		} finally {
			sqlSession.close();
		}
	}
	
	public List<Map<String, Object>> selectRecoverTimeList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectRecoverTimeList();
		} finally {
			sqlSession.close();
		}
	}
	
	public List<Map<String, Object>> selectRecoverDateList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectRecoverDateList();
		}catch(Exception e){
			System.out.println(e);
			return null;
		}finally {
			sqlSession.close();
		}
	}
	
	public List<Map<String, Object>> selectMonitorInspectVadioList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectMonitorInspectVadioList();
		}catch(Exception e){
			System.out.println(e);
			return null;
		}finally {
			sqlSession.close();
		}
	}
	
	public List<RunplanView> selectRunplanViewList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectRunplanViewList();
		} finally {
			sqlSession.close();
		}
	}
	
	//查询语言的权值
	public List<Map<String, Object>> selectLanguageList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectLanguageList();
		} finally {
			sqlSession.close();
		}
	}
	
	//查询发射台的权值
	public List<Map<String, Object>> selectStationList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectStationList();
		} finally {
			sqlSession.close();
		}
	}
	
	//查询告警类型
	public List<Map<String, Object>> selectEquAlarmTypeList() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectEquAlarmTypeList();
		} finally {
			sqlSession.close();
		}
	}
	
	//查询告警类型
	public List<EquAlarm> selectEquAlarmList(Map<String, Integer> param) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectEquAlarmList(param);
		} finally {
			sqlSession.close();
		}
	}
	
	public void deleteManualUnitRecords(List<Map<String, Object>> list) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
//			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
//			mapper.deleteManualUnitRecords(list);;
			sqlSession.delete("deleteManualUnitRecords", list);
			sqlSession.commit();
		} finally {
			sqlSession.close();
		}
	}
	
	public void deleteMeasureUnitRecords(List<Map<String, Object>> list) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
//			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
//			mapper.deleteMeasureUnitRecords(list);;
			sqlSession.delete("deleteMeasureUnitRecords", list);
			sqlSession.commit();
		} finally {
			sqlSession.close();
		}
	}
}
