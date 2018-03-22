package com.neusoft.gbw.cp.schedule.service;

import java.util.Date;

import np.cmpnt.libs.commons.util.DateUtil;

import com.neusoft.gbw.cp.schedule.constants.ScheduleConstants;
import com.neusoft.gbw.cp.schedule.context.TimeMsgContext;
import com.neusoft.gbw.cp.schedule.utils.TimeUtil;
import com.neusoft.gbw.cp.schedule.vo.TimeRemindMsg;
import com.neusoft.gbw.cp.schedule.vo.TimeSetMsg;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.core.schedule.vo.ScheduleMsg;

public class TimeMsgDisposeProcess {
	private static class ScheduleRemindMsgHandlerHolder {
		private static final TimeMsgDisposeProcess INSTANCE = new TimeMsgDisposeProcess();
	}
	
	public static TimeMsgDisposeProcess getInstance() {
		return ScheduleRemindMsgHandlerHolder.INSTANCE;
	}
	
	public void disposeTimeSetMsg(TimeSetMsg msg) {
		//存储消息设置对象
		save(msg);
		//转换成调度表达式
		ScheduleMsg se = converScheduleExp(msg);
		//将表达式发送至调度服务
		sendSchedule(se);
	}
	
	private void save(TimeSetMsg msg) {
		TimeMsgContext.getInstance().put(msg);
	}
	
	private ScheduleMsg converScheduleExp(TimeSetMsg msg) {
		ScheduleMsg se = new ScheduleMsg();
		se.setHandlerId(ScheduleConstants.ScheduleTopic.RECIEVE_TIME_REMIND_MSG_TOPIC);
		se.setMessageCode(msg.getRemindTopic());
		se.setPeriodStartTime(msg.getRemindTime());
		if(msg.getTimeUnit() != null && msg.getTimeUnit() == "1") { //新增加逻辑 add by jiahao
			se.setTaskScheduleType("9");//9 按照小时轮巡
			se.setPlanType("9");
			se.setPeriodHourInterval(msg.getTimeinterval() + "");
			return se;
		}
		
		if(60 == msg.getTimeinterval()) {
			se.setTaskScheduleType("9");//9 按照小时轮巡
			se.setPlanType("9");
			se.setPeriodHourInterval("1");
		}else {
			se.setTaskScheduleType("8");//8 按照分钟轮巡
			se.setPlanType("8");
			se.setPeriodMinuteInterval(msg.getTimeinterval()+"");
		}
		return se;
	}
	
	private void sendSchedule(ScheduleMsg se) {
		ARSFToolkit.sendEvent(ScheduleConstants.ScheduleTopic.SCHEDULE_INCREMENTS_OBJ, se);
	}
	
	public void disposeTimeRemindMsg(String msg) {
		//查找对应消息设置
		TimeSetMsg setMsg = TimeMsgContext.getInstance().getTimeSetMsg(msg);
		if(setMsg != null) {
			//构建消息提醒对象
			TimeRemindMsg remindMsg = buildTimeRemind(setMsg);
			//发送消息
			sendBuildService(setMsg,remindMsg);
		}
	}
	
	private TimeRemindMsg buildTimeRemind(TimeSetMsg setMsg) {
		TimeRemindMsg msg = new TimeRemindMsg();
		msg.setTimeinterval(setMsg.getTimeinterval());
		String currentTime = DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm") + ":00";
		msg.setRemindtime(currentTime);
		try {
			String endTime = TimeUtil.getAfterTime(currentTime, setMsg.getTimeinterval());
			msg.setEndTime(endTime);
		} catch (Exception e) {
		}
		return msg;
	}
	
	private void sendBuildService(TimeSetMsg setMsg,TimeRemindMsg remindMsg) {
		ARSFToolkit.sendEvent(setMsg.getRemindTopic(), remindMsg);
	}
	
	public static void main(String[] args) {
		String currentTime = DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm") + ":00";
		System.out.println(currentTime);
	}
}
