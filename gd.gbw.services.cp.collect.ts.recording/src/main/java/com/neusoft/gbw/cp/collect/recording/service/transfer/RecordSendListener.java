package com.neusoft.gbw.cp.collect.recording.service.transfer;

import com.neusoft.gbw.cp.collect.context.RecordContext;
import com.neusoft.gbw.cp.collect.recording.vo.StreamResult;
import com.neusoft.nms.common.net.socket.client.NMSSocketClient;
import com.neusoft.nms.common.net.socket.exception.NMSSocketException;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;
import com.neusoft.np.arsf.common.util.NPJsonUtil;

public class RecordSendListener extends NMService{
	
	private RecordContext channel = null;
	private NMSSocketClient client = null;

	public RecordSendListener(NMSSocketClient client) {
		this.client = client;
		channel = RecordContext.getInstance();
	}

	@Override
	public void run() {
		StreamResult result = null;
		String mesResult = null;
		while(isThreadRunning()) {
			try {
				result = channel.dataTake();
				mesResult = decode(result);
				Log.debug("录音采集完成，返回结果信息至采集平台，result=" + mesResult.toString());
				client.send(mesResult);
			} catch (InterruptedException e) {
				Log.error("", e);
			} catch (NMSSocketException e) {
				Log.error("", e);
			}finally{
				result = null;
				mesResult =null;
			}
		}
		
	}
	
	public static String decode(StreamResult result){
		return NPJsonUtil.objectToJson(result);
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
