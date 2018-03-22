package com.neusoft.gbw.cp.build.domain.build.quality;


import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.conver.util.Time;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskSchedule;

public class QualityReportTimeBuilder {

	protected static com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.CycleReportTime buildCycleReportTime(TaskSchedule schedule) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.CycleReportTime time = new com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.CycleReportTime();
		String dayofWeek = schedule.getDayofweek();
		dayofWeek = (dayofWeek == null ||dayofWeek.equals(BuildConstants.EVERY_DAY)) ? "All" : dayofWeek;
		time.setDayOfWeek(dayofWeek);
		time.setStartTime(converterToTime(schedule.getStarttime())+"");
		time.setEndTime(converterToTime(schedule.getEndtime())+"");
//		time.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		time.setReportMode(BuildConstants.NO_REPORT_MODE);
		time.setExpireDays(schedule.getExpiredays()+"");
		
		return time;
	}
	
	protected static com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.CycleReportTime buildCycleReportTimeV5(TaskSchedule schedule) {
		com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.CycleReportTime time = new com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.CycleReportTime();
		String dayofWeek = schedule.getDayofweek();
		dayofWeek = (dayofWeek == null ||dayofWeek.equals(BuildConstants.EVERY_DAY)) ? "All" : dayofWeek;
		time.setDayOfWeek(dayofWeek);
		time.setStartTime(converterToTime(schedule.getStarttime())+"");
		time.setEndTime(converterToTime(schedule.getEndtime())+"");
//		time.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		time.setReportMode(BuildConstants.NO_REPORT_MODE);
//		time.setExpireDays(schedule.getExpiredays()+"");
		
		return time;
	}
	
	protected static com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.CycleReportTime buildCycleReportTimeV6(TaskSchedule schedule) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.CycleReportTime time = new com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.CycleReportTime();
		String dayofWeek = schedule.getDayofweek();
		dayofWeek = (dayofWeek == null ||dayofWeek.equals(BuildConstants.EVERY_DAY)) ? "All" : dayofWeek;
		time.setDayOfWeek(dayofWeek);
		time.setStartTime(converterToTime(schedule.getStarttime())+"");
		time.setEndTime(converterToTime(schedule.getEndtime())+"");
//		time.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		time.setReportMode(BuildConstants.NO_REPORT_MODE);
//		time.setExpireDays(schedule.getExpiredays()+"");
		
		return time;
	}
	
	protected static com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.CycleReportTime buildCycleReportTimeV7(TaskSchedule schedule) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.CycleReportTime time = new com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.CycleReportTime();
		String dayofWeek = schedule.getDayofweek();
		dayofWeek = (dayofWeek == null ||dayofWeek.equals(BuildConstants.EVERY_DAY)) ? "All" : dayofWeek;
		time.setDayOfWeek(dayofWeek);
		time.setStartTime(converterToTime(schedule.getStarttime())+"");
		time.setEndTime(converterToTime(schedule.getEndtime())+"");
//		time.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		time.setReportMode(BuildConstants.NO_REPORT_MODE);
//		time.setExpireDays(schedule.getExpiredays()+"");
		
		return time;
	}
	
	protected static com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.SingleReportTime buildSingleReportTime(TaskSchedule schedule) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.SingleReportTime time = new com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi.SingleReportTime();
//		time.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		time.setReportMode(BuildConstants.NO_REPORT_MODE);
		time.setExpireDays(schedule.getExpiredays()+"");
		time.setStartDateTime(schedule.getStarttime());
		time.setEndDateTime(schedule.getEndtime());
		
		return time;
	}
	
	protected static com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.SingleReportTime buildSingleReportTimeV5(TaskSchedule schedule) {
		com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.SingleReportTime time = new com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi.SingleReportTime();
//		time.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		time.setReportMode(BuildConstants.NO_REPORT_MODE);
//		time.setExpireDays(schedule.getExpiredays()+"");
		time.setStartDateTime(schedule.getStarttime());
		time.setEndDateTime(schedule.getEndtime());
		
		return time;
	}
	
	protected static com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.SingleReportTime buildSingleReportTimeV6(TaskSchedule schedule) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.SingleReportTime time = new com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi.SingleReportTime();
//		time.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		time.setReportMode(BuildConstants.NO_REPORT_MODE);
//		time.setExpireDays(schedule.getExpiredays()+"");
		time.setStartDateTime(schedule.getStarttime());
		time.setEndDateTime(schedule.getEndtime());
		
		return time;
	}
	
	protected static com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.SingleReportTime buildSingleReportTimeV7(TaskSchedule schedule) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.SingleReportTime time = new com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi.SingleReportTime();
//		time.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		time.setReportMode(BuildConstants.NO_REPORT_MODE);
//		time.setExpireDays(schedule.getExpiredays()+"");
		time.setStartDateTime(schedule.getStarttime());
		time.setEndDateTime(schedule.getEndtime());
		
		return time;
	}
	
	protected static com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetCycleReportTime buildOffsetCycleReportTime(TaskSchedule schedule) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetCycleReportTime offCycleTime = new com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetCycleReportTime();
		String dayofWeek = schedule.getDayofweek();
		dayofWeek = (dayofWeek == null ||dayofWeek.equals(BuildConstants.EVERY_DAY)) ? "All" : dayofWeek;
		offCycleTime.setDayOfWeek(dayofWeek);
		offCycleTime.setStartTime(converterToTime(schedule.getStarttime())+"");
		offCycleTime.setEndTime(converterToTime(schedule.getEndtime())+"");
//		offCycleTime.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		offCycleTime.setReportMode(BuildConstants.NO_REPORT_MODE);
		offCycleTime.setExpireDays(schedule.getExpiredays()+"");
		
		return offCycleTime;
	}
	
	protected static com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetCycleReportTime buildOffsetCycleReportTimeV5(TaskSchedule schedule) {
		com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetCycleReportTime offCycleTime = new com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetCycleReportTime();
		String dayofWeek = schedule.getDayofweek();
		dayofWeek = (dayofWeek == null ||dayofWeek.equals(BuildConstants.EVERY_DAY)) ? "All" : dayofWeek;
		offCycleTime.setDayOfWeek(dayofWeek);
		offCycleTime.setStartTime(converterToTime(schedule.getStarttime())+"");
		offCycleTime.setEndTime(converterToTime(schedule.getEndtime())+"");
//		offCycleTime.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		offCycleTime.setReportMode(BuildConstants.NO_REPORT_MODE);
//		offCycleTime.setExpireDays(schedule.getExpiredays()+"");
		
		return offCycleTime;
	}
	
	protected static com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.OffsetCycleReportTime buildOffsetCycleReportTimeV6(TaskSchedule schedule) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.OffsetCycleReportTime offCycleTime = new com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.OffsetCycleReportTime();
		String dayofWeek = schedule.getDayofweek();
		dayofWeek = (dayofWeek == null ||dayofWeek.equals(BuildConstants.EVERY_DAY)) ? "All" : dayofWeek;
		offCycleTime.setDayOfWeek(dayofWeek);
		offCycleTime.setStartTime(converterToTime(schedule.getStarttime())+"");
		offCycleTime.setEndTime(converterToTime(schedule.getEndtime())+"");
//		offCycleTime.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		offCycleTime.setReportMode(BuildConstants.NO_REPORT_MODE);;
//		offCycleTime.setExpireDays(schedule.getExpiredays()+"");
		
		return offCycleTime;
	}
	
	protected static com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetCycleReportTime buildOffsetCycleReportTimeV7(TaskSchedule schedule) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetCycleReportTime offCycleTime = new com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetCycleReportTime();
		String dayofWeek = schedule.getDayofweek();
		dayofWeek = (dayofWeek == null ||dayofWeek.equals(BuildConstants.EVERY_DAY)) ? "All" : dayofWeek;
		offCycleTime.setDayOfWeek(dayofWeek);
		offCycleTime.setStartTime(converterToTime(schedule.getStarttime())+"");
		offCycleTime.setEndTime(converterToTime(schedule.getEndtime())+"");
//		offCycleTime.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		offCycleTime.setReportMode(BuildConstants.NO_REPORT_MODE);
//		offCycleTime.setExpireDays(schedule.getExpiredays()+"");
		
		return offCycleTime;
	}
	
	protected static com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetSingleReportTime buildOffsetSingleReportTime(TaskSchedule schedule) {
		com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetSingleReportTime offSingleTime = new com.neusoft.gbw.cp.conver.v8.protocol.vo.offset.OffsetSingleReportTime();
//		offSingleTime.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		offSingleTime.setReportMode(BuildConstants.NO_REPORT_MODE);
		offSingleTime.setExpireDays(schedule.getExpiredays()+"");
		offSingleTime.setStartDateTime(schedule.getStarttime());
		offSingleTime.setEndDateTime(schedule.getEndtime());
		
		return offSingleTime;
	}
	
	protected static com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetSingleReportTime buildOffsetSingleReportTimeV5(TaskSchedule schedule) {
		com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetSingleReportTime offSingleTime = new com.neusoft.gbw.cp.conver.v5.protocol.vo.offset.OffsetSingleReportTime();
//		offSingleTime.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		offSingleTime.setReportMode(BuildConstants.NO_REPORT_MODE);
//		offSingleTime.setExpireDays(schedule.getExpiredays()+"");
		offSingleTime.setStartDateTime(schedule.getStarttime());
		offSingleTime.setEndDateTime(schedule.getEndtime());
		
		return offSingleTime;
	}
	
	protected static com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.OffsetSingleReportTime buildOffsetSingleReportTimeV6(TaskSchedule schedule) {
		com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.OffsetSingleReportTime offSingleTime = new com.neusoft.gbw.cp.conver.v6.protocol.vo.offset.OffsetSingleReportTime();
//		offSingleTime.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		offSingleTime.setReportMode(BuildConstants.NO_REPORT_MODE);
//		offSingleTime.setExpireDays(schedule.getExpiredays()+"");
		offSingleTime.setStartDateTime(schedule.getStarttime());
		offSingleTime.setEndDateTime(schedule.getEndtime());
		
		return offSingleTime;
	}
	
	protected static com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetSingleReportTime buildOffsetSingleReportTimeV7(TaskSchedule schedule) {
		com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetSingleReportTime offSingleTime = new com.neusoft.gbw.cp.conver.v7.protocol.vo.offset.OffsetSingleReportTime();
//		offSingleTime.setReportInterval(getReportInterval());
//		//主动上报
//		time.setReportTime(BuildConstants.WAIT_REPORT_TIME);
//		time.setReportMode(BuildConstants.WAIT_REPORT_MODE);
//		//不主动上报
		offSingleTime.setReportMode(BuildConstants.NO_REPORT_MODE);
//		offSingleTime.setExpireDays(schedule.getExpiredays()+"");
		offSingleTime.setStartDateTime(schedule.getStarttime());
		offSingleTime.setEndDateTime(schedule.getEndtime());
		
		return offSingleTime;
	}
	
	private static Time converterToTime(String time) {
		int i = Time.timeToint(time);
		return new Time(i);
	}
}
