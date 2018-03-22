package com.neusoft.gbw.cp.process.measure.channel.online;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.neusoft.gbw.cp.collect.stream.vo.VoiceStream;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.process.measure.channel.RecordVoiceStream;
import com.neusoft.gbw.cp.process.measure.context.CollectTaskModel;
import com.neusoft.gbw.cp.process.measure.context.MesureRecordModel;
import com.neusoft.gbw.cp.record.exception.RecordingException;
import com.neusoft.gbw.domain.task.effect.intf.dto.OnlineListenerDTO;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class RecordOnlineTaskHandler extends NMService {

	private CollectData data = null;
//	private IRecordProcess process = null;
	private String fileName = null;
	private String subFilePath = null;
	private String http_path = null;
	private String eval_path = null;
	private String save_path = null;
	private VoiceStream info = null;
	private OnlineTaskChannel channel = null;
	private RecordVoiceStream stream = null;
	
	public RecordOnlineTaskHandler(CollectData data, OnlineTaskChannel channel) {
		this.data = data;
		fileName = getRecordFileName(data);
		subFilePath = RecordingAssistant.getpath();
		save_path = getSaveURL(data.getCollectTask());
		this.channel = channel;
		stream = new RecordVoiceStream();
	}

	@Override
	public void run() {
		String streamMediaUrl = getUrl(data);
		Log.info("[在线监听录音]获取音频地址 [" + streamMediaUrl + "] 开始录音...");
		try {
			info = recordDispose(data, save_path, fileName);
		} catch (RecordingException e) {
			Log.error("[在线监听录音]音频漏采," + getLogContent(data.getCollectTask()) + "url=" + streamMediaUrl, e);
			//添加   异常 出现类
			Log.debug("RecordingException     RecordOnlineTaskHandler");
		}
		
		int collectStatus = info.getCollectStatus();
		if(collectStatus == 1) {
			http_path = getRadioURL(data.getCollectTask());
			eval_path = getEvalURL(data.getCollectTask());
		}
		
		Log.info("[在线监听录音]获取音频地址 [" + streamMediaUrl + "] 录音完成");
		// 更新获取的录音记录个数
		channel.updateRecordCount(data);
		//将暂存data对象转换成存储对象,进行存储
		channel.storeData(data, http_path, eval_path);
		//通知智能评估进行打分
		sleepToEval();
		channel.sendEvaluationMsg(data);
		
		//添加逻辑，当每个一个URL录音完成后，需要通知构建服务下发完成任务停止接收机运行
		Log.info("[在线监听录音]通知构建服务，进行下一次录音操作");
		syntStreamStop(data, streamMediaUrl);
		
		//计数，没到个数计数，到了就像web发送消息
		if(isSendWeb(data)) {
			sleep();
			channel.sendWebMsg(getDTO(data.getCollectTask()));
		}
		//清除缓存
		clear();
	}
	
	private VoiceStream recordDispose(CollectData data, String save_path, String fileName) throws RecordingException {
		VoiceStream voice = stream.recordStream(data.getCollectTask(), getUrl(data), 0, save_path, fileName);//status 0:开启 1：关闭
		return voice;
	}
//	private RecordInfo recordDispose(CollectData data) throws RecordingException {
//		RecordInfo info = createRecordInfo(data);
//		int version = data.getCollectTask().getData().getProVersion();
//		switch(version) {
//		case 8:
//			process = RecordFactory.getInstance().newRecordingService(info);
//			process.open();
//			info = process.dispose();
//			break;
//		case 7:
//			VoiceStream voice = stream.recordStream(data.getCollectTask(), getUrl(data), 0);//status 0:开启 1：关闭
//			info.setCutFileByte(voice.getCollectData());
//			info.setRecordLength(voice.getDataLength());
//			break;
//		}
//		return info;
//	}

	
	private boolean isSendWeb(CollectData data) {
		String key = channel.getKey(data.getCollectTask());
		boolean isOver = MesureRecordModel.getInstance().isOverTask(key);
		if(isOver) 
			MesureRecordModel.getInstance().remove(key);
		return isOver;
	}

//	private boolean saveDispose(RecordInfo info) {
//		boolean isFile = false;
//		if(info == null || info.getRecordLength() < 0 || info.getCutFileByte() == null) {
//			return isFile;
//		}
//		isFile = RecordingAssistant.createFilePath(subFilePath,"");
//		if(isFile)
//			isFile = RecordingAssistant.saveRecordToDisk(info ,subFilePath,fileName);
//		return isFile;
//	}
	
	private String getRecordFileName(CollectData data) {
		String file_name = data.getCollectTask().getBusTask().getMonitor_code() 
				+ "_" + data.getCollectTask().getBusTask().getFreq()
				+ "_" + getStationName(data)
				+ "_" + getCurrentTime() + ".wav";
		return file_name;
	}
	
	private String getStationName(CollectData data) {
		String runplan_id = data.getCollectTask().getBusTask().getRunplan_id();
		String stationName = runplan_id == null ? "0" : CollectTaskModel.getInstance().getStationById(runplan_id);
		return stationName;
	} 
	
	private void syntStreamStop(CollectData collectData, String url) {
		CollectTask task = collectData.getCollectTask();
		task.addExpandObj(ExpandConstants.STREAM_STOP_CONTROL_KEY, url);
		ARSFToolkit.sendEvent(EventServiceTopic.STREAM_TASK_OVER_TOPIC, task);
	}

	private String getUrl(CollectData collectData) {
		String url = null;
		int version = data.getData().getVersionId();
		switch (version) {
		case 8:
			List<com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.MediaStream> msListV8 = (List<com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.MediaStream>) ((com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamRealtimeQueryR) ((Report)  collectData.getData().getReportData()).getReport()).getMediaStreams();
			if(msListV8 != null  && !msListV8.isEmpty()) 
				url = msListV8.get(0).getURL();
			break;
		case 7:
			List<com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.MediaStream> msListV7 = (List<com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.MediaStream>) ((com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamRealtimeQueryR) ((Report)  collectData.getData().getReportData()).getReport()).getMediaStreams();
			if(msListV7 != null  && !msListV7.isEmpty()) 
				url = msListV7.get(0).getURL();
			break;
		}
		return url;
	}
	
//	private RecordInfo createRecordInfo(CollectData data) {
//		RecordInfo info = new RecordInfo();
//		CollectTask task = data.getCollectTask();
//		info.setEquCode(task.getAttrInfo().getFirstEquCode());
//		int manufacturer_id = task.getBusTask().getManufacturer_id();
//		if(manufacturer_id == 2) 
//			info.setMantype(ManufacturersType.TS);
//		else
//			info.setMantype(ManufacturersType.Other);
//		info.setMonitorId(task.getBusTask().getMonitor_id() + "");
//		info.setOperType(OperationType.auto);
//		info.setPtype(task.getData().getProVersion() + "");
//		int length = task.getBusTask().getRecordLength();
//		info.setRecordDuration(length == 0 ? ProcessConstants.RECORD_TASK_TIME_LENGTH:length);
//		info.setUrl(getUrl(data));
//		return info;
//	}
	
	private String getRadioURL(CollectTask task) {
		return RecordingAssistant.getURL(fileName);
	}
	
	private String getEvalURL(CollectTask task) {
		return RecordingAssistant.getSaveFilePath(subFilePath,fileName);
	}
	
	private String getSaveURL(CollectTask task) {
		return RecordingAssistant.getSaveFilePath(subFilePath,"");
	}
	
	private String getLogContent(CollectTask task) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("TaskID=" + task.getBusTask().getTask_id() + ",");
		buffer.append("TaskFreq=" + task.getBusTask().getFreq() + ",");
		buffer.append("MonitorID=" + task.getBusTask().getMonitor_id() + ",");
		
		return buffer.toString();
	}
	
	public OnlineListenerDTO getDTO(CollectTask task) {
		OnlineListenerDTO onlineTask = (OnlineListenerDTO)task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		return onlineTask;
	}
	
	private static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(new Date()).toString();
	}
	
	private void sleep() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
	}
	
	private void sleepToEval() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}
	
	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String arg0) {
		this.serviceName = arg0;
	}
	
	private void clear() {
		if(info != null) 
		   info = null;
//		if(process != null) 
//		   process.close();
	}

}
