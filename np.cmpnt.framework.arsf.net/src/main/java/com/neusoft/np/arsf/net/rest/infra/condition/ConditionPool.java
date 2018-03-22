package com.neusoft.np.arsf.net.rest.infra.condition;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.net.rest.infra.config.NPBundlerVar;

public class ConditionPool {

	private Lock lock = new ReentrantLock();

	private ConcurrentMap<String, Condition> conditionMap;

	public void init() {
		conditionMap = new ConcurrentHashMap<String, Condition>();
	}

	public void wait(String id) throws ConditionPoolException, InterruptedException {
		lock.lock();
		try {
			if (conditionMap.containsKey(id)) {
				throw new ConditionPoolException("已经存在该请求，标识字段重复：" + id);
			}
			Condition condition = lock.newCondition();
			conditionMap.put(id, condition);
			Log.info("标识字段：" + id + "，正在请求数据");
			condition.await(NPBundlerVar.timeout, TimeUnit.MILLISECONDS);
			// condition.await();
			Log.info("标识字段：" + id + "，完成请求数据");
		} finally {
			lock.unlock();
		}
	}

	public void signal(String id) throws ConditionPoolException {
		lock.lock();
		try {
			if (!conditionMap.containsKey(id)) {
				throw new ConditionPoolException("唤醒已经休眠的线程失败，该现场已经被唤醒或者超时：" + id);
			}
			conditionMap.get(id).signal();
		} finally {
			lock.unlock();
		}
	}

	public void stop() {
		// TODO Auto-generated method stub
	}

}
