//package com.neusoft.gbw.cp.collect.stream.service.transfer;
//
//import com.neusoft.gbw.cp.collect.stream.context.ServiceContext;
//import com.neusoft.gbw.cp.collect.stream.service.ByteUtils;
//import com.neusoft.gbw.cp.collect.stream.vo.StreamParam;
//import com.neusoft.gbw.cp.collect.stream.vo.VoiceStream;
//import com.neusoft.gbw.cp.core.EventServiceTopic;
//import com.neusoft.nms.common.net.socket.exception.NMSSocketException;
//import com.neusoft.nms.common.net.socket.server.NMSSocketServer;
//import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
//import com.neusoft.np.arsf.base.bundle.Log;
//import com.neusoft.np.arsf.common.util.NMService;
//
//public class OldCollectVoiceStreamHandler extends NMService {
//	
//	private String socketKey = null;
//	private NMSSocketServer server = null;
//	private static final String MEASURE = "measure";
//	private static final String REALTIME	= "realtime";
//	
//	public OldCollectVoiceStreamHandler(String socketKey, NMSSocketServer server) {
//		this.socketKey = socketKey;
//		this.server = server;
//	}
//
//	@Override
//	public void run() {
//		byte[] result = null;
//		VoiceStream stream = null;
//		while(isThreadRunning()) {
//			try {
//				//接收录音返回的字节数组
//				result = server.receive(socketKey, 17);
//				if(result == null) {
//					sleep(500); 
//					continue;
//				}
//				String id = ByteUtils.getId(result) + "";
//				int length = ByteUtils.getLength(result);
//				int status = ByteUtils.getStatus(result);
//				
//				Log.debug("接收录音平台录音数据，id=" + id + ",来源socketKey=" + socketKey);
//				result = server.receive(socketKey, length);
//				StreamParam para = ServiceContext.getInstance().getStreamParam(id);
//				if (para == null) {
//					Log.warn("没有查找到指定的ID的音频请求任务， ID=" + id);
//					continue;
//				}
//				
//				stream = new VoiceStream();
//				stream.setInfo(para);
//				stream.setCollectData(result);
//				stream.setDataLength(length);
//				stream.setCollectStatus(status);
//				
//				String srcCode = para.getSrcCode();
//				switch(srcCode) {
//				case MEASURE:
//					ARSFToolkit.sendEvent(EventServiceTopic.COLLECT_MEASURE_STREAM_RESPONSE_TOPIC, stream);
//					break;
//				case REALTIME:
//					ARSFToolkit.sendEvent(EventServiceTopic.COLLECT_REALTIME_STREAM_RESPONSE_TOPIC, stream);
//					break;
//				}
//				Log.debug("录音任务采集完成，para=" + para.toString());
//			} catch (NMSSocketException e) {
//				Log.error("通信出现异常，SocketKey=" + socketKey, e);
//				break;
//			}
//		}
//	}
//	
////	private boolean isEmptyResult(byte[] result) {
////		boolean isEmpty = true;
////		for(int i=0;i<result.length;i++) {
////			if(result[i] != 0)
////				isEmpty = false;
////		}
////		return isEmpty;
////	}
//	
//	private void sleep(long millis) {
//		try {
//			Thread.sleep(millis);
//		} catch (InterruptedException e) {
//		}
//	}
//	
//	@Override
//	public String getServiceName() {
//		return this.serviceName;
//	}
//
//	@Override
//	public void setServiceName(String serviceName) {
//		this.serviceName = serviceName;
//	}
//
//}
