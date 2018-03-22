package com.neusoft.gbw.cp.manual;

import java.util.List;

import com.neusoft.gbw.cp.manual.vo.ManualSchedule;

public interface IManualSplit {
	
	void setManualSchedule(List<ManualSchedule> scheduleList);
	
	List<ManualSchedule> getSplitResult() throws Exception;
}
