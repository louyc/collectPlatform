package com.neusoft.gbw.cp.process.realtime.service.transfer.equ;

import com.neusoft.gbw.cp.collect.stream.vo.StreamParam;
import com.neusoft.gbw.cp.process.realtime.constants.ConfigVariable;
import com.neusoft.nms.common.net.socket.NMSSocketFactory;
import com.neusoft.nms.common.net.socket.client.NMSSocketClient;
import com.neusoft.nms.common.net.socket.exception.NMSSocketException;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;
import com.neusoft.np.arsf.common.util.NPJsonUtil;

public class AlarmSendListener extends NMService{
	
	private int count = 1;
	private EquAlarmListenerMgr mgr = null;
	private NMSSocketClient client = null;

	public AlarmSendListener(EquAlarmListenerMgr mgr) {
		this.mgr = mgr;
	}

	@Override
	public void run() {
		String alarm = null;
		while(isThreadRunning()) {
			try {
				connect();
			} catch (NMSSocketException e) {
				Log.error("设备高告警发送线程，第" + (count++) + "次重新连接失败", e);
				sleep(1000);
				continue;
			}
			
			while(isThreadRunning()) {
				try {
					alarm = mgr.take();
					if(alarm == null)
						continue;
					
					client.send(alarm);
					Log.debug("发送告警信息至5.3采集平台，alarm=" + alarm);
				} catch (NMSSocketException e) {
					Log.error("", e);
					break;
				} catch (InterruptedException e) {
					Log.error("", e);
					break;
				}
			}
		}
		
	}
	
	private void connect() throws NMSSocketException {
		String ip = ConfigVariable.COLLECT_PLETFORM_IP;
		int port = ConfigVariable.COLLECT_PLETFORM_PORT;
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
