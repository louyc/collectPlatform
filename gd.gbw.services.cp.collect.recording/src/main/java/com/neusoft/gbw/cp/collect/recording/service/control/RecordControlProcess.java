 package com.neusoft.gbw.cp.collect.recording.service.control;

import com.neusoft.gbw.cp.collect.context.RecordContext;
import com.neusoft.gbw.cp.collect.recording.service.RecordCollectProcess;
import com.neusoft.gbw.cp.collect.recording.vo.StreamParam;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class RecordControlProcess extends NMService{
	
	private RecordContext channel = null;
	private RecordControlMgr mgr = null;
//	private boolean isStreaming = false;

	public RecordControlProcess(RecordControlMgr mgr) {
		this.mgr = mgr;
		channel = RecordContext.getInstance();
	}

	@Override
	public void run() {
		StreamParam param = null;
		while(isThreadRunning()) {
			try {
				param = channel.streamTake();
			} catch (InterruptedException e) {
				break;
			}
			String monitorCode = param.getMonitorCode();
			String equCode = param.getEquCode();
			String key = monitorCode + "_" + equCode;
			//0 开始录音 ，1停止录音
			int status = param.getStatus();
			switch(status) {
			case 0:
				//检查录音线程是否录音
				if(channel.isRunning(key)) {
					RecordCollectProcess runingProcess = channel.getProcess(key);
					if(runingProcess != null)
						//停止录音线程
						runingProcess.stopRecord();
//					sleep(1000);
					//在30秒内等待上一个任务完全停止（接收handle被关闭）
					for (int i=0;i<300;i++) {
						if (runingProcess ==null || !runingProcess.isThreadRunning()) break;
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							break;
						}
					}
					if (runingProcess != null) {
						Log.warn("系统正在录音(V8)，将录音关闭，并等待30秒，接收录音的handle可能未被完全关闭！");
					}
				}
				RecordCollectProcess process = mgr.startRecord(param);
				//缓存录音线程句柄
				channel.putProcess(key, process);
				break;
			case 1:
				RecordCollectProcess runingProcess = channel.getProcess(key);
				if(runingProcess != null)
					runingProcess.stopRecord();
				break;
			}
		}
		
	}
	
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
