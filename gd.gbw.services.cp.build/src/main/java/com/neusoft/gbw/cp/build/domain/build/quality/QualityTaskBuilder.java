package com.neusoft.gbw.cp.build.domain.build.quality;

import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.build.domain.build.ChannelBuilder;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.conver.util.Time;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskSchedule;

public class QualityTaskBuilder {

	protected static com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityTask buildQualityKpiTask(Task kTask) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityTask qualTask = new com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.QualityTask();
		qualTask.setChannel(ChannelBuilder.buildKpiChannel(kTask.getTaskFreq().getBand(), kTask.getTaskFreq().getFreq()));
		qualTask.setSleepTime(converterToTime(getQualSleepTime(kTask))+"");
//		TaskSchedule schedule = kTask.getTaskSchedule();
//		if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
//			qualTask.addCycleReportTime(QualityReportTimeBuilder.buildCycleReportTime(schedule));
//		else
//			qualTask.addSingleReportTime(QualityReportTimeBuilder.buildSingleReportTime(schedule));
//		
		List<TaskSchedule> scheduleList = kTask.getScheduleList();
		for(TaskSchedule schedule : scheduleList) {
			if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
				qualTask.addCycleReportTime(QualityReportTimeBuilder.buildCycleReportTime(schedule));
			else
				qualTask.addSingleReportTime(QualityReportTimeBuilder.buildSingleReportTime(schedule));
		}
		
		return qualTask;
	}
	
	protected static com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.QualityTask buildQualityOffsetTask(Task oTask) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.QualityTask qualTask = new com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.QualityTask();
		qualTask.setChannel(ChannelBuilder.buildOffsetChannel(oTask.getTaskFreq().getBand(), oTask.getTaskFreq().getFreq()));
		qualTask.setSleepTime(converterToTime(getOffsetSleepTime(oTask))+"");
//		TaskSchedule schedule = oTask.getTaskSchedule();
//		if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
//			qualTask.addOffsetCycleReportTime(QualityReportTimeBuilder.buildOffsetCycleReportTime(schedule));
//		else
//			qualTask.addOffsetSingleReportTime(QualityReportTimeBuilder.buildOffsetSingleReportTime(schedule));
		
		List<TaskSchedule> scheduleList = oTask.getScheduleList();
		for(TaskSchedule schedule : scheduleList) {
			if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
				qualTask.addOffsetCycleReportTime(QualityReportTimeBuilder.buildOffsetCycleReportTime(schedule));
			else
				qualTask.addOffsetSingleReportTime(QualityReportTimeBuilder.buildOffsetSingleReportTime(schedule));
		}
		
		return qualTask;
	}
	
	protected static com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityTask buildQualityKpiTaskV5(Task kTask) {
		com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityTask qualTask = new com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.QualityTask();
		qualTask.setChannel(ChannelBuilder.buildKpiChannelV5(kTask.getTaskFreq().getBand(), kTask.getTaskFreq().getFreq()));
		qualTask.setSleepTime(converterToTime(getQualSleepTime(kTask))+"");
//		TaskSchedule schedule = kTask.getTaskSchedule();
//		if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
//			qualTask.addCycleReportTime(QualityReportTimeBuilder.buildCycleReportTimeV5(schedule));
//		else
//			qualTask.addSingleReportTime(QualityReportTimeBuilder.buildSingleReportTimeV5(schedule));
//		
		List<TaskSchedule> scheduleList = kTask.getScheduleList();
		for(TaskSchedule schedule : scheduleList) {
			if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
				qualTask.addCycleReportTime(QualityReportTimeBuilder.buildCycleReportTimeV5(schedule));
			else
				qualTask.addSingleReportTime(QualityReportTimeBuilder.buildSingleReportTimeV5(schedule));
		}
		
		return qualTask;
	}
	
	protected static com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.QualityTask buildQualityOffsetTaskV5(Task oTask) {
		com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.QualityTask qualTask = new com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.QualityTask();
		qualTask.setChannel(ChannelBuilder.buildOffsetChannelV5(oTask.getTaskFreq().getBand(), oTask.getTaskFreq().getFreq()));
		qualTask.setSleepTime(converterToTime(getOffsetSleepTime(oTask))+"");
//		TaskSchedule schedule = oTask.getTaskSchedule();
//		if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
//			qualTask.addOffsetCycleReportTime(QualityReportTimeBuilder.buildOffsetCycleReportTimeV5(schedule));
//		else
//			qualTask.addOffsetSingleReportTime(QualityReportTimeBuilder.buildOffsetSingleReportTimeV5(schedule));
		
		List<TaskSchedule> scheduleList = oTask.getScheduleList();
		for(TaskSchedule schedule : scheduleList) {
			if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
				qualTask.addOffsetCycleReportTime(QualityReportTimeBuilder.buildOffsetCycleReportTimeV5(schedule));
			else
				qualTask.addOffsetSingleReportTime(QualityReportTimeBuilder.buildOffsetSingleReportTimeV5(schedule));

		}
		
		return qualTask;
	}
	
	protected static com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityTask buildQualityKpiTaskV6(Task kTask) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityTask qualTask = new com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.QualityTask();
		qualTask.setChannel(ChannelBuilder.buildKpiChannelV6(kTask.getTaskFreq().getBand(), kTask.getTaskFreq().getFreq()));
		qualTask.setSleepTime(converterToTime(getQualSleepTime(kTask))+"");
//		TaskSchedule schedule = kTask.getTaskSchedule();
//		if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
//			qualTask.addCycleReportTime(QualityReportTimeBuilder.buildCycleReportTimeV6(schedule));
//		else
//			qualTask.addSingleReportTime(QualityReportTimeBuilder.buildSingleReportTimeV6(schedule));
		
		List<TaskSchedule> scheduleList = kTask.getScheduleList();
		for(TaskSchedule schedule : scheduleList) {
			if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
				qualTask.addCycleReportTime(QualityReportTimeBuilder.buildCycleReportTimeV6(schedule));
			else
				qualTask.addSingleReportTime(QualityReportTimeBuilder.buildSingleReportTimeV6(schedule));
		}
		
		return qualTask;
	}
	
	protected static com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityTask buildQualityKpiTaskV7(Task kTask) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityTask qualTask = new com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.QualityTask();
		qualTask.setChannel(ChannelBuilder.buildKpiChannelV7(kTask.getTaskFreq().getBand(), kTask.getTaskFreq().getFreq()));
		qualTask.setSleepTime(converterToTime(getQualSleepTime(kTask))+"");
//		TaskSchedule schedule = kTask.getTaskSchedule();
//		if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
//			qualTask.addCycleReportTime(QualityReportTimeBuilder.buildCycleReportTimeV7(schedule));
//		else
//			qualTask.addSingleReportTime(QualityReportTimeBuilder.buildSingleReportTimeV7(schedule));
		
		List<TaskSchedule> scheduleList = kTask.getScheduleList();
		for(TaskSchedule schedule : scheduleList) {
			if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
				qualTask.addCycleReportTime(QualityReportTimeBuilder.buildCycleReportTimeV7(schedule));
			else
				qualTask.addSingleReportTime(QualityReportTimeBuilder.buildSingleReportTimeV7(schedule));
		}
		
		return qualTask;
	}
	
	protected static com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.QualityTask buildQualityOffsetTaskV6(Task oTask) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.QualityTask qualTask = new com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.QualityTask();
		qualTask.setChannel(ChannelBuilder.buildOffsetChannelV6(oTask.getTaskFreq().getBand(), oTask.getTaskFreq().getFreq()));
		qualTask.setSleepTime(converterToTime(getOffsetSleepTime(oTask))+"");
//		TaskSchedule schedule = oTask.getTaskSchedule();
//		if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
//			qualTask.addOffsetCycleReportTime(QualityReportTimeBuilder.buildOffsetCycleReportTimeV6(schedule));
//		else
//			qualTask.addOffsetSingleReportTime(QualityReportTimeBuilder.buildOffsetSingleReportTimeV6(schedule));
		
		List<TaskSchedule> scheduleList = oTask.getScheduleList();
		for(TaskSchedule schedule : scheduleList) {
			if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
				qualTask.addOffsetCycleReportTime(QualityReportTimeBuilder.buildOffsetCycleReportTimeV6(schedule));
			else
				qualTask.addOffsetSingleReportTime(QualityReportTimeBuilder.buildOffsetSingleReportTimeV6(schedule));
		}
		
		return qualTask;
	}
	
	protected static com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.QualityTask buildQualityOffsetTaskV7(Task oTask) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.QualityTask qualTask = new com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.QualityTask();
		qualTask.setChannel(ChannelBuilder.buildOffsetChannelV7(oTask.getTaskFreq().getBand(), oTask.getTaskFreq().getFreq()));
		qualTask.setSleepTime(converterToTime(getOffsetSleepTime(oTask))+"");
//		TaskSchedule schedule = oTask.getTaskSchedule();
//		if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
//			qualTask.addOffsetCycleReportTime(QualityReportTimeBuilder.buildOffsetCycleReportTimeV7(schedule));
//		else
//			qualTask.addOffsetSingleReportTime(QualityReportTimeBuilder.buildOffsetSingleReportTimeV7(schedule));
//		
		List<TaskSchedule> scheduleList = oTask.getScheduleList();
		for(TaskSchedule schedule : scheduleList) {
			if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
				qualTask.addOffsetCycleReportTime(QualityReportTimeBuilder.buildOffsetCycleReportTimeV7(schedule));
			else
				qualTask.addOffsetSingleReportTime(QualityReportTimeBuilder.buildOffsetSingleReportTimeV7(schedule));
		}
		
		return qualTask;
	}
	
	private static String getOffsetSleepTime(Task kTask) {
		String sleepTime = BuildConstants.WAIT_TIME;
		Map<String, String> confMap = kTask.getTaskConfAttr();
		if (confMap.containsKey(BuildConstants.QualityTask.OFFSET_TIME_INTERVAL))
			sleepTime = confMap.get(BuildConstants.QualityTask.OFFSET_TIME_INTERVAL);
		return sleepTime;
	}
	
	private static String getQualSleepTime(Task kTask) {
		String sleepTime = BuildConstants.WAIT_TIME;
		Map<String, String> confMap = kTask.getTaskConfAttr();
		if (confMap.containsKey(BuildConstants.QualityTask.QUAL_TIME_INTERVAL))
			sleepTime = confMap.get(BuildConstants.QualityTask.QUAL_TIME_INTERVAL);
		return sleepTime;
	}
	
	private static Time converterToTime(String time) {
		int i = Time.timeToint(time);
		return new Time(i);
	}
}
