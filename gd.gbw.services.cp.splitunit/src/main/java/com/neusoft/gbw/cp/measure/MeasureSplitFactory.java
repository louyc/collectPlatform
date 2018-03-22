package com.neusoft.gbw.cp.measure;

import com.neusoft.gbw.cp.measure.util.MeasureSplitProcessor;

public class MeasureSplitFactory {

	public static IMeasureSplit newMeasureSplit() {
		return new MeasureSplitProcessor();
	}
}
