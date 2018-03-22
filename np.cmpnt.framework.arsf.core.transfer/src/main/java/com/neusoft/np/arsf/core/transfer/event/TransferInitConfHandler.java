package com.neusoft.np.arsf.core.transfer.event;

import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.core.transfer.service.TransferConfigProcess;
import com.neusoft.np.arsf.core.transfer.vo.TransferConstants;
import com.neusoft.np.arsf.core.transfer.vo.conf.TransferConfig;

public class TransferInitConfHandler implements BaseEventHandler {
	
	private TransferConfigProcess transferProcess = null;
	
	public TransferInitConfHandler() {
		transferProcess = new TransferConfigProcess();
	}

	@Override
	public String getTopicName() {
		return TransferConstants.TRANSFER_INIT_CONF_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if (arg0 instanceof TransferConfig) {
			transferProcess.openChannel((TransferConfig)arg0);
		}
		return true;
	}
}
