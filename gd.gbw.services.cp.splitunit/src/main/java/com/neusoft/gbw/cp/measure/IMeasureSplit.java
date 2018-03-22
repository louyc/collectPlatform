package com.neusoft.gbw.cp.measure;

import java.util.List;

public interface IMeasureSplit {

	void setMeasureTask(List<MeasureTask> measureList);
	
	List<MeasureResult> getSplitResult(String startTime, String endTime) throws Exception;
}
