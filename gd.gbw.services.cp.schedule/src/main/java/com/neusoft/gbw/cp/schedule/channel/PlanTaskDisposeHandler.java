package com.neusoft.gbw.cp.schedule.channel;

import java.util.Map;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.OperationType;
import com.neusoft.gbw.cp.core.collect.ScheduleFormatType;
import com.neusoft.gbw.cp.schedule.constants.ScheduleConstants;
import com.neusoft.gbw.cp.schedule.context.ScheduleTaskContext;
import com.neusoft.gbw.cp.schedule.utils.TimeUtil;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;
import com.neusoft.np.arsf.core.schedule.vo.ScheduleMsg;

public class PlanTaskDisposeHandler extends NMService {

	private PlanTaskChannel channel = null;
	private TimeUtil timeUtil;
	private ScheduleTaskContext context;
	
	public PlanTaskDisposeHandler(PlanTaskChannel channel) {
		this.channel = channel;
		this.context = ScheduleTaskContext.getInstance();
		timeUtil = new TimeUtil();
	}
	
	@Override
	public void run() {
		CollectTask task = null;
		while (isThreadRunning()) {
			try {
				task = (CollectTask)channel.take();
			} catch (InterruptedException e) {
				Log.error("接收调度任务队列数据异常", e);
				break;
			}
			OperationType type = task.getSchedule().getOperType();
			if(type == null) {
				Log.warn("任务ID=" + task.getBusTask().getTask_id() + "的操作类型为空");
				continue;
			}
			if(task.getSchedule().getTime() == null) {
				Log.debug("延时时间为空 .  任务唯一标识=" + context.getId(task));
				sendTask(type,task);
				continue;
			}
			//与当前系统时间比较，如果在系统时间之前，则直接发送
			if(!timeUtil.checkStartTime(task.getSchedule().getTime())) {
				sendTask(type,task);
				continue;
			}
			
			ScheduleMsg se = getScheduleMsg(task);
			switch (type){
			case add:
				context.syncTask(task);
				addSchedule(se);
				break;
			case update:
				context.syncTask(task);
				delSchedule(se);
				sleep();
				addSchedule(se);
				break;
			case del:
				context.removeTask(task);
				delSchedule(se);
				break;
			}
			
		}
	}
	
	private void sendTask(OperationType type,CollectTask task){
		ARSFToolkit.sendEvent(EventServiceTopic.COLLECT_TASK_TOPIC, task);
	}
	
	private ScheduleMsg getScheduleMsg(CollectTask task){
		ScheduleMsg se = new ScheduleMsg();
		ScheduleFormatType  type  = task.getSchedule().getFormat();
		String time = null;
		Map<String, String> map = null;
		switch (type) {
		case yMdHms:
			time = task.getSchedule().getTime();
			map = timeUtil.dateToMap(time);
			break;
		case Hms:
			time = task.getSchedule().getTime();
			map = timeUtil.timeToMap(time);
			break;
		case hour:
			time = task.getSchedule().getTime();
			map = timeUtil.hourToMap(time);
			break;
		case minute:
			time = task.getSchedule().getTime();
			map = timeUtil.minuteToMap(time);
			break;
		}
		se.setMessageCode(context.getId(task));
		se.setHandlerId(ScheduleConstants.ScheduleTopic.RECIEVE_DELAY_SCHEDULE_MSG_TOPIC);
		se.setPlanType("7");
		se.setTaskScheduleType("7");
		se.setPlanYear(map.get(ScheduleConstants.YEAR));
		se.setPlanMonth(map.get(ScheduleConstants.MONTH));
		se.setPlanDay(map.get(ScheduleConstants.DAY));
		se.setPlanHour(map.get(ScheduleConstants.HOUR));
		se.setPlanMinute(map.get(ScheduleConstants.MINUTE));
		se.setPlanSeconds(map.get(ScheduleConstants.SECOND));
		se.setSrcTime(time);
		return se;
	}
	
	private void addSchedule(ScheduleMsg se){
		ARSFToolkit.sendEvent(ScheduleConstants.ScheduleTopic.SCHEDULE_INCREMENTS_OBJ, se);
		
	}
	
	private void delSchedule(ScheduleMsg se){
		ARSFToolkit.sendEvent(ScheduleConstants.ScheduleTopic.SCHEDULE_REMOVE_OBJ, se);
	}
	
	private void sleep() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			//添加打印日志
			Log.debug("InterruptedException     中断异常      PlanTaskDisposeHandler ");
		}
		//添加捕捉异常
		catch(IllegalArgumentException  e){
			Log.debug("IllegalArgumentException  不合法 不合适参数     PlanTaskDisposeHandler");
		}
	}

	@Override
	public String getServiceName() {
		return serviceName;
	}

	@Override
	public void setServiceName(String arg0) {
		this.serviceName = arg0;
	}

}
