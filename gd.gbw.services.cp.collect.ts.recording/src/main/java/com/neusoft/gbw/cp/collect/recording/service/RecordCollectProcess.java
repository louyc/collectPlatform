package com.neusoft.gbw.cp.collect.recording.service;

import com.neusoft.gbw.cp.collect.context.RecordContext;
import com.neusoft.gbw.cp.collect.recording.service.control.RecordControlMgr;
import com.neusoft.gbw.cp.collect.recording.utils.RecordingAssistant;
import com.neusoft.gbw.cp.collect.recording.vo.StreamParam;
import com.neusoft.gbw.cp.collect.recording.vo.StreamRecord;
import com.neusoft.gbw.cp.collect.recording.vo.StreamResult;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class RecordCollectProcess extends NMService{
	
	private RecordControlMgr mgr = null;
	private StreamParam param = null;
	private RecordContext channel = null;

	public RecordCollectProcess(RecordControlMgr mgr, StreamParam param) {
		this.mgr = mgr;
		this.param = param;
		channel = RecordContext.getInstance();
	}

	@Override
	public void run() {
		int collectStatus = 1;
		IRecord record = mgr.getHandler();
		Log.debug("[录音操作]初始化录音");
		record.init(param);
		Log.debug("[录音操作]开始录音");
		StreamRecord data = record.start();  //录音结束
		Log.debug("[录音操作]录音结束");
		//写入物理文件
		int status = data.getRecordStatus();
		switch(status) {
		case 0:
			//漏采，采集出现异常
			Log.debug("[录音操作]漏采、采集出现异常"+"录音长度："+data.getRecordLength());
			collectStatus = 3;
			break;
		case 1:
			//正常采集
			//保存文件
			if(createFilePath() && saveRecordToDisk(data)) {
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
	
	private boolean saveRecordToDisk(StreamRecord stream) {
		String filePath = param.getSavePath();
		String fileName = param.getFileName();
		return RecordingAssistant.saveRecordToDisk(filePath, stream, fileName);
	}
	
	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
//	public static void main(String[] args) {
//		String time = getCurrentTime();
//		String threadId = getCurrentThreadId();
//		String str = time + "#" + threadId;
//		FileUtil.writerFile("D:\\1.txt", str);
//	}

}
