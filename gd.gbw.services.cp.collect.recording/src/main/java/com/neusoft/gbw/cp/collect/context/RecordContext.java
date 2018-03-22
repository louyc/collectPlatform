package com.neusoft.gbw.cp.collect.context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.collect.recording.constant.RecordConstants;
import com.neusoft.gbw.cp.collect.recording.service.RecordCollectProcess;
import com.neusoft.gbw.cp.collect.recording.vo.StreamParam;
import com.neusoft.gbw.cp.collect.recording.vo.StreamResult;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;


public class RecordContext {
	
	private static class Holder {
		private static final RecordContext INSTANCE = new RecordContext();
	}
	
	private RecordContext(){
		this.inputQueue = ARSFToolkit.getBlockingQueue(RecordConstants.RECORD_TASK_QUEUE, RecordConstants.RECORD_TASK_QUEUE_SIZE);
		this.outputQueue = ARSFToolkit.getBlockingQueue(RecordConstants.RECORD_DATA_QUEUE, RecordConstants.RECORD_DATA_QUEUE_SIZE);
		processMap = new HashMap<String, RecordCollectProcess>();
	}
	
	public static RecordContext getInstance() {
		return Holder.INSTANCE;
	}
	
	private BlockingQueue<StreamParam> inputQueue = null;
	private BlockingQueue<StreamResult> outputQueue = null;
	//key:monitorCode+equCode
	private Map<String, RecordCollectProcess> processMap = null;
	
	public void putProcess(String key, RecordCollectProcess process) {
		processMap.put(key, process);
	}
	
	public RecordCollectProcess getProcess(String key) {
		if(processMap.containsKey(key))
			return processMap.get(key);
		return null; 
	}
	
	public boolean isRunning(String key) {
		if(processMap.containsKey(key))
			return processMap.get(key).isThreadRunning();
		return false;
	}
	
	public void removeProcess(String key) {
		processMap.remove(key);
	}
	
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
