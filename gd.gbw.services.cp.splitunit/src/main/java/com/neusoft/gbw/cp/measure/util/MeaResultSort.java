package com.neusoft.gbw.cp.measure.util;

import java.util.Comparator;

import com.neusoft.gbw.cp.measure.MeasureResult;

public class MeaResultSort implements Comparator<Object> {

	@Override
	public int compare(Object arg0, Object arg1) {
		MeasureResult result0 = (MeasureResult)arg0;
		MeasureResult result1 = (MeasureResult)arg1;
		
		return result0.getStartMTime().compareTo(result1.getStartMTime());
	}
}
