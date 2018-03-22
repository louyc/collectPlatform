package com.neusoft.gbw.cp.process.alarm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.neusoft.gbw.cp.core.store.EquAlarm;

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

	//查询告警类型
	public List<EquAlarm> selectEquAlarmList(Map<String, Long> param) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			CPMapper mapper = sqlSession.getMapper(CPMapper.class);
			return mapper.selectEquAlarmList(param);
		} finally {
			sqlSession.close();
		}
	}
}
