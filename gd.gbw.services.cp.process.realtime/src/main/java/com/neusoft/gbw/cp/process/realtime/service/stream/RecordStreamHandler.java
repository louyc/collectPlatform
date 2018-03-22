package com.neusoft.gbw.cp.process.realtime.service.stream;

import com.neusoft.gbw.cp.collect.stream.vo.VoiceStream;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.process.realtime.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.realtime.context.StreamTaskMode;
import com.neusoft.gbw.cp.record.exception.RecordingException;
import com.neusoft.gbw.cp.record.service.IRecordProcess;
import com.neusoft.gbw.cp.record.vo.RecordInfo;
import com.neusoft.gbw.domain.task.effect.intf.dto.RealTimeStreamDTO;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.gbw.infrastructure.msgcenter.srv.GBWMsgConstant;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class RecordStreamHandler extends NMService {
	private CollectTask task = null;
	private RecordInfo info = null;
	private VoiceStream steam = null;
	private IRecordProcess process = null;
	private String fileName = null;
	private String subFilePath = null;
	private String filePath = null;
	private RecordVoiceStream stream = null;
	private boolean is_stop;
	
	public RecordStreamHandler(CollectTask task, RecordInfo info) {
		this.info = info;
		this.is_stop = false;
		this.task = task;
		init();
	}

	@Override
	public void run() {
			String streamMediaUrl = info.getUrl();
			Log.info("获取音频地址 [" + streamMediaUrl + "] 开始录音...");
			
			try {
				steam = recordDispose(task, info, filePath, fileName);
			} catch (RecordingException e) {
				Log.error("[录音服务]音频漏采,TaskFreq=" + task.getBusTask().getFreq() + "url=" + streamMediaUrl, e);
			}
			
			int collectStatus = steam.getCollectStatus();
			if(collectStatus == 1) 
				sendAppMsg(task,true);
			else 
				sendAppMsg(task,false);
			
			Log.info("音频地址 [" + streamMediaUrl + "] 录音完成");
			
			//查看是否在播音,没有播音停止音频任务或者是否是自动关闭，如果是则
			if(isStreamRuning(streamMediaUrl) || is_stop)
				syntStreamStop(task, streamMediaUrl);
	}
	
	private VoiceStream recordDispose(CollectTask task, RecordInfo info, String save_path, String fileName) throws RecordingException {
		VoiceStream voice = stream.recordStream(task, info.getUrl(), 0, save_path, fileName);//status 0:开启 1：关闭
		return voice;
	}
	
//	private RecordInfo recordDispose(CollectTask task, RecordInfo info) throws RecordingException {
//		int version = task.getData().getProVersion();
//		switch(version) {
//		case 8:
//			process = RecordFactory.getInstance().newRecordingService(info);
//			process.open();
//			info = process.dispose();
//			break;
//		case 7:
//			VoiceStream voice = stream.recordStream(task, info.getUrl(), 0);//status 0:开启 1：关闭
//			info.setRecordLength(voice.getDataLength());
//			break;
//		}
//		return info;
//	}
	
	public void setRecordStatus(boolean is0ver) {
		int version = task.getData().getProVersion();
		switch(version) {
		case 7:
			stream.stopRecordStream(task, info.getUrl(), 1, filePath, fileName);
			break;
		case 8:
			process.setOver(is0ver);
			this.is_stop = is0ver;
			break;
		default:
			Log.debug("未找到正确的站点协议版本，version=" + version); 
			break;
		}

	}
	
	private boolean isStreamRuning(String url) {
		return StreamTaskMode.getInstance().isStreamRuning(url);
	}
	
	private void sendAppMsg(CollectTask task,boolean isSuccess) {
		JMSDTO jms = new JMSDTO();
		String http_path = StreamAssistant.getHttpPath(subFilePath, fileName);
		RealTimeStreamDTO dto = getDto(task);
		
		if(!isSuccess) {
			dto.setReturnType(ProcessConstants.OTHER_EXCEPTION);
			dto.setReturnDesc(ProcessConstants.STREAM_LEAKAGE);
			send(jms,dto);
			return;
		}
		
		dto.setUrl(http_path);
		//查看录音状态，true 为手动停止，  false为超时停止
		if(is_stop) {
			Log.info("[录音服务]手动停止录音,TaskFreq=" + task.getBusTask().getFreq() + ", http_path=" + http_path);
			dto.setReturnType(ProcessConstants.REPORT_SUCCESS);
			dto.setReturnDesc(ProcessConstants.REPORT_SUCCESS_DESC);
		}else {
			Log.info("[录音服务]录音超时,TaskFreq=" + task.getBusTask().getFreq() + ", http_path=" + http_path);
			dto.setReturnType(ProcessConstants.STREAM_TIME_OUT);
			dto.setReturnDesc(ProcessConstants.RECORD_TIME_OUT_DESC);
		}
		
		send(jms,dto);
	}
	
	private void send(JMSDTO jms,RealTimeStreamDTO dto) {
		jms.setObj(dto);
		jms.setTypeId(GBWMsgConstant.C_JMS_REAL_RECORD_RESPONSE_MSG);
		ARSFToolkit.sendEvent(ProcessConstants.JMS_SEND_MSG_TOPIC, jms);
		Log.info("[录音服务]发送前台录音完成消息,TaskFreq=" + task.getBusTask().getFreq() + ",fileName=" + fileName);
	}
	
	private RealTimeStreamDTO getDto(CollectTask task) {
		RealTimeStreamDTO dto = (RealTimeStreamDTO)task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		dto.setCommand(ProcessConstants.STOP_COMMAND);
		return dto;
	}
	
	
//	private boolean saveDispose(RecordInfo info) {
//		boolean isFile = false;
//		if(info == null || info.getRecordLength() <= 0) {
//			return isFile;
//		}
//		
//		isFile = StreamAssistant.createFilePath(subFilePath,"");
//		if(isFile)
//			isFile = StreamAssistant.saveRecordToDisk(info ,subFilePath,fileName);
//		return isFile;
//	}
	
	
	private void syntStreamStop(CollectTask task, String url) {
		Log.info("[录音功能]自动发送音频停止消息至站点，url=" + url);
		updateDTO(task);
		task.addExpandObj(ExpandConstants.STREAM_STOP_CONTROL_KEY, url);
		ARSFToolkit.sendEvent(EventServiceTopic.STREAM_TASK_OVER_TOPIC, task);
		removeRecord(task);
	}
	
	private void updateDTO(CollectTask task) {
		 ((RealTimeStreamDTO)task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY)).setCommand(ProcessConstants.STOP_COMMAND);
	}
	
	private void removeRecord(CollectTask task) {
		 RealTimeStreamDTO dto =  getDto(task);
		 String key = dto.getFreq() + "_" + dto.getUrl();
		 StreamTaskMode.getInstance().removeRecord(key);
	}
	
	private void init() {
		fileName = StreamAssistant.getRecordFileName(task);
		subFilePath = StreamAssistant.getpath();
		stream = new RecordVoiceStream();
		filePath = StreamAssistant.getSaveFilePath(subFilePath, "");
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
