package com.neusoft.gbw.cp.collect.context;

import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.collect.recording.constant.RecordConstants;
import com.neusoft.gbw.cp.collect.recording.vo.StreamParam;
import com.neusoft.gbw.cp.collect.recording.vo.StreamResult;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;


public class RecordContext {
	
	private static class Holder {
		private static final RecordContext INSTANCE = new RecordContext();
	}
	
	private RecordContext(){
		this.inputQueue = ARSFToolkit.getBlockingQueue(RecordConstants.RECORD_TASK_QUEUE, RecordConstants.RECORD_TASK_QUEUE_SIZE);
		this.outputQueue = ARSFToolkit.getBlockingQueue(RecordConstants.RECORD_DATA_QUEUE, RecordConstants.RECORD_DATA_QUEUE_SIZE);
	}
	
	public static RecordContext getInstance() {
		return Holder.INSTANCE;
	}
	
	private BlockingQueue<StreamParam> inputQueue = null;
	private BlockingQueue<StreamResult> outputQueue = null;
	
	public void putTask(StreamParam param) {
		try {
			inputQueue.put(param);
		} catch (InterruptedException e) {
		}
	}
	
	public void putData(StreamResult data) {
		try {
			outputQueue.put(data);
		} catch (InterruptedException e) {
		}
	}
	
	public StreamParam streamTake() throws InterruptedException {
		return inputQueue.take();
	}

	public StreamResult dataTake() throws InterruptedException {
		return outputQueue.take();
	}
}
