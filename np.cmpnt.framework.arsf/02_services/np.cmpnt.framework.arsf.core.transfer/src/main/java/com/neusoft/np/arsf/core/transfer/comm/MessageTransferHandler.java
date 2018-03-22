package com.neusoft.np.arsf.core.transfer.comm;

import java.util.concurrent.BlockingQueue;

import com.neusoft.np.arsf.base.bundle.ARSFToolkit;

public class MessageTransferHandler extends NMTransferService {

	public void send(Object result) {
		ARSFToolkit.sendEvent(getTopic(), result);
	}

	public MessageTransferHandler(BlockingQueue<Object> queue) {
		super(queue);
	}
}
