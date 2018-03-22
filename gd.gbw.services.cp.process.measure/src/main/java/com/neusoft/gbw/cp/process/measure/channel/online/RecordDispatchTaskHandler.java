package com.neusoft.gbw.cp.process.measure.channel.online;

import java.util.List;

import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class RecordDispatchTaskHandler extends NMService {

	private OnlineTaskChannel channel = null;
	
	public RecordDispatchTaskHandler(OnlineTaskChannel channel) {
		this.channel = channel;
	}

	@Override
	public void run() {
		CollectData collectData = null;
		while (isThreadRunning()) {
			try {
				collectData = channel.take();
			} catch (InterruptedException e) {
				Log.debug(this.getClass().getName()+"队列存储报错",e);
				break;
			}
			
			//过滤stop音频任务
			if(isFilterStopStream(collectData)) {
				Log.info("[在线监听]该音频为停止音频任务，不做操作, " + getLogMsg(collectData));
				continue;
			}
			
			if(!isStartStream(collectData)) {
				if (collectData != null) {
					CollectTask task = collectData.getCollectTask();
					Log.debug("在线监听CollectTask回收内存。CollectTaskID=" + task.getCollectTaskID());
					task = null;
					collectData = null;
				}
				continue;
			}
			//创建录音线程
			channel.createRecordHandler(collectData);
		}
	}
	
	private boolean isFilterStopStream(CollectData collectData) {
		boolean flag = false;
		int version  = collectData.getCollectTask().getData().getProVersion();
		switch (version) {
		case 8:
			flag = isStopStreamV8(collectData);
			break;
		case 7:
			flag = isStopStreamV7(collectData);
			break;
		case 6:
			//暂时没有v6，v5的收测业务功能
//			flag = isStreamV6(collectData);
			flag = false;
			break;
		case 5:
			//暂时没有v6，v5的收测业务功能
//			flag = isStreamV5(collectData);
			flag = false;
			break;
		}
		return flag;
	}
	
	

	private boolean isStartStream(CollectData collectData) {
		boolean flag = true;
		int version  = collectData.getCollectTask().getData().getProVersion();
		switch (version) {
		case 8:
			flag = isStreamV8(collectData);
			break;
		case 7:
			flag = isStreamV7(collectData);
			break;
		case 6:
			//暂时没有v6，v5的收测业务功能
//			flag = isStreamV6(collectData);
			flag = false;
			break;
		case 5:
			//暂时没有v6，v5的收测业务功能
//			flag = isStreamV5(collectData);
			flag = false;
			break;
		}
		return flag;
	}

	private boolean isStreamV8(CollectData collectData) {
		boolean flag = true;
		try{
			if ((com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamRealtimeQueryR) ((Report)  collectData.getData().getReportData()).getReport() == null) {
				String value = ((Report) collectData.getData().getReportData()).getReportReturn().getValue();
				String desc = ((Report)  collectData.getData().getReportData()).getReportReturn().getDesc();
				Log.info("[录音功能]返回路径异常," + getLogMsg(collectData)+" 失败原因:" +desc + " 失败代码:"+value);
				flag = false;
			}else{
				List<com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.MediaStream> msList = (List<com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.MediaStream>) ((com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamRealtimeQueryR) ((Report)  collectData.getData().getReportData()).getReport()).getMediaStreams();
				if(msList != null  && !msList.isEmpty()) {
					String url = msList.get(0).getURL();
					if(url == null) {
						Log.info("[录音功能]未找到音频路径," + getLogMsg(collectData));
						flag = false;
					}
				}
			}
		}catch(Exception e) {
			flag = false;
		}
		return flag;
	}
	
	private boolean isStreamV7(CollectData collectData) {
		boolean flag = true;
		try{
			if ((com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamRealtimeQueryR) ((Report)  collectData.getData().getReportData()).getReport() == null) {
				String value = ((Report) collectData.getData().getReportData()).getReportReturn().getValue();
				String desc = ((Report)  collectData.getData().getReportData()).getReportReturn().getDesc();
				Log.info("[录音功能]返回路径异常," + getLogMsg(collectData)+" 失败原因:" +desc + " 失败代码:"+value);
				flag = false;
			}else{
				List<com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.MediaStream> msList = (List<com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.MediaStream>) ((com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamRealtimeQueryR) ((Report)  collectData.getData().getReportData()).getReport()).getMediaStreams();
				if(msList != null  && !msList.isEmpty()) {
					String url = msList.get(0).getURL();
					if(url == null) {
						Log.info("[录音功能]未找到音频路径 ," + getLogMsg(collectData));
						flag = false;
					}
				}
			}
		}catch(Exception e) {
			flag = false;
		}
		return flag;
	}
	
	private boolean isStopStreamV8(CollectData collectData) {
		boolean flag = false;
		try{
			Query query = (Query)collectData.getCollectTask().getData().getQuery();
			com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamRealtimeQuery stream = (com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamRealtimeQuery)query.getQuery();
			String command = stream.getRealtimeStreams().get(0).getAction();
			if(command.equalsIgnoreCase("stop"))
				flag = true;
		}catch(Exception e) {
			flag = false;
		}
		return flag;
	}
	
	private boolean isStopStreamV7(CollectData collectData) {
		boolean flag = false;
		try{
			Query query = (Query)collectData.getCollectTask().getData().getQuery();
			com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamRealtimeQuery stream = (com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamRealtimeQuery)query.getQuery();
			String command = stream.getRealtimeStreams().get(0).getAction();
			
			if(command.equalsIgnoreCase("stop")) 
				flag = true;
		}catch(Exception e) {
			flag = false;
		}
		return flag;
	}
	
	private String getLogMsg(CollectData data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("taskId=" + data.getCollectTask().getBusTask().getTask_id() + ",");
		buffer.append("monitorId=" + data.getCollectTask().getBusTask().getMonitor_id() + ",");
		buffer.append("Freq=" + data.getCollectTask().getBusTask().getFreq() + ",");
		return buffer.toString();
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String arg0) {
		this.serviceName = arg0;
	}
}
