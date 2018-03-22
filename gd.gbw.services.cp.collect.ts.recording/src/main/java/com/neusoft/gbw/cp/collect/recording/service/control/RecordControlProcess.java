package com.neusoft.gbw.cp.collect.recording.service.control;

import com.neusoft.gbw.cp.collect.context.RecordContext;
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
			
			int status = param.getStatus();
			switch(status) {
			case 0:
				//如果正在录音，则关闭录音
				if(mgr.threadStat()) {
					Log.debug("系统正在录音，将录音关闭，并等待30秒................");
					mgr.stopRecord();
//					isStreaming = false;
					//在30秒内等待上一个任务完全停止（接收handle被关闭）
					for (int i=0;i<300;i++) {
						if (mgr.isRealStoped()) break;
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							break;
						}
					}
					if (mgr.isRealStoped() == false) {
						Log.warn("系统正在录音，将录音关闭，并等待30秒，接收录音的handle可能未被完全关闭！");
					}
				}
				mgr.startRecord(param);
//				isStreaming = true;
				break;
			case 1:
				mgr.stopRecord();	
//				isStreaming = false;
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
