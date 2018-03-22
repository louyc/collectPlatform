package com.neusoft.np.arsf.core.schedule.quartz;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * 项目名称: 采集平台框架<br>
 * 模块名称: 采集平台框架任务管理服务<br>
 * 功能描述: 周期调度处理机制。图示参数采集平台框架概要设计。
 * 	功能：根据当前的周期参数，获取所以需要触发的周期任务，返回Map<String, Set<String>>，
 * 	示例：<A:<A1、A2>，B:<B1、B2>><br>
 * 创建日期: 2012-6-13 <br>
 * 版权信息: Copyright (c) 2012<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2012-6-13       黄守凯        创建
 * </pre>
 */
public class PeriodCentre {

	private static class PeriodCentreHolder {
		private static final PeriodCentre INSTANCE = new PeriodCentre();
	}

	private PeriodCentre() {
	}

	private Lock lock = new ReentrantLock();

	protected static PeriodCentre getInstance() {
		return PeriodCentreHolder.INSTANCE;
	}

	private ConcurrentMap<Integer, Set<String>> periodMap = new ConcurrentHashMap<Integer, Set<String>>();

	/**
	 * 添加一条新的周期任务
	 * 
	 * @param key1 similarTarget
	 * @param period 周期
	 */
	protected void addPreriodInfo(String key1, int period) {
		lock.lock();
		try {
			if (!periodMap.containsKey(period)) {
				periodMap.put(period, new HashSet<String>());
			}
			periodMap.get(period).add(key1);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 删除一条周期任务。
	 * 
	 * @param key1 similarTarget
	 * @param key2 businessUnitID
	 * @param period 周期
	 */
	protected void delPeriodInfo(String key1, int period) {
		lock.lock();
		try {
			if (!periodMap.containsKey(period)) {
				return;
			}
			Set<String> secondaryKeySet = periodMap.get(period);
			if (secondaryKeySet != null) {
				secondaryKeySet.remove(key1);
				if (secondaryKeySet.isEmpty()) {
					periodMap.remove(period);
				}
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 获取满足当前周期参数触发条件的所有周期任务。
	 * 
	 * @param lastPeriod 周期参数
	 * @return 示例：<A:<A1、A2>，B:<B1、B2>>
	 */
	protected Set<String> getTriggerKeysByPeriod(long lastPeriod) {
		lock.lock();
		Set<String> keys = new HashSet<String>();
		try {
			Iterator<Entry<Integer, Set<String>>> periodMapIter = periodMap.entrySet().iterator();
			while (periodMapIter.hasNext()) {
				Entry<Integer, Set<String>> entry = periodMapIter.next();
				int period = entry.getKey();
				if (!checkPeriod(period, lastPeriod)) {
					continue;
				}
				keys.addAll(entry.getValue());
			}
			return keys;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 周期参数触发条件。目前为周期参数与周期的摸为0
	 * 
	 * @param period 周期
	 * @param lastPeriod 周期参数（系统持续时间）
	 * @return 是否触发
	 */
	protected static boolean checkPeriod(int period, long lastPeriod) {
		if (period == 0) {
			return false;
		}
		return lastPeriod % period == 0;
	}
}
