package com.neusoft.gbw.cp.build.domain.build.audio;


import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskSchedule;

public class EffectReportTimeBuilder {

	public static com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamCycleReportTime buildStreamCycleReportTime(TaskSchedule cycleTime,String recordLength) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamCycleReportTime time = new com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamCycleReportTime();
		String dayofWeek = cycleTime.getDayofweek();
		dayofWeek = (dayofWeek == null ||dayofWeek.equals(BuildConstants.EVERY_DAY)) ? "All" : dayofWeek;
		time.setDayOfWeek(dayofWeek);
		time.setStartTime(cycleTime.getStarttime());
		time.setEndTime(cycleTime.getEndtime());
		time.setRecordLength(recordLength);
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		time.setReportMode(BuildConstants.NO_REPORT_MODE);
		time.setExpireDays(cycleTime.getExpiredays()+"");
		
		return time;
	}
	
	public static com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamSingleReportTime buildStreamSingleReportTime(TaskSchedule singleTime,String recordLength) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamSingleReportTime time = new com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamSingleReportTime();
		time.setRecordLength(recordLength);
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		time.setReportMode(BuildConstants.NO_REPORT_MODE);
		time.setExpireDays(singleTime.getExpiredays()+"");
		time.setStartDateTime(singleTime.getStarttime());
		time.setEndDateTime(singleTime.getEndtime());
		
		return time;
	}
	
	public static com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamCycleReportTime buildStreamCycleReportTimeV5(TaskSchedule cycleTime,String recordLength) {
		com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamCycleReportTime time = new com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamCycleReportTime();
		String dayofWeek = cycleTime.getDayofweek();
		dayofWeek = (dayofWeek == null ||dayofWeek.equals(BuildConstants.EVERY_DAY)) ? "All" : dayofWeek;
		time.setDayOfWeek(dayofWeek);
		time.setStartTime(cycleTime.getStarttime());
		time.setEndTime(cycleTime.getEndtime());
		time.setRecordLength(recordLength);
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
		time.setExpireDays(cycleTime.getExpiredays()+"");
		
		return time;
	}
	
	public static com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamCycleReportTime buildStreamCycleReportTimeV6(TaskSchedule cycleTime,String recordLength) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamCycleReportTime time = new com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamCycleReportTime();
		String dayofWeek = cycleTime.getDayofweek();
		dayofWeek = (dayofWeek == null ||dayofWeek.equals(BuildConstants.EVERY_DAY)) ? "All" : dayofWeek;
		time.setDayOfWeek(dayofWeek);
		time.setStartTime(cycleTime.getStarttime());
		time.setEndTime(cycleTime.getEndtime());
		time.setRecordLength(recordLength);
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
		time.setExpireDays(cycleTime.getExpiredays()+"");
		
		return time;
	}
	
	public static com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamCycleReportTime buildStreamCycleReportTimeV7(TaskSchedule cycleTime,String recordLength) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamCycleReportTime time = new com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamCycleReportTime();
		String dayofWeek = cycleTime.getDayofweek();
		dayofWeek = (dayofWeek == null ||dayofWeek.equals(BuildConstants.EVERY_DAY)) ? "All" : dayofWeek;
		time.setDayOfWeek(dayofWeek);
		time.setStartTime(cycleTime.getStarttime());
		time.setEndTime(cycleTime.getEndtime());
		time.setRecordLength(recordLength);
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
		time.setExpireDays(cycleTime.getExpiredays()+"");
		
		return time;
	}
	
	public static com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamSingleReportTime buildStreamSingleReportTimeV5(TaskSchedule singleTime,String recordLength) {
		com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamSingleReportTime time = new com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamSingleReportTime();
		time.setRecordLength(recordLength);
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
		time.setExpireDays(singleTime.getExpiredays()+"");
		time.setStartDateTime(singleTime.getStarttime());
		time.setEndDateTime(singleTime.getEndtime());
		
		return time;
	}
	
	public static com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamSingleReportTime buildStreamSingleReportTimeV6(TaskSchedule singleTime,String recordLength) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamSingleReportTime time = new com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamSingleReportTime();
		time.setRecordLength(recordLength);
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
		time.setExpireDays(singleTime.getExpiredays()+"");
		time.setStartDateTime(singleTime.getStarttime());
		time.setEndDateTime(singleTime.getEndtime());
		
		return time;
	}
	
	public static com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamSingleReportTime buildStreamSingleReportTimeV7(TaskSchedule singleTime,String recordLength) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamSingleReportTime time = new com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamSingleReportTime();
		time.setRecordLength(recordLength);
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
		time.setExpireDays(singleTime.getExpiredays()+"");
		time.setStartDateTime(singleTime.getStarttime());
		time.setEndDateTime(singleTime.getEndtime());
		
		return time;
	}
}
