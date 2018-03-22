package com.neusoft.gbw.cp.collect.stream.service.transfer;

import com.neusoft.gbw.cp.collect.stream.context.ServiceContext;
import com.neusoft.gbw.cp.collect.stream.service.ConverUtils;
import com.neusoft.gbw.cp.collect.stream.vo.StreamChannel;
import com.neusoft.gbw.cp.collect.stream.vo.StreamParam;
import com.neusoft.nms.common.net.socket.exception.NMSSocketException;
import com.neusoft.nms.common.net.socket.server.NMSSocketServer;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;
import com.neusoft.np.arsf.common.util.NMService;

public class CollectStreamResquestHandler extends NMService {
	
	private String socketKey = null;
	private NMSSocketServer server = null;
	private CollectStreamListenerMgr mgr = null;
	private final String CHECK_INFO = "<collect-stream-resquest/>";
	private final String CONN_INFO = "<collect-stream-response/>";
	private ServiceContext context = null;
	private StreamChannel channel = null;
	private String monitorCode = null;

	public CollectStreamResquestHandler(String socketKey, CollectStreamListenerMgr mgr) {
		this.server = mgr.getServer();
		this.socketKey = socketKey;
		context = ServiceContext.getInstance();
		this.mgr = mgr;
	}

	@Override
	public void run() {
		boolean isOpen = false;
		String str = null;
		//建立通信信道
		while(true) {
			try {
				str = server.receive(socketKey);
				if (str == null)
					continue;
				Log.debug("接收到请求身份验证的信息*********"+str);
				if (str.trim().startsWith(CHECK_INFO)) {
					server.send(socketKey, CONN_INFO);
					//获取站点的编码
					str = server.receive(socketKey);
					//懒加载多个站点信息
					creatCollectStream(str);
					monitorCode = str;
					//缓存code所开启的客户端
					//如果客户端存在，则停止原来的线程。
//					if(context.containMonitor(monitorCode)) {
//						Log.debug("该站点平台客户端已注册，停掉原有线程，monitorCode=" + monitorCode);
//						String oldSocketKey = context.getSocketKey(monitorCode);
//						stopStreamReq(oldSocketKey);
//						stopStreamRes(oldSocketKey);
//					}else {
//						context.putMonitor(monitorCode, socketKey);
//					}
//					context.putStreamReq(socketKey, this);
					Log.info("平台音频采集通过身份验证，建立连接=" + socketKey + ", Code=" + str);
					isOpen = true;
					break;
				} 
				Log.info("平台音频采集未通过身份验证，连接KEY=" + socketKey);
				Thread.sleep(1000);
			} catch (Exception e) {
				Log.error("音频通信异常", e);
				break;
			}
		}
		
		//如果校验连接未通过，则中断线程其他操作
		if(!isOpen) {
			Log.warn("平台音频采集未通过身份验证，中断线程中其他操作。monitorCode=" + monitorCode + ",socketKey=" + socketKey);
			try {
				server.removeClient(socketKey);
			} catch (NMSSocketException e) {
				Log.error("关闭socket连接异常", e);
			}
			return;
		}
		
		//开启接收采集数据线程
		mgr.startCollectVoiceStream(socketKey);
//		context.putStreamRes(socketKey, voice);
		
		//传输实际音频录音请求和采集结果
		StreamParam stream = null;
		while(isThreadRunning()) {
			try {
				stream = channel.take();
			} catch (InterruptedException e) {
				Log.debug(this.getClass().getName()+"队列存储报错",e);
				break;
			}
			
			String request = getStreamRequest(stream);
			//发送录音请求
			try {
				Log.info("发送录音任务，socketKey＝" + socketKey + "，request＝" + request);
				server.send(socketKey, request);
				Log.debug("发送录音任务SUCCESS，socketKey＝" + socketKey + "，request＝" + request);
			} catch (NMSSocketException e) {
//				//如果发送失败，主动断开连接，考虑重新发送此请求
				Log.error("发送录音任务通信异常，SocketKey=" + socketKey, e);
				try {
					channel.put(stream);
				} catch (InterruptedException ex) {
				}
				break;
			}
		}
		Log.debug(monitorCode +"音频录音平台已重启，关闭原已启动的录音平台线程 ,socketKey=" + socketKey);
	}
	
//	private void stopStreamReq(String socketKey) {
//		context.stopStreamReq(socketKey);
//	}
//
//	private void stopStreamRes(String socketKey) {
//		context.stopStreamRes(socketKey);
//	}
	
	private String getStreamRequest(StreamParam stream) {
		try {
			return ConverUtils.converString(stream);
		} catch (NMBeanUtilsException e) {
			return null;
		}
	}
	
	private void creatCollectStream(String str) {
		channel = context.getStreamChannel(str);
		if(channel == null) {
			channel = new StreamChannel(str);
			context.put(str, channel);
		}
	}

	@Override
	public String getServiceName() {
		return this.serviceName ;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}
