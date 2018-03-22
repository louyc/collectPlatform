package com.neusoft.gbw.cp.build.domain.build.spectrum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.conver.util.Time;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.CycleReportTime;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SingleReportTime;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SpectrumTask;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskSchedule;

public class SpectrumTaskBuilder {

	protected static SpectrumTask buildSpectrumTask(Task spTask) {
		String[] freq = getStartAndEndFreq(spTask);
		SpectrumTask spectrumTask = new SpectrumTask();
		spectrumTask.setTaskType(BuildConstants.SPECTRUM_TASK+"");
		spectrumTask.setBand(spTask.getTaskFreq().getBand()+"");
		spectrumTask.setStartFreq(freq[0]);
		spectrumTask.setEndFreq(freq[1]);
		spectrumTask.setStepFreq(getFreqStep(spTask));
		spectrumTask.setSleepTime(converterToTime(getSleepTime(spTask))+"");
		
		List<CycleReportTime> cycleReportTimes = new ArrayList<CycleReportTime>();
		List<SingleReportTime> singleReportTimes = new ArrayList<SingleReportTime>();
		
		List<TaskSchedule> scheduleList = spTask.getScheduleList();
		for(TaskSchedule schedule : scheduleList) {
			if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE) {
				cycleReportTimes.add(SpectrumReportTimeBuilder.buildSpectrumCycleReportTime(schedule));
				spectrumTask.setCycleReportTimes(cycleReportTimes);
			}else {
				singleReportTimes.add(SpectrumReportTimeBuilder.buildSpectrumSingleReportTime(schedule));
				spectrumTask.setSingleReportTimes(singleReportTimes);
			}
		}
		return spectrumTask;
	}
	
	protected static com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumTask buildSpectrumTaskV5(Task spTask) {
		String[] freq = getStartAndEndFreq(spTask);
		com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumTask spectrumTask = new com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SpectrumTask();
		spectrumTask.setTaskType(BuildConstants.SPECTRUM_TASK+"");
		spectrumTask.setBand(spTask.getTaskFreq().getBand()+"");
		spectrumTask.setStartFreq(freq[0]);
		spectrumTask.setEndFreq(freq[1]);
		spectrumTask.setStepFreq(getFreqStep(spTask));
		spectrumTask.setSleepTime(converterToTime(getSleepTime(spTask))+"");
		List<com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.CycleReportTime> cycleReportTimes = new ArrayList<com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.CycleReportTime>();
		List<com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SingleReportTime> singleReportTimes = new ArrayList<com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SingleReportTime>();

//		TaskSchedule schedule = spTask.getTaskSchedule();
//		if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
//			cycleReportTimes.add(SpectrumReportTimeBuilder.buildSpectrumCycleReportTimeV5(schedule));
//		else
//			singleReportTimes.add(SpectrumReportTimeBuilder.buildSpectrumSingleReportTimeV5(schedule));
//		spectrumTask.setCycleReportTimes(cycleReportTimes);
//		spectrumTask.setSingleReportTimes(singleReportTimes);
		List<TaskSchedule> scheduleList = spTask.getScheduleList();
		for(TaskSchedule schedule : scheduleList) {
			if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE) {
				cycleReportTimes.add(SpectrumReportTimeBuilder.buildSpectrumCycleReportTimeV5(schedule));
				spectrumTask.setCycleReportTimes(cycleReportTimes);
			}else {
				singleReportTimes.add(SpectrumReportTimeBuilder.buildSpectrumSingleReportTimeV5(schedule));
				spectrumTask.setSingleReportTimes(singleReportTimes);
			}
		}
		
		return spectrumTask;
	}
	
	protected static com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumTask buildSpectrumTaskV6(Task spTask) {
		String[] freq = getStartAndEndFreq(spTask);
		com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumTask spectrumTask = new com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SpectrumTask();
		spectrumTask.setTaskType(BuildConstants.SPECTRUM_TASK+"");
		spectrumTask.setBand(spTask.getTaskFreq().getBand()+"");
		spectrumTask.setStartFreq(freq[0]);
		spectrumTask.setEndFreq(freq[1]);
		spectrumTask.setStepFreq(getFreqStep(spTask));
		spectrumTask.setSleepTime(converterToTime(getSleepTime(spTask))+"");
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.CycleReportTime> cycleReportTimes = new ArrayList<com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.CycleReportTime>();
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SingleReportTime> singleReportTimes = new ArrayList<com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SingleReportTime>();
	
//		TaskSchedule schedule = spTask.getTaskSchedule();
//		if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
//			cycleReportTimes.add(SpectrumReportTimeBuilder.buildSpectrumCycleReportTimeV6(schedule));
//		else
//			singleReportTimes.add(SpectrumReportTimeBuilder.buildSpectrumSingleReportTimeV6(schedule));
//		spectrumTask.setCycleReportTimes(cycleReportTimes);
//		spectrumTask.setSingleReportTimes(singleReportTimes);
		
		List<TaskSchedule> scheduleList = spTask.getScheduleList();
		for(TaskSchedule schedule : scheduleList) {
			if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE) {
				cycleReportTimes.add(SpectrumReportTimeBuilder.buildSpectrumCycleReportTimeV6(schedule));
				spectrumTask.setCycleReportTimes(cycleReportTimes);
			}else {
				singleReportTimes.add(SpectrumReportTimeBuilder.buildSpectrumSingleReportTimeV6(schedule));
				spectrumTask.setSingleReportTimes(singleReportTimes);
			}
		}
		
		
		return spectrumTask;
	}
	
	protected static com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumTask buildSpectrumTaskV7(Task spTask) {
		String[] freq = getStartAndEndFreq(spTask);
		com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumTask spectrumTask = new com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SpectrumTask();
		spectrumTask.setTaskType(BuildConstants.SPECTRUM_TASK+"");
		spectrumTask.setBand(spTask.getTaskFreq().getBand()+"");
		spectrumTask.setStartFreq(freq[0]);
		spectrumTask.setEndFreq(freq[1]);
		spectrumTask.setStepFreq(getFreqStep(spTask));
		spectrumTask.setSleepTime(converterToTime(getSleepTime(spTask))+"");
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.CycleReportTime> cycleReportTimes = new ArrayList<com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.CycleReportTime>();
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SingleReportTime> singleReportTimes = new ArrayList<com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SingleReportTime>();

//		TaskSchedule schedule = spTask.getTaskSchedule();
//		if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE)
//			cycleReportTimes.add(SpectrumReportTimeBuilder.buildSpectrumCycleReportTimeV7(schedule));
//		else
//			singleReportTimes.add(SpectrumReportTimeBuilder.buildSpectrumSingleReportTimeV7(schedule));
//		spectrumTask.setCycleReportTimes(cycleReportTimes);
//		spectrumTask.setSingleReportTimes(singleReportTimes);
		
		List<TaskSchedule> scheduleList = spTask.getScheduleList();
		for(TaskSchedule schedule : scheduleList) {
			if (schedule.getSchedule_type() == BuildConstants.CYCLE_TYPE) {
				cycleReportTimes.add(SpectrumReportTimeBuilder.buildSpectrumCycleReportTimeV7(schedule));
				spectrumTask.setCycleReportTimes(cycleReportTimes);
			}else {
				singleReportTimes.add(SpectrumReportTimeBuilder.buildSpectrumSingleReportTimeV7(schedule));
				spectrumTask.setSingleReportTimes(singleReportTimes);
			}
		}
		return spectrumTask;
	}
	
	private static String getFreqStep(Task spTask) {
		String freqStep = BuildConstants.WAIT_TIME;
		Map<String, String> confMap = spTask.getTaskConfAttr();
		if (confMap.containsKey(BuildConstants.SpectrumTask.SPEC_FREQ_STEP))
			freqStep = confMap.get(BuildConstants.SpectrumTask.SPEC_FREQ_STEP);
		return freqStep;
	}
	
	private static String getSleepTime(Task spTask) {
		String freqStep = BuildConstants.WAIT_TIME;
		Map<String, String> confMap = spTask.getTaskConfAttr();
		if (confMap.containsKey(BuildConstants.SpectrumTask.SPEC_TIME_INTERVAL))
			freqStep = confMap.get(BuildConstants.SpectrumTask.SPEC_TIME_INTERVAL);
		return freqStep;
	}
	
	private static String[] getStartAndEndFreq(Task spTask) {
		return spTask.getTaskFreq().getFreq().split("~");
	}
	
	private static Time converterToTime(String time) {
		int i = Time.timeToint(time);
		return new Time(i);
	}
}
