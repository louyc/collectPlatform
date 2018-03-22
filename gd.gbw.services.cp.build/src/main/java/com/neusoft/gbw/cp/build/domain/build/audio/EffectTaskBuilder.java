package com.neusoft.gbw.cp.build.domain.build.audio;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.build.domain.build.ChannelBuilder;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.conver.util.Time;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskSchedule;

public class EffectTaskBuilder {

	public static com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamTask buildStreamTask(Task sTask) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamTask steamtask = new com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamTask();
		steamtask.setChannel(ChannelBuilder.buildAudioChannel(sTask.getTaskFreq().getBand(), sTask.getTaskFreq().getFreq()));
		steamtask.setSleepTime(converterToTime(getSleepTime(sTask))+"");
		//将taskSchedule修改成List，在list中将一个频率的多个调度化成一个小任务进行下发
		
//		TaskSchedule schedule = sTask.getTaskSchedule();
//		String recordLength = getRecordLength(sTask);
//		if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
//			steamtask.addStreamCycleReportTime(EffectReportTimeBuilder.buildStreamCycleReportTime(schedule,recordLength));
//		else
//			steamtask.addStreamSingleReportTime(EffectReportTimeBuilder.buildStreamSingleReportTime(schedule,recordLength));
		List<TaskSchedule> scheduleList = sTask.getScheduleList();
		for(TaskSchedule schedule : scheduleList) {
			String recordLength = getRecordLength(sTask);
			if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
				steamtask.addStreamCycleReportTime(EffectReportTimeBuilder.buildStreamCycleReportTime(schedule,recordLength));
			else
				steamtask.addStreamSingleReportTime(EffectReportTimeBuilder.buildStreamSingleReportTime(schedule,recordLength));
		}
		
		return steamtask;
	}
	public static com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamTask buildStreamTaskV5(Task sTask) {
		com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamTask steamtask = new com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamTask();
		steamtask.setChannel(ChannelBuilder.buildAudioChannelV5(sTask.getTaskFreq().getBand(), sTask.getTaskFreq().getFreq()));
		steamtask.setSleepTime(converterToTime(getSleepTime(sTask))+"");
//		TaskSchedule schedule = sTask.getTaskSchedule();
//		String recordLength = getRecordLength(sTask);
//		if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
//			steamtask.addStreamCycleReportTime(EffectReportTimeBuilder.buildStreamCycleReportTimeV5(schedule,recordLength));
//		else
//			steamtask.addStreamSingleReportTime(EffectReportTimeBuilder.buildStreamSingleReportTimeV5(schedule,recordLength));
		List<TaskSchedule> scheduleList = sTask.getScheduleList();
		for(TaskSchedule schedule : scheduleList) {
			String recordLength = getRecordLength(sTask);
			if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
				steamtask.addStreamCycleReportTime(EffectReportTimeBuilder.buildStreamCycleReportTimeV5(schedule,recordLength));
			else
				steamtask.addStreamSingleReportTime(EffectReportTimeBuilder.buildStreamSingleReportTimeV5(schedule,recordLength));
		}
		
		
		return steamtask;
	}
	
	public static com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamTask buildStreamTaskV6(Task sTask) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamTask steamtask = new com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamTask();
		steamtask.setChannel(ChannelBuilder.buildAudioChannelV6(sTask.getTaskFreq().getBand(), sTask.getTaskFreq().getFreq()));
		steamtask.setSleepTime(converterToTime(getSleepTime(sTask))+"");
//		TaskSchedule schedule = sTask.getTaskSchedule();
//		String recordLength = getRecordLength(sTask);
//		if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
//			steamtask.addStreamCycleReportTime(EffectReportTimeBuilder.buildStreamCycleReportTimeV6(schedule,recordLength));
//		else
//			steamtask.addStreamSingleReportTime(EffectReportTimeBuilder.buildStreamSingleReportTimeV6(schedule,recordLength));
		List<TaskSchedule> scheduleList = sTask.getScheduleList();
		for(TaskSchedule schedule : scheduleList) {
			String recordLength = getRecordLength(sTask);
			if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
				steamtask.addStreamCycleReportTime(EffectReportTimeBuilder.buildStreamCycleReportTimeV6(schedule,recordLength));
			else
				steamtask.addStreamSingleReportTime(EffectReportTimeBuilder.buildStreamSingleReportTimeV6(schedule,recordLength));
		}
			
		return steamtask;
	}
	
	public static com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamTask buildStreamTaskV7(Task sTask) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamTask steamtask = new com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamTask();
		steamtask.setChannel(ChannelBuilder.buildAudioChannelV7(sTask.getTaskFreq().getBand(), sTask.getTaskFreq().getFreq()));
		steamtask.setSleepTime(converterToTime(getSleepTime(sTask))+"");
//		TaskSchedule schedule = sTask.getTaskSchedule();
//		String recordLength = getRecordLength(sTask);
//		if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
//			steamtask.addStreamCycleReportTime(EffectReportTimeBuilder.buildStreamCycleReportTimeV7(schedule,recordLength));
//		else
//			steamtask.addStreamSingleReportTime(EffectReportTimeBuilder.buildStreamSingleReportTimeV7(schedule,recordLength));
		List<TaskSchedule> scheduleList = sTask.getScheduleList();
		for(TaskSchedule schedule : scheduleList) {
			String recordLength = getRecordLength(sTask);
			if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
				steamtask.addStreamCycleReportTime(EffectReportTimeBuilder.buildStreamCycleReportTimeV7(schedule,recordLength));
			else
				steamtask.addStreamSingleReportTime(EffectReportTimeBuilder.buildStreamSingleReportTimeV7(schedule,recordLength));
		}
		
		return steamtask;
	}
	
	private static String getRecordLength(Task sTask) {
		String recordLength = BuildConstants.WAIT_TIME;
		Map<String, String> confMap = sTask.getTaskConfAttr();
		if (confMap.containsKey(BuildConstants.EffectTask.EFFECT_RECORDLENGTH)){
			if(!confMap.get(BuildConstants.EffectTask.EFFECT_RECORDLENGTH).contains(":")){
				recordLength =converSectoTime(Integer.parseInt(confMap.get(BuildConstants.EffectTask.EFFECT_RECORDLENGTH))*60);
			}else{
				recordLength = confMap.get(BuildConstants.EffectTask.EFFECT_RECORDLENGTH);
			}
		}
		return recordLength;
	}

	private static String getSleepTime(Task kTask) {
		String sleepTime = BuildConstants.WAIT_TIME;
		Map<String, String> confMap = kTask.getTaskConfAttr();
		if (confMap.containsKey(BuildConstants.EffectTask.EFFECT_TIME_INTERVAL))
			sleepTime = confMap.get(BuildConstants.EffectTask.EFFECT_TIME_INTERVAL);
		return sleepTime;
	}
	
	private static Time converterToTime(String time) {
		int i = Time.timeToint(time);
		return new Time(i);
	}
	
	private static String converSectoTime(int time){
		Time t = new Time(time);
		return t.getTime();
	}
}
