package com.neusoft.gbw.cp.collect.recording.service.control;

import com.neusoft.gbw.cp.collect.recording.service.IRecord;
import com.neusoft.gbw.cp.collect.recording.service.RecordCollectProcess;
import com.neusoft.gbw.cp.collect.recording.service.V7RecordHandler;
import com.neusoft.gbw.cp.collect.recording.vo.StreamParam;
import com.neusoft.np.arsf.common.util.NMServiceCentre;

public class RecordControlMgr {
	
	private IRecord handler = null;
	private RecordControlProcess process = null;
	private RecordCollectProcess collectProcess = null;
	private Thread thread = null;
	private NMServiceCentre pool = new NMServiceCentre();
	
	public void init() {
		handler = new V7RecordHandler();
	}
	
	public void open() {
		//创建控制线程
		process = new RecordControlProcess(this);
		process.setServiceName("录音控制线程");
		pool.addService(process);
	}
	
	public void startRecord(StreamParam param){
		collectProcess = new RecordCollectProcess(this,param);
		collectProcess.setServiceName("录音采集线程");
		thread = new Thread(collectProcess);
		thread.start();
	}
	
	public void stopRecord() {
		this.getHandler().stop();
	}
	
	public boolean threadStat() {
		return thread == null ? false :thread.isAlive();
	}
	
	public void close() {
		pool.stopAllServicePool();
	}

	public IRecord getHandler() {
		return handler;
	}
	
	public boolean isRealStoped() {
		if (handler == null) return true;
		return handler.isRealStoped();
	}
	
}
