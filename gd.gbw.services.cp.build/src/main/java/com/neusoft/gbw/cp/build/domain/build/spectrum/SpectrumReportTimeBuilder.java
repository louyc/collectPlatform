package com.neusoft.gbw.cp.build.domain.build.spectrum;


import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskSchedule;

public class SpectrumReportTimeBuilder {

	public static com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.CycleReportTime buildSpectrumCycleReportTime(TaskSchedule cycleTime) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.CycleReportTime time = new com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.CycleReportTime();
		String dayofWeek = cycleTime.getDayofweek();
		dayofWeek = (dayofWeek == null ||dayofWeek.equals(BuildConstants.EVERY_DAY)) ? "All" : dayofWeek;
		time.setDayOfWeek(dayofWeek);
		time.setStartTime(cycleTime.getStarttime());
		time.setEndTime(cycleTime.getEndtime());
//		time.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		time.setReportMode(BuildConstants.NO_REPORT_MODE);
		time.setExpireDays(cycleTime.getExpiredays()+"");
		return time;
	}
	
	public static com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.CycleReportTime buildSpectrumCycleReportTimeV5(TaskSchedule cycleTime) {
		com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.CycleReportTime time = new com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.CycleReportTime();
		String dayofWeek = cycleTime.getDayofweek();
		dayofWeek = (dayofWeek == null ||dayofWeek.equals(BuildConstants.EVERY_DAY)) ? "All" : dayofWeek;
		time.setDayOfWeek(dayofWeek);
		time.setStartTime(cycleTime.getStarttime());
		time.setEndTime(cycleTime.getEndtime());
//		time.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		time.setReportMode(BuildConstants.NO_REPORT_MODE);
//		time.setExpireDays(cycleTime.getExpiredays()+"");
		return time;
	}
	
	public static com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.CycleReportTime buildSpectrumCycleReportTimeV6(TaskSchedule cycleTime) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.CycleReportTime time = new com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.CycleReportTime();
		String dayofWeek = cycleTime.getDayofweek();
		dayofWeek = (dayofWeek == null ||dayofWeek.equals(BuildConstants.EVERY_DAY)) ? "All" : dayofWeek;
		time.setDayOfWeek(dayofWeek);
		time.setStartTime(cycleTime.getStarttime());
		time.setEndTime(cycleTime.getEndtime());
//		time.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		time.setReportMode(BuildConstants.NO_REPORT_MODE);
//		time.setExpireDays(cycleTime.getExpiredays()+"");
		return time;
	}
	
	public static com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.CycleReportTime buildSpectrumCycleReportTimeV7(TaskSchedule cycleTime) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.CycleReportTime time = new com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.CycleReportTime();
		String dayofWeek = cycleTime.getDayofweek();
		dayofWeek = (dayofWeek == null ||dayofWeek.equals(BuildConstants.EVERY_DAY)) ? "All" : dayofWeek;
		time.setDayOfWeek(dayofWeek);
		time.setStartTime(cycleTime.getStarttime());
		time.setEndTime(cycleTime.getEndtime());
//		time.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		time.setReportMode(BuildConstants.NO_REPORT_MODE);
//		time.setExpireDays(cycleTime.getExpiredays()+"");
		return time;
	}
	
	public static com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SingleReportTime buildSpectrumSingleReportTime(TaskSchedule singleTime) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SingleReportTime time = new com.neusoft.gbw.cp.conver.v8.protocol.vo.spectrum.SingleReportTime();
//		time.setReportInterval(getReportInterval());
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
	
	public static com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SingleReportTime buildSpectrumSingleReportTimeV5(TaskSchedule singleTime) {
		com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SingleReportTime time = new com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum.SingleReportTime();
//		time.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		time.setReportMode(BuildConstants.NO_REPORT_MODE);
//		time.setExpireDays(singleTime.getExpiredays()+"");
		time.setStartDateTime(singleTime.getStarttime());
		time.setEndDateTime(singleTime.getEndtime());
		return time;
	}
	
	public static com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SingleReportTime buildSpectrumSingleReportTimeV6(TaskSchedule singleTime) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SingleReportTime time = new com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum.SingleReportTime();
//		time.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		time.setReportMode(BuildConstants.NO_REPORT_MODE);
//		time.setExpireDays(singleTime.getExpiredays()+"");
		time.setStartDateTime(singleTime.getStarttime());
		time.setEndDateTime(singleTime.getEndtime());
		return time;
	}
	
	public static com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SingleReportTime buildSpectrumSingleReportTimeV7(TaskSchedule singleTime) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SingleReportTime time = new com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum.SingleReportTime();
//		time.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		time.setReportMode(BuildConstants.NO_REPORT_MODE);
//		time.setExpireDays(singleTime.getExpiredays()+"");
		time.setStartDateTime(singleTime.getStarttime());
		time.setEndDateTime(singleTime.getEndtime());
		return time;
	}
}
