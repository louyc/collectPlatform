package com.neusoft.gbw.cp.manual.uitl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.manual.IManualSplit;
import com.neusoft.gbw.cp.manual.vo.ManualSchedule;
import com.neusoft.gbw.cp.measure.util.TimeSplitUtils;


/**
 * @author jh
 *	//1、首先拆分跨天
	//2、查分schedule中的每天，将每天拆成周一、周二、周三、周四
	//3、分类，将播出/检修的对象拆分
	//4、比对，去掉检修
	//5、组成新的对象
 */
public class ManualSpitProcessor  implements IManualSplit{
	
	private static final String END_TIME = "23:59:59";
	private static final String START_TIME = "00:00:00";
	private static final String DAY_OF_WEEK = "-1";
	private static final int[] weekDays = {0,1,2,3,4,5,6};
	private Map<String, List<ManualSchedule>> broadMap = null;
	private Map<String, List<ManualSchedule>> overhaulMap = null;
	private ManualResultSort sort = new ManualResultSort();

	@Override
	public void setManualSchedule(List<ManualSchedule> scheduleList) {
		broadMap = new HashMap<String, List<ManualSchedule>>();
		overhaulMap = new HashMap<String, List<ManualSchedule>>();
		//1、首先拆分跨天
		List<ManualSchedule> tempSchedule = splitDay(scheduleList);
		//2、拆分schedule中的每天，将每天拆成周一、周二、周三、周四
		List<ManualSchedule> newSchedule = splitEveryDay(tempSchedule);
		//3、分类，将播出/检修的对象拆分
		List<ManualSchedule> manualList = null;
		List<ManualSchedule> overList = null;
		for(ManualSchedule schedule : newSchedule) {
			int is_overhaul = schedule.getIs_overhaul();
			String day = schedule.getDayofweek();
			if(1 == is_overhaul) {
				if(overhaulMap.containsKey(day))
					overhaulMap.get(day).add(schedule);
				else {
					overList = new ArrayList<ManualSchedule>();
					overList.add(schedule);
					overhaulMap.put(day, overList);
				}	
			}else {
				if(broadMap.containsKey(day))
					broadMap.get(day).add(schedule);
				else {
					manualList = new ArrayList<ManualSchedule>();
					manualList.add(schedule);
					broadMap.put(day, manualList);
				}	
			}
		}
	}
	
	private List<ManualSchedule> splitDay(List<ManualSchedule> scheduleList) {
		String splitWeek = null;
		
		List<ManualSchedule> tempScheduleList = new java.util.ArrayList<ManualSchedule>();
		//查分任务调度跨天的情况。
		for(ManualSchedule one : scheduleList) {
//			//临时，目前检修的首先不做处理
//			int is_overhaul = one.getIs_overhaul();
//			if(1 == is_overhaul)
//				continue;
			//去掉有效期不是有效的任务
			if(!compareTime(one.getValid_start_time(), one.getValid_end_time())) 
				continue;
			
			int schedule_type = one.getSchedule_type();
			if(schedule_type == 1) {//计划时间不做操作
				tempScheduleList.add(one);
				continue;
			}
			
			String start = one.getStarttime();
			String end = one.getEndtime();
			//判断是否跨天,不跨天
			if (!TimeUtils.isCrossDay(start, end)){
				tempScheduleList.add(one);
				continue;
			}
			//跨天
			String dayofweek = one.getDayofweek();
			if(!dayofweek.equals(DAY_OF_WEEK))
				splitWeek = Integer.parseInt(dayofweek) + 1 + "";
			else 
				splitWeek = dayofweek;
			
			 ManualSchedule schedule1 =  createSchedule(one, start, END_TIME, dayofweek);
			 tempScheduleList.add(schedule1);
			 //如果开始时间等于结束时间
			 if(end.equals(START_TIME))
				 continue;
			
			 ManualSchedule schedule2 =  createSchedule(one, START_TIME, end , splitWeek);
			 tempScheduleList.add(schedule2);
		}
		return tempScheduleList;
	}
	
	private boolean compareTime(String starttime, String endtime) {
		boolean flag = false;
		try {
			if(starttime == null || endtime == null)
				return true;
			long start = TimeUtils.getTimeToMilliSecond(starttime);
			long end = TimeUtils.getTimeToMilliSecond(endtime);
			long now = System.currentTimeMillis();
			if(start < now && end > now)
				flag = true;
		} catch (Exception e) {
		}
		return flag;
	}
	
	private List<ManualSchedule> splitEveryDay(List<ManualSchedule> tempScheduleList) {
		List<ManualSchedule> newScheduleList = new java.util.ArrayList<ManualSchedule>();
		//查分任务调度跨天的情况。
		for(ManualSchedule one : tempScheduleList) {
			int schedule_type = one.getSchedule_type();
			if(schedule_type == 1) {//计划时间不做操作
				newScheduleList.add(one);
				continue;
			}
			
			String start = one.getStarttime();
			String end = one.getEndtime();
			String dayofweek = one.getDayofweek();
			if(!dayofweek.equals(DAY_OF_WEEK)) {//不是每天的
				newScheduleList.add(one);
				continue;
			}
			//是每天的，将每天的拆成周一:1、周二:2、.....周六：6、周日：0
			for(int day=0;day<=weekDays.length - 1;day++) {
				ManualSchedule schedule1 =  createSchedule(one, start, end, weekDays[day] + "");
				newScheduleList.add(schedule1);
			}
		}
		return newScheduleList;
	}
	
	private ManualSchedule createSchedule(ManualSchedule one, String start, String end, String dayofweek) {
		ManualSchedule schedule = new ManualSchedule();
		schedule.setDayofweek(dayofweek);
		schedule.setEndtime(end);
		schedule.setExpiredays(one.getExpiredays());
		schedule.setIs_overhaul(one.getIs_overhaul());
		schedule.setReportmode(one.getReportmode());
		schedule.setRunplan_id(one.getRunplan_id());
		schedule.setSchedule_type(one.getSchedule_type());
		schedule.setStarttime(start);
		schedule.setSync_status(one.getSync_status());
		schedule.setTaskfreq_id(one.getTaskfreq_id());
		schedule.setTime_id(one.getTime_id());
		return schedule;
	}

	@Override
	public List<ManualSchedule> getSplitResult() throws Exception {
		List<ManualSchedule> scheduleList = new ArrayList<ManualSchedule>();
		//4、比对，去掉检修
		//5、组成新的对象
		List<ManualSchedule> manualList = null;
		List<ManualSchedule> overList = null;
		for(String day : broadMap.keySet()) {
			if(!overhaulMap.containsKey(day)) {
				scheduleList.addAll(broadMap.get(day));
				continue;
			}
			overList = overhaulMap.get(day);
			manualList = broadMap.get(day);
			scheduleList.addAll(splitOverTime(manualList, overList));
		}
		Collections.sort(scheduleList, sort);
		return scheduleList;
	}
	
	private List<ManualSchedule> splitOverTime(List<ManualSchedule> manualList, List<ManualSchedule> overList) {
		List<ManualSchedule> tempList = new ArrayList<ManualSchedule>();
		Iterator<ManualSchedule> iter = manualList.iterator();
		while(iter.hasNext()) {
			ManualSchedule mTask = iter.next();
			Iterator<ManualSchedule> it = overList.iterator();
			while(it.hasNext()) {
				ManualSchedule overTask = it.next();
				String scheduleStartTime = mTask.getStarttime();
				String scheduleEndTime = mTask.getEndtime();
				String overStartTime = overTask.getStarttime();
				String overEndTime = overTask.getEndtime();
				
				int index = TimeSplitUtils.isScopeHHTime(scheduleStartTime, scheduleEndTime, overStartTime, overEndTime);
				if (index == 0) {
					continue;
				}
				if (index == 6) {
					iter.remove();
					break;
				}
				
				switch(index) {
				case 1:
					mTask.setStarttime(overEndTime);
					mTask.setEndtime(scheduleEndTime);
					break;
				case 2:
					mTask.setStarttime(overEndTime);
					mTask.setEndtime(scheduleEndTime);
					break;
				case 3:
					mTask.setStarttime(scheduleStartTime);
					mTask.setEndtime(overStartTime);
					tempList.add(createSchedule(mTask, overEndTime, scheduleEndTime, mTask.getDayofweek()));
					break;
				case 4:
					mTask.setStarttime(scheduleStartTime);
					mTask.setEndtime(overStartTime);
					break;
				case 5:
					mTask.setStarttime(scheduleStartTime);
					mTask.setEndtime(overStartTime);
					break;
				}
			}
		}
		if (!tempList.isEmpty()) {
			List<ManualSchedule> list = splitOverTime(tempList, overList);
			manualList.addAll(list);
		}
		return manualList;
	}
	
}
