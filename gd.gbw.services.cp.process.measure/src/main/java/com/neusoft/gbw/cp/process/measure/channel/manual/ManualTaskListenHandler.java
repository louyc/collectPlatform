package com.neusoft.gbw.cp.process.measure.channel.manual;

import java.io.Serializable;
import java.util.Map;

import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.measure.context.CollectTaskModel;
import com.neusoft.gbw.cp.process.measure.vo.manual.ManualTaskInfo;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskDTO;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskRecoverCPMsgDTO;
import com.neusoft.gbw.infrastructure.msgcenter.intf.JMSDTO;
import com.neusoft.gbw.infrastructure.msgcenter.srv.GBWMsgConstant;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class ManualTaskListenHandler extends NMService {
	
	private CollectTaskModel model = CollectTaskModel.getInstance();
	private static final String success = "任务回收成功";
	private static final String fauil = "任务回收失败";
	private static final String timeout = "任务回收超时";
	
	@Override
	public void run() {
		Map<String , ManualTaskInfo> taskMap = null;
		while(isThreadRunning()) {
			taskMap = model.getManualTaskMap();
			if(taskMap.isEmpty()) {
				sleep(1000);
				continue;
			}
			for(ManualTaskInfo info : taskMap.values()) {
				if(info == null)
					continue;
				CollectTask task = (CollectTask)info.getManualTask().get(0);
				//判断时间是否超过五分钟，超过则删除该任务计时
				if(compareTime(info.getTime())) {
					sendWebMsg(task,ProcessConstants.TASK_RECOVER_TIME_OUT,timeout);
					model.removeTask(task.getBusTask().getTask_id() + task.getTaskType().name());
					break;
				}

				if(compareSize(info)) {
					if(info.isTaskStatus() && info.getNullSize()!=info.getManualTask().size())
						sendWebMsg(task,ProcessConstants.TASK_RECOVER_SUCCESS,success);
					else
						sendWebMsg(task,ProcessConstants.TASK_RECOVER_FAUIL,fauil);
					 
					String key = task.getBusTask().getTask_id() + task.getTaskType().name(); 
					Log.debug("设置任务回收完成， key=" + key);
					model.removeTask(key);
					break;
				}
			}
			
//			sleep(3000);
		}
	}
	
	private boolean compareTime(long time) {
		boolean timeOut = false;
		long newTime = System.currentTimeMillis();
		if(newTime - time > ProcessConstants.MANUAL_RECOVER_TASK_TIME_OUT)
			timeOut = true;
		return timeOut;
	}
	
	private boolean compareSize(ManualTaskInfo info) {
		boolean flag = false;
		int taskSize = info.getTaskSize();
		int recoverSize = info.getRecoverSize();
		if(recoverSize == taskSize)
			flag = true;
		return flag;
	}
	
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
	
	private void sendWebMsg(CollectTask task,String type, String taskStatus) {
		TaskRecoverCPMsgDTO recover = new TaskRecoverCPMsgDTO();
		recover.setReturnDesc(createTaskMsg(task, type, taskStatus));
		recover.setReturnType(type);
		TaskDTO dto = (TaskDTO)task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
		recover.setTaskDto(dto);
		JMSDTO jmsDto = buildJMSDTO(recover);
		ARSFToolkit.sendEvent(ProcessConstants.JMS_SEND_MSG_TOPIC, jmsDto);
		Log.debug("任务回收完成，发送前台完成消息，" + getLogContent(task) );
	}
	
	private String getLogContent(CollectTask task) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("TaskID=" + task.getBusTask().getTask_id() + ",");
		buffer.append("TaskFreq=" + task.getBusTask().getFreq() + ",");
		buffer.append("MonitorID=" + task.getBusTask().getMonitor_id() + ",");
		buffer.append("MonitorCode=" + task.getBusTask().getMonitor_code()+ ",");
		buffer.append("taskType=" + task.getData().getType()+ ",");
		return buffer.toString();
	}
	
	private JMSDTO buildJMSDTO(TaskRecoverCPMsgDTO recover) {
		JMSDTO jms = new JMSDTO();
		jms.setObj((Serializable) recover);
		jms.setTypeId(GBWMsgConstant.C_JMS_REAL_TASK_RECOVER_RESPONSE_MSG);
		return jms;
	}
	
	public String createTaskMsg(CollectTask task, String taskStatus, String taskInfo) {
		StringBuffer descStr = new StringBuffer();
		descStr.append(taskInfo);
		descStr.append(";站点：" + task.getBusTask().getMonitor_code() + ",");
		descStr.append("任务名称：" + task.getBusTask().getTask_name());
		return descStr.toString();
	}
	
	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
