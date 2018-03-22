package com.neusoft.gbw.cp.process.realtime.service.stream;

import com.neusoft.gbw.cp.collect.stream.vo.StreamParam;
import com.neusoft.gbw.cp.collect.stream.vo.VoiceStream;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.realtime.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.realtime.context.TaskProcessContext;
import com.neusoft.gbw.cp.process.realtime.service.condition.ConditionPoolException;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

public class RecordVoiceStream {

	public VoiceStream recordStream(CollectTask task, String url, int status, String save_path, String fileName) {
		TaskProcessContext context = TaskProcessContext.getInstance();
		VoiceStream stream = buildVoiceStream(task, url, status, save_path, fileName);
		context.getTsp().put(stream.getInfo().getId(), stream);
		ARSFToolkit.sendEvent(EventServiceTopic.COLLECT_STREAM_REQUEST_TOPIC, stream.getInfo());
		try {
			context.getCtp().wait(stream.getInfo().getId());
		} catch (ConditionPoolException e) {
			Log.error("任务请求失败", e);
		} catch (InterruptedException e) {
			Log.error("任务请求失败", e);
		}
		VoiceStream reStream = null;
		try {
			reStream = context.getTsp().remove(stream.getInfo().getId()).getData();
		}catch(Exception e) {
			Log.error("任务请求过期超时", e);;
		}
		
		return reStream;
	}
	
	public void stopRecordStream(CollectTask task, String url, int status, String save_path, String fileName) {
		VoiceStream stream = buildVoiceStream(task, url, status, save_path, fileName);
		ARSFToolkit.sendEvent(EventServiceTopic.COLLECT_STREAM_REQUEST_TOPIC, stream.getInfo());
	}
	
	private VoiceStream buildVoiceStream(CollectTask task, String url, int status, String save_path, String fileName) {
		VoiceStream stream = new VoiceStream();
		StreamParam para = new StreamParam();
		para.setId(generateTaskId(task));
		para.setFreq(task.getBusTask().getFreq());
		para.setSrcCode(ProcessConstants.SRC_CODE);
		para.setMonitorCode(task.getBusTask().getMonitor_code());
		para.setMonVersion(task.getData().getProVersion());
		para.setSavePath(save_path);
		para.setFileName(fileName);
		para.setEquCode(task.getAttrInfo().getFirstEquCode());
		para.setRecordLength(task.getBusTask().getRecordLength());
		para.setUrl(url);
		para.setStatus(status);
		stream.setInfo(para);
		
		return stream;
	}
	
	private String generateTaskId(CollectTask task) {
		return task.getBusTask().getMonitor_code() + task.getAttrInfo().getFirstEquCode() + System.currentTimeMillis() + "";
	}
}
