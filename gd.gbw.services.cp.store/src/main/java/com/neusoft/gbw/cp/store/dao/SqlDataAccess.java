package com.neusoft.gbw.cp.store.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.neusoft.gbw.domain.abnormal.intf.dto.AbnormalEventDTO;
import com.neusoft.np.arsf.base.bundle.Log;

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

	/**
	 * 批量插入方法
	 * @param name 数据的标签（xml中的id）
	 * @param infoList 数据集合
	 */
	public void insertRecords(String name, List<Map<String,Object>> infoList) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			//在  insertQualityAlarm时  插入指标报警时  查询数据库中是否已有数据 
			if("insertQualityAlarm".equals(name) || "insertQualityAlarm1".equals(name)){
				for(Map<String,Object> m: infoList){
					Log.debug("********指标告警接收数据  ：   "+m.entrySet());
					String s=sqlSession.selectOne("beforeinsertQualityAlarm", m);
					if(s==null||"".equals(s)){
						sqlSession.insert(name, m);
						Log.debug("----------------指标报警插入完成--------------");
					}
				}
			}
			else{
				sqlSession.insert(name, infoList);
			}

			//sqlSession.insert(name, infoList);
			sqlSession.commit();
		} catch(Exception e) {
			Log.error("批量插入异常，label=" + name, e);
		} finally {
			Log.info("批量插入# 主题名称：[" + name + "]数据插入完成, 操作个数=" + infoList.size());
			sqlSession.close();
			infoList.clear();
			
			infoList = null;
		}
	}

	/**
	 * 批量更新方法
	 * @param name 数据的标签（xml中的id）
	 * @param infoList 数据集合
	 */
	public void updateRecords(String name, List<Map<String,Object>> infoList) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<Map<String,Object>> tempInfoList = null;
		try {
			if("updateMeasureAutoFailStatus".equals(name)){
				for(Map<String,Object> m: infoList){
					sqlSession.update(name, m);
					sqlSession.commit();
				}
			}else{
				//针对批量更新会出现锁表问题，因此进行单挑提交更新
				for(Map<String,Object> map : infoList) {
					tempInfoList = new ArrayList<Map<String,Object>>();
					tempInfoList.add(map);
					sqlSession.update(name, tempInfoList);
					sqlSession.commit();
					tempInfoList.clear();
					tempInfoList = null;
				}
			}
		}catch(Exception e) {
			Log.error("批量更新异常，label=" + name, e);
		}finally {
			Log.info("批量更新# 主题名称：[" + name + "]数据更新完成, 操作个数=" + infoList.size());
			sqlSession.close();
			infoList.clear();
			infoList = null;
		}
	}

	/**
	 * 批量删除方法
	 * @param name 数据的标签（xml中的id）
	 * @param infoList 数据集合
	 */
	public void deleteRecords(String name, List<Map<String,Object>> infoList) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			sqlSession.delete(name, infoList);
			sqlSession.commit();
		}catch(Exception e) {
			Log.error("批量删除异常，label=" + name, e);
		}finally {
			Log.info("批量删除# 主题名称：[" + name + "]数据删除完成, 操作个数=" + infoList.size());
			sqlSession.close();
			infoList.clear();
			infoList = null;
		}
	}
	//查询指标报警数据是否重复
	/*	public AbnormalEventDTO beforeinsertQualityAlarm(String name, AbnormalEventDTO dto) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		AbnormalEventDTO dtonew=new AbnormalEventDTO();
		try {
			//sqlSession.delete(name, dto);
			Map<String,String> map=new HashMap<String,String>();
			map.put("eventId", dto.getEventId());
			map.put("monitorId", dto.getMonitorId());
			 dtonew=sqlSession.selectOne(name, map);
			sqlSession.commit();
		}catch(Exception e) {
			Log.error("批量删除异常，label=" + name, e);
		}finally {
			//Log.info("批量删除# 主题名称：[" + name + "]数据删除完成, 操作个数=" + infoList.size());
			sqlSession.close();

		}
		return dtonew;
	}*/
}
