package com.neusoft.gbw.cp.collect.recording.service.control;

import com.neusoft.gbw.cp.collect.recording.service.RecordCollectProcess;
import com.neusoft.gbw.cp.collect.recording.vo.StreamParam;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMServiceCentre;

public class RecordControlMgr {
	
	
	private RecordControlProcess process = null;
	private Thread thread = null;
	private NMServiceCentre pool = new NMServiceCentre();
	
	public void open() {
		//创建控制线程
		process = new RecordControlProcess(this);
		process.setServiceName("录音控制线程");
		try{
			pool.addService(process);
		}catch(Exception e){
			Log.debug(this.getClass().getName()+"录音控制线程启动报错",e);
		}
	}
	
	public RecordCollectProcess startRecord(StreamParam param){
		RecordCollectProcess collectProcess = new RecordCollectProcess(param);
		collectProcess.setServiceName("录音采集线程#" + param.getMonitorCode() + "_" + param.getEquCode());
		thread = new Thread(collectProcess);
		thread.start();
		return collectProcess;
	}
	
	public boolean threadStat() {
		return thread == null ? false :thread.isAlive();
	}
	
	public void close() {
		pool.stopAllServicePool();
	}
}
