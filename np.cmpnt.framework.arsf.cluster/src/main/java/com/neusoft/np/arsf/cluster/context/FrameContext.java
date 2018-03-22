package com.neusoft.np.arsf.cluster.context;

import java.sql.Connection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.neusoft.np.arsf.cluster.vo.PlatformStatus;

public class FrameContext {

	private static class Holder {
		private static final FrameContext INSTANCE = new FrameContext();
	}

	private FrameContext() {
		session = new SqlConnectionProcess();
		session.init();
	}

	public static FrameContext getInstance() {
		return Holder.INSTANCE;
	}
	
	private Lock lock = new ReentrantLock();
	private SqlConnectionProcess session = null;
	private PlatformStatus pStatus = PlatformStatus.ACTIVE;
	
	public Connection getConnection() {
		return session.getConnect();
	}
	
	public PlatformStatus getPlatformStatus() {
		lock.lock();
		try {
			return pStatus;
		} finally {
			lock.unlock();
		}
	}

	public void changePlatformStatus(PlatformStatus pStatus) {
		lock.lock();
		try {
			this.pStatus = pStatus;
		} finally {
			lock.unlock();
		}
	}
}
