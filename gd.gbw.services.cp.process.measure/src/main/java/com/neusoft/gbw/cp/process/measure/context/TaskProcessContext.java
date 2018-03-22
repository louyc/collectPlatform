package com.neusoft.gbw.cp.process.measure.context;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.neusoft.gbw.cp.collect.stream.vo.VoiceStream;
import com.neusoft.gbw.cp.process.measure.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.measure.channel.condition.ConditionPool;
import com.neusoft.gbw.cp.process.measure.channel.condition.TimeoutDataPool;
import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.service.config.Configuration;

public class TaskProcessContext {
	
	private static class TaskProcessContextHolder {
		private static final TaskProcessContext INSTANCE = new TaskProcessContext();
	}
	
	private TaskProcessContext(){
		taskProcessMap = new ConcurrentHashMap<String, ITaskProcess>();
		init();
	}
	
	public static TaskProcessContext getInstance() {
		return TaskProcessContextHolder.INSTANCE;
	}
	
	private void init() {
		ctp = new ConditionPool();
		ctp.init();
		tsp = new TimeoutDataPool<VoiceStream>();
		tsp.init();
		tsp.start();
	}

	//key:realtimeType、ManualType
	private Map<String, ITaskProcess> taskProcessMap = null;
	
	private ConditionPool ctp;

	private TimeoutDataPool<VoiceStream> tsp;
	
	public void loadRealtimeProcess() {
		Configuration config = ARSFToolkit.getConfiguration();
		Map<String, String> map = config.getAllBusinessProperty(ProcessConstants.MEASURE_PROCESS_SCOPE);
		
		ITaskProcess process = null;
		Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry<String, String> element = iter.next();
			try {
				process = (ITaskProcess) Class.forName(element.getValue()).newInstance();
				taskProcessMap.put(element.getKey(), process);
			} catch (ClassNotFoundException e) {
				Log.error("处理通道没有找到加载处理的指定类，Name" + element.getValue(), e);
			} catch (Exception e) {
				Log.error("处理通道构建处理类出现异常。", e);
			} 
		}
	}
	
	public ITaskProcess getTaskProcessor(String name) {
		if (taskProcessMap.containsKey(name)) {
			return taskProcessMap.get(name);
		}
		return null;
	}
	
	public ConditionPool getCtp() {
		return ctp;
	}

	public void setCtp(ConditionPool ctp) {
		this.ctp = ctp;
	}

	public TimeoutDataPool<VoiceStream> getTsp() {
		return tsp;
	}

	public void setTsp(TimeoutDataPool<VoiceStream> tsp) {
		this.tsp = tsp;
	}

}
