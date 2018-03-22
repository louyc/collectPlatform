package com.neusoft.np.arsf.core.transfer.service;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.core.transfer.vo.TransferConstants;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferConfig;
import com.neusoft.np.arsf.core.transfer.vo.status.TransferStatus;
import com.neusoft.np.arsf.core.transfer.vo.status.TransferStatusType;

public class TransferMonitorMgr {
	
	private TransferStatus status = null;
	
	private Lock statusLock = new ReentrantLock();
	
	public TransferMonitorMgr(TransferConfig config) {
		status = new TransferStatus();
		status.setConfig(config);
		status.setStatus(TransferStatusType.STATUS_CLOSE);
	}
	
	public TransferStatusType getCurrentStatus() {
		return status.getStatus();
	}
	
	public void updateTransferStatus(TransferStatusType statusType) {
		statusLock.lock();
		try {
			TransferStatusType currentStatus = status.getStatus();
			if (!currentStatus.equals(statusType)) {
				//当前状态发生切换需要通知信息
				status.setStatus(statusType);
				sendEvent();
			}
		} finally {
			statusLock.unlock();
		}
	}
	
	public void open() {
		status.setStatus(TransferStatusType.STATUS_CLOSE);
	}
	
	public void close() {
		
	}
	
	private void sendEvent() {
		ARSFToolkit.sendEvent(TransferConstants.TRANSFER_MONITOR_STATUS_TOPIC, status);
	}
}
