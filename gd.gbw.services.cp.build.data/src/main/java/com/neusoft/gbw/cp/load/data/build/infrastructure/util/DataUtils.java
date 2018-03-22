package com.neusoft.gbw.cp.load.data.build.infrastructure.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskSchedule;
import com.neusoft.gbw.cp.load.data.build.infrastructure.constants.BuildConstants;

public class DataUtils {
	
	public static List<TaskSchedule> sort(List<TaskSchedule> scheduleList) {
		Collections.sort(scheduleList, new Comparator<TaskSchedule>() {
			@Override
			public int compare(TaskSchedule o1, TaskSchedule o2) {
				String dayofweek = o1.getDayofweek();
				String oneTime = o1.getStarttime();
				String otherTime = o2.getStarttime();
				try {
					if(dayofweek.equals(BuildConstants.EVERY_DAY)) {
							if(TimeUtils.getTimeToMilliSecond(TimeUtils.getMergeTime(oneTime)) > TimeUtils.getTimeToMilliSecond(TimeUtils.getMergeTime(otherTime))) 
								return 1;
					}else {
							if(TimeUtils.getTimeToMilliSecond(oneTime) > TimeUtils.getTimeToMilliSecond(otherTime)) 
								return 0;
					}
					} catch (Exception e) {
				}
				return -1;
			}
			
		});
		return scheduleList;
	} 

}