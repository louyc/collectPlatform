package com.neusoft.gbw.cp.collect.recording.service.transfer;

import com.neusoft.gbw.cp.collect.context.RecordContext;
import com.neusoft.gbw.cp.collect.recording.constant.ConfigVariable;
import com.neusoft.gbw.cp.collect.recording.constant.RecordConstants;
import com.neusoft.gbw.cp.collect.recording.utils.RecordUtils;
import com.neusoft.gbw.cp.collect.recording.vo.StreamParam;
import com.neusoft.nms.common.net.socket.NMSSocketFactory;
import com.neusoft.nms.common.net.socket.client.NMSSocketClient;
import com.neusoft.nms.common.net.socket.exception.NMSSocketException;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;
import com.neusoft.np.arsf.common.util.NPJsonUtil;

public class RecordRecieveListener extends NMService{
	
	private int count = 1;
	private RecordListenerMgr mgr = null;
	private RecordContext channel = null;
	private NMSSocketClient client = null;

	public RecordRecieveListener(RecordListenerMgr mgr) {
		this.mgr = mgr;
		channel = RecordContext.getInstance();
	}

	@Override
	public void run() {
		while(isThreadRunning()) {
			try {
				//采用长连接方式
				connect();
				//重连后，关闭发送线程
				mgr.stopSendListener();
			} catch (NMSSocketException e) {
				Log.error("第" + (count++) + "次重新连接失败", e);
				sleep(1000);
				continue;
			}
			
			String result = null;
			try {
				client.send(RecordConstants.CHECK_INFO);
				Log.info("音频采集平台发送身份验证，验证信息=" + RecordConstants.CHECK_INFO);
				while(true) {
					result = client.receive();
					if(result.trim().startsWith(RecordConstants.CONN_INFO)) {
						Log.info("音频采集平台接收身份验证连接信息，连接信息=" + RecordConstants.CONN_INFO);
						client.send(ConfigVariable.MONITOR_CODE);
						break;
					}
					
				} 
			} catch (NMSSocketException e) {
				Log.error("录音客户端通信异常", e);
				client.disconnect();
				continue;
			}
			//开始录音线程
			mgr.startSendListener(client);
			
			while(isThreadRunning()) {
				try {
					result = client.receive();
					Log.info("接收音频采集任务，task=" + result);
					channel.putTask(decode(result));
					//更新进程状态文件（RmDxx.status）
					RecordUtils.refreshHeartbit();
				} catch (NMSSocketException e) {
					Log.error("", e);
					break;
				}
			}
		}
		
	}
	
	private void connect() throws NMSSocketException {
		String ip = ConfigVariable.DISPOSE_TRANSFER_IP;
		int port = ConfigVariable.DISPOSE_TRANSFER_PORT;
//		int clientPort = ConfigVariable.DISPOSE_TRANSFER_CLIENTBIND_PORT;
//		client = NMSSocketFactory.createSocketBindPortClient(ip, port, clientPort);
		client = NMSSocketFactory.createSocketClient(ip, port);
		client.connect();
	}
	
	public static StreamParam decode(String task){
		StreamParam param = NPJsonUtil.jsonToObject(task, StreamParam.class);
		return param;
	}
	
	private void sleep(long million) {
		try {
			Thread.sleep(million);
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
