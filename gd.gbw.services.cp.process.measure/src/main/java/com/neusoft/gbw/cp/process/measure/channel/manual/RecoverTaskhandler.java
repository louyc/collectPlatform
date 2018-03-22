package com.neusoft.gbw.cp.process.measure.channel.manual;

import java.util.List;

import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.measure.context.CollectTaskModel;
import com.neusoft.gbw.cp.process.measure.vo.manual.ManualTaskInfo;
import com.neusoft.np.arsf.base.bundle.Log;

public class RecoverTaskhandler extends RecoverTaskProcessor{
	
	public void dispose(List<CollectTask> taskList) {
		ManualTaskInfo mTask = new ManualTaskInfo();
		mTask.setManualTask(taskList);
		CollectTask task = (CollectTask)taskList.get(0);
		long task_id = task.getBusTask().getTask_id();
		String key = task_id + task.getTaskType().name();
//		Timer timer = new Timer();
//		timer.schedule(new TimeOutTask(task), ProcessConstants.MANUAL_RECOVER_TASK_TIME_OUT);
//		mTask.setTimer(timer);
		mTask.setTime(System.currentTimeMillis());
		CollectTaskModel.getInstance().addManualTask(taskList,key,mTask);
		Log.debug("采集设置任务回收，size=" + taskList.size() + ",key=" + key);
	}
	
	
	
	
//	private class TimeOutTask extends TimerTask {
//		
//		private CollectTask task = null;
//		
//		public TimeOutTask(CollectTask task) {
//			this.task = task;
//		}
//
//		@Override
//		public void run() {
//			sendWebMsg(task,ProcessConstants.TASK_RECOVER_TIME_OUT,"任务回收超时");
//			CollectTaskModel.getInstance().removeTask(task.getBusTask().getTask_id() + "");
//		}
//		
//	}
	
//	private void sendWebMsg(CollectTask task,String type, String desc) {
//		TaskRecoverCPMsgDTO recover = new TaskRecoverCPMsgDTO();
//		recover.setReturnDesc(desc);
//		recover.setReturnType(type);
//		TaskDTO dto = (TaskDTO)task.getExpandObject(ExpandConstants.REPORT_CONTROL_KEY);
//		recover.setTaskDto(dto);
//		JMSDTO jmsDto = buildJMSDTO(recover);
//		ARSFToolkit.sendEvent(ProcessConstants.JMS_SEND_MSG_TOPIC, jmsDto);
//		Log.debug("任务回收完成，发送前台完成消息，taskDTO=" + dto);
//	}
	
	
//	private JMSDTO buildJMSDTO(TaskRecoverCPMsgDTO recover) {
//		JMSDTO jms = new JMSDTO();
//		jms.setObj((Serializable) recover);
//		jms.setTypeId(GBWMsgConstant.C_JMS_REAL_TASK_RECOVER_RESPONSE_MSG);
//		return jms;
//	}
}
