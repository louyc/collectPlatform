package com.neusoft.gbw.cp.collect.recording.service.transfer;

import com.neusoft.nms.common.net.socket.client.NMSSocketClient;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMServiceCentre;

public class RecordListenerMgr {
	
	private NMServiceCentre pool = new NMServiceCentre();
	private RecordRecieveListener recieveListener = null;
	private RecordSendListener sendListener = null;
	private String recieveThreadName = "录音任务接收客户端线程";
	private String sendThreadName = "录音数据发送客户端线程";

	public void openRecordingListener() {
		startRecieveListener();
	}
	
	private void startRecieveListener() {
		Log.info("****************创建音频采集数据任务接收线程**************");
		recieveListener = new RecordRecieveListener(this);
		recieveListener.setServiceName(recieveThreadName);
		pool.addService(recieveListener);
		Log.info("音频采集数据任务接收线程启动");
	}
	
	protected void startSendListener(NMSSocketClient client) {
		Log.info("*************创建音频采集数据发送线程***************");
		sendListener = new RecordSendListener(client);
		sendListener.setServiceName(sendThreadName);
		pool.addService(sendListener);
		Log.info("音频采集数据发送线程启动");
	}
	
	protected void stopSendListener() {
		if(sendListener != null) {
			pool.removeServiceByName(sendThreadName);
			sendListener = null;
		}
		Log.info("音频采集数据发送线程关闭");
	}
	
	
	
	public void closeRecordingListener() {
		pool.stopAllServicePool();
	}
}
