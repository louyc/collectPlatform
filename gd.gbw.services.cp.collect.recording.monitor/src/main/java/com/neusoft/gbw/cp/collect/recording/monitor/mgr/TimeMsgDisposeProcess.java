package com.neusoft.gbw.cp.collect.recording.monitor.mgr;

import java.util.Date;

import np.cmpnt.libs.commons.util.DateUtil;

import com.neusoft.gbw.cp.collect.recording.monitor.comstant.ConfigVariable;
import com.neusoft.gbw.cp.collect.recording.monitor.comstant.MoniContants;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.core.schedule.vo.ScheduleMsg;

public class TimeMsgDisposeProcess {
	
	public void disposeTimeSetMsg() {
		//转换成调度表达式
		ScheduleMsg se = converScheduleExp();
		//将表达式发送至调度服务
		sendSchedule(se);
		Log.debug("向调度发送轮训提醒......");
	}
	
	private ScheduleMsg converScheduleExp() {
		ScheduleMsg se = new ScheduleMsg();
		se.setHandlerId(MoniContants.MONITOR_TIME_REMIND_MSG_TOPIC);
		se.setMessageCode(MoniContants.MONITOR_TIME_REMIND_MSG_TOPIC);
		se.setPeriodStartTime(getCurrentTime());//设定当前时间开发轮巡
		se.setTaskScheduleType("8");//8 按照分钟轮巡
		se.setPlanType("8");//
		se.setPeriodMinuteInterval(ConfigVariable.RECORD_STATUS_INSPECT_TIME + "");
		return se;
	}
	
	private void sendSchedule(ScheduleMsg se) {
		ARSFToolkit.sendEvent(MoniContants.SCHEDULE_INCREMENTS_OBJ, se);
	}
	
	private String getCurrentTime() {
		return DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
	}
	
//	public void disposeTimeRemindMsg(String msg) {
//		//查找对应消息设置
//		TimeSetMsg setMsg = TimeMsgContext.getInstance().getTimeSetMsg(msg);
//		if(setMsg != null) {
//			//构建消息提醒对象
//			TimeRemindMsg remindMsg = buildTimeRemind(setMsg);
//			//发送消息
//			sendBuildService(setMsg,remindMsg);
//		}
//	}
	
//	private TimeRemindMsg buildTimeRemind(TimeSetMsg setMsg) {
//		TimeRemindMsg msg = new TimeRemindMsg();
//		msg.setTimeinterval(setMsg.getTimeinterval());
//		String currentTime = DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
//		msg.setRemindtime(currentTime);
//		try {
//			String endTime = TimeUtil.getAfterTime(currentTime, setMsg.getTimeinterval());
//			msg.setEndTime(endTime);
//		} catch (Exception e) {
//		}
//		return msg;
//	}
//	
//	private void sendBuildService(TimeSetMsg setMsg,TimeRemindMsg remindMsg) {
//		ARSFToolkit.sendEvent(setMsg.getRemindTopic(), remindMsg);
//	}
}
