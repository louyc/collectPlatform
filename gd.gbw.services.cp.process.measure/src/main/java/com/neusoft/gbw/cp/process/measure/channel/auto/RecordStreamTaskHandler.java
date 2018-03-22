package com.neusoft.gbw.cp.process.measure.channel.auto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.neusoft.gbw.cp.collect.stream.vo.VoiceStream;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.process.measure.channel.MeaUnitChannel;
import com.neusoft.gbw.cp.process.measure.channel.RecordVoiceStream;
import com.neusoft.gbw.cp.process.measure.constants.ConfigVariable;
import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.measure.context.CollectTaskModel;
import com.neusoft.gbw.cp.record.exception.RecordingException;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class RecordStreamTaskHandler extends NMService {

	private CollectData data = null;
//	private IRecordProcess process = null;
	private VoiceStream info = null;
	private String fileName = null;
	private String http_path = null;
	private String eval_path = null;
	private String save_path = null;
	private MeaUnitChannel channel = null;
	private RecordVoiceStream stream = null;
	private String collect_desc = null;
	
	public RecordStreamTaskHandler(CollectData data, MeaUnitChannel channel) {
		this.data = data;
		fileName = getRecordFileName(data);
		save_path = getSaveURL(data.getCollectTask());
		this.channel = channel;
		this.stream = new RecordVoiceStream();
	}

	@Override
	public void run() {
		String streamMediaUrl = getUrl(data);
		Log.info("获取音频地址 [" + streamMediaUrl + "] 开始录音...");
		
		try {
			info = recordDispose(data, save_path, fileName);
		} catch (RecordingException e) {
			Log.error("[录音服务]音频漏采," + getLogContent(data.getCollectTask()) + "url=" + streamMediaUrl, e);
		}
		int recordLength = info.getDataLength();
		Log.debug("%%%%%%%%%%%%%%%%%%%%%%    "+recordLength+"     %%%%%%%%%%%%%%%%%%%%%");
		if(recordLength < 441000) {
			Log.warn("[录音服务]音频漏采,音频大小不足," + getLogContent(data.getCollectTask()) + ",url=" + streamMediaUrl + ",length=" + recordLength);
			int manufacturer_id = data.getCollectTask().getBusTask().getManufacturer_id();
			String monitorCode = data.getCollectTask().getBusTask().getMonitor_code();
			if(manufacturer_id == ProcessConstants.IS_TAISHUN_MONITOR && recordLength == 0) {
				checkRecordMonitor(monitorCode);
			}
			//获取采集数据状态，确定状态类型，将失败原因写入数据库，并将任务发送到采集平台进行补采
			int measureStatus = getStatus(data);
			//判断漏采为第一次漏采，还是补采，漏采则发送给采集进行补采，如果是补采则将漏采原因封装入库
			if(measureStatus == 1) {
				Log.debug("[录音功能]收测单元补采，" +  getLogContent(data.getCollectTask()));
				try {
					Thread.sleep(10000);  //漏采 等待10s后 再重新发送采集任务
				} catch (InterruptedException e) {
					Log.debug("线程等待报错",e);
				}
				sendTask(data.getCollectTask());
				return;
			}else {
				collect_desc = ProcessConstants.LeakageReason.COLLECT_FILE_SIZE_NOT_ENOUGH;
			}
		}else //录音文件采集成功状态更新
			collect_desc = ProcessConstants.LeakageReason.RECORD_FILE_COLLECT_SUCCESS;
		
		int collectStatus = info.getCollectStatus();
		if(collectStatus == 1) { //收测状态， 1：第一次采集，2：补采
			http_path = getRadioURL(data.getCollectTask());
			eval_path = getEvalURL(data.getCollectTask());
		}else {
			Log.debug("[录音服务]录音文件创建失败，eval_path=" + eval_path +  getLogContent(data.getCollectTask()));
			if(!collect_desc.equals(ProcessConstants.LeakageReason.COLLECT_FILE_SIZE_NOT_ENOUGH))
				collect_desc = ProcessConstants.LeakageReason.RECORD_FILE_CREATE_FAUIL;
		}
		
		Log.debug("获取音频地址 [" + streamMediaUrl +"   "+  data.getCollectTask().getBusTask().getRunplan_id()+"  "+
				data.getCollectTask().getBusTask().getUnit_begin()	+
				"   "+collect_desc+ " 录音完成"
				+"站点名称："+data.getCollectTask().getBusTask().getMonitor_id()+
				" 老站点名称：  "+data.getCollectTask().getBusTask().getOrgMonitorId()
				+" 任务id："+data.getCollectTask().getBusTask().getTask_id()
				+" unit开始："+data.getCollectTask().getBusTask().getUnit_begin()
				+" unit结束："+data.getCollectTask().getBusTask().getUnit_end());
		// 更新获取的录音记录个数
		//channel.updateRecordCount(data);
		//将暂存data对象转换成存储对象,进行存储
		channel.storeData(data, http_path, eval_path, collect_desc);
		//添加逻辑，当每个一个URL录音完成后，需要通知构建服务下发完成任务停止接收机运行
		Log.debug("通知构建服务，进行下一次录音操作");
		syntStreamStop(data, streamMediaUrl);
		//确保智能评估能够执行完整个评估过程，如果异常通过保障机制设置评估失败
		sleep();
		channel.storeSafeAssessData(data);
		clear();
	}
	
	private void checkRecordMonitor(String monitorCode) {
//		if(!MesureRecordModel.getInstance().checkMonitorRecordCount(monitorCode)) 
//			return;
		Log.warn("录音平台出现异常，向平台监控程序发送重启命令，monitorCode=" + monitorCode);
		ARSFToolkit.sendEvent(ProcessConstants.JMS_SEND_RECORD_MONITOR_MSG_TOPIC, monitorCode);
	}
	/**
	 * 构建录音
	 * @param data
	 * @param save_path
	 * @param fileName
	 * @return
	 * @throws RecordingException
	 */
	private VoiceStream recordDispose(CollectData data, String save_path, String fileName) throws RecordingException {
		VoiceStream voice = stream.recordStream(data.getCollectTask(), getUrl(data), 0, save_path, fileName);//status 0:开启 1：关闭
		return voice;
	}
	
	private String getRecordFileName(CollectData data) {
		String file_name = data.getCollectTask().getBusTask().getMonitor_code() 
				+ "_" + data.getCollectTask().getBusTask().getFreq()
				+ "_" + getStationName(data)
				+ "_" + getCurrentTime() + ".wav";
		return file_name;
	}
	
	private String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(new Date()).toString();
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
		Log.debug("[录音服务]重定向启动关闭当前录音的URL任务，URL=" + url + "," + getLogContent(task));
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
		return RecordingAssistant.getURL(task,fileName);
	}
	
	private String getEvalURL(CollectTask task) {
		return RecordingAssistant.getSaveFilePath(task,fileName);
	}
	
	private String getSaveURL(CollectTask task) {
		return RecordingAssistant.getSaveFilePath(task,"");
	}
	
	private String getLogContent(CollectTask task) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("TaskID=" + task.getBusTask().getTask_id() + ",");
		buffer.append("TaskFreq=" + task.getBusTask().getFreq() + ",");
		buffer.append("MonitorID=" + task.getBusTask().getMonitor_id() + ",");
		buffer.append("MonitorCode=" + task.getBusTask().getMonitor_code()+ ",");
		return buffer.toString();
	}
	
	private int getStatus(CollectData data) {
		return data.getCollectTask().getBusTask().getMeasure_status();
	}
	
	private void sendTask(CollectTask task){
		task.getBusTask().setMeasure_status(2);//修改补采状态
		ARSFToolkit.sendEvent(EventServiceTopic.COLLECT_TASK_TOPIC, task);
	}
	
	private void sleep() {
		try {
			Thread.sleep(ConfigVariable.WAIT_SAFE_ASSESS_TIME*1000);
		} catch (InterruptedException e) {
			Log.debug(e+"等待智能评估打分失败");
		}
	}
	private void clear() {
		if(info != null) {
		   info = null;
		}
//		if(process != null) 
//		   process.close();
		if(data != null)
			clearData(data);
	}
	
	private void clearData(CollectData data) {
		if (data == null) 
			return;
		CollectTask task = data.getCollectTask();
		Log.debug("本次收测单元的CollectTask回收内存。CollectTaskID=" + task.getCollectTaskID());
		task = null;
		data = null;
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

