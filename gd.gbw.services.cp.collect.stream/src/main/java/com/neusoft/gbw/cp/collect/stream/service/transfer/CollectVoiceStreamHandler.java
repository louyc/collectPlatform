package com.neusoft.gbw.cp.collect.stream.service.transfer;

import com.neusoft.gbw.cp.collect.stream.context.ServiceContext;
import com.neusoft.gbw.cp.collect.stream.vo.StreamParam;
import com.neusoft.gbw.cp.collect.stream.vo.StreamResult;
import com.neusoft.gbw.cp.collect.stream.vo.VoiceStream;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.nms.common.net.socket.exception.NMSSocketException;
import com.neusoft.nms.common.net.socket.server.NMSSocketServer;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;
import com.neusoft.np.arsf.common.util.NPJsonUtil;

public class CollectVoiceStreamHandler extends NMService {
	
	private String socketKey = null;
	private NMSSocketServer server = null;
	private static final String MEASURE = "measure";
	private static final String REALTIME	= "realtime";
	
	public CollectVoiceStreamHandler(String socketKey, NMSSocketServer server) {
		this.socketKey = socketKey;
		this.server = server;
	}

	@Override
	public void run() {
		String result = null;
		VoiceStream stream = null;
		while(isThreadRunning()) {
			try {
				//接收录音返回的状态数据
				result = server.receive(socketKey);
				if(result == null) {
					sleep(500); 
					continue;
				}
				Log.debug("接收录音平台录音数据，来源socketKey=" + socketKey + ",result=" + result);
				
				StreamResult streamResult = decode(result);
				String id = streamResult.getId();
				StreamParam para = ServiceContext.getInstance().getStreamParam(id);
				if (para == null) {
					Log.warn("没有查找到指定的ID的音频请求任务， ID=" + id);
					continue;
				}
				
				stream = new VoiceStream();
				stream.setInfo(para);
				//采集文件大小长度
				stream.setDataLength(streamResult.getRecordLength());
				//采集状态 3失败
				stream.setCollectStatus(streamResult.getCollectStatus());
				//服务code
				String srcCode = para.getSrcCode();
				switch(srcCode) {
				case MEASURE:
					ARSFToolkit.sendEvent(EventServiceTopic.COLLECT_MEASURE_STREAM_RESPONSE_TOPIC, stream);
					break;
				case REALTIME:
					ARSFToolkit.sendEvent(EventServiceTopic.COLLECT_REALTIME_STREAM_RESPONSE_TOPIC, stream);
					break;
				}
				Log.debug("录音任务采集完成，para=" + para.toString());
			} catch (NMSSocketException e) {
				Log.error("接收录音消息通信出现异常，SocketKey=" + socketKey, e);
				break;
			}
		}
	}
	
	public static StreamResult decode(String result){
		return NPJsonUtil.jsonToObject(result, StreamResult.class);
	}
	
//	private boolean isEmptyResult(byte[] result) {
//		boolean isEmpty = true;
//		for(int i=0;i<result.length;i++) {
//			if(result[i] != 0)
//				isEmpty = false;
//		}
//		return isEmpty;
//	}
	
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
