package com.neusoft.gbw.cp.collect.recording.service;

import com.neusoft.gbw.cp.collect.context.RecordContext;
import com.neusoft.gbw.cp.collect.recording.utils.RecordingAssistant;
import com.neusoft.gbw.cp.collect.recording.vo.StreamParam;
import com.neusoft.gbw.cp.collect.recording.vo.StreamRecord;
import com.neusoft.gbw.cp.collect.recording.vo.StreamResult;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class RecordCollectProcess extends NMService{
	
	private StreamParam param = null;
	private RecordContext channel = null;
	private IRecord handler = null;

	public RecordCollectProcess(StreamParam param) {
		this.param = param;
		channel = RecordContext.getInstance();
		handler = new V8RecordProcess();
	}

	@Override
	public void run() {
		int collectStatus = 1;
		handler.init(param);
		StreamRecord data = handler.start();
		//写入物理文件
		int status = data.getRecordStatus();
		switch(status) {
		case 0:
			//漏采，采集出现异常
			collectStatus = 3;
			break;
		case 1:
			//正常采集
			//保存文件
			String srcCode = param.getSrcCode();//任务来源（来源于那个处理服务）
			if(createFilePath() && saveRecordToDisk(param,data, srcCode)) {
				Log.debug("[录音操作]录音文件生成完成，filePath=" + param.getSavePath() + param.getFileName());
			}else {
				Log.debug("[录音操作]录音文件生成异常，filePath=" + param.getSavePath() + param.getFileName());
				collectStatus = 3;
			}
			break;
		default:
		}
		StreamResult result = getResult(collectStatus, data.getRecordLength());
		channel.putData(result);
		//清除录音线程句柄
		String monitorCode = param.getMonitorCode();
		String equCode = param.getEquCode();
		String key = monitorCode + "_" + equCode;
		channel.removeProcess(key);
		clear(data);
//		//将操作时间写入本地文件，证明录音还在进行
//		RecordUtils.writeStatus();
	}
	
	private void clear(StreamRecord data) {
		if(data != null) {
			data.getStream().clear();
			data = null;
		}
	}
	
	private StreamResult getResult(int collectStatus, int recordLength) {
		StreamResult result = new StreamResult();
		result.setSrcCode(param.getSrcCode());
		result.setCollectStatus(collectStatus);
		result.setEquCode(param.getEquCode());
		result.setFreq(param.getFreq());
		result.setId(param.getId());
		result.setMonitorCode(param.getMonitorCode());
		result.setRecordLength(recordLength);
		return result;
	}
	
	private boolean createFilePath() {
		return RecordingAssistant.createFilePath(param.getSavePath());
	}
	
	private boolean saveRecordToDisk(StreamParam param,StreamRecord stream, String srcCode) {
		String filePath = param.getSavePath();
		String fileName = param.getFileName();
		if(param.getUrl().startsWith("TTCP")){
			return RecordingAssistant.saveRecordToTTCPDisk(filePath, stream, fileName, srcCode);
		}else{
			return RecordingAssistant.saveRecordToDisk(filePath, stream, fileName, srcCode);
		}
	}
	
	public void stopRecord() {
		this.getHandler().stop();
	}
	
	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public IRecord getHandler() {
		return handler;
	}
	
//	public static void main(String[] args) {
//		String time = getCurrentTime();
//		String threadId = getCurrentThreadId();
//		String str = time + "#" + threadId;
//		FileUtil.writerFile("D:\\1.txt", str);
//	}

}
