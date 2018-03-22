package com.neusoft.gbw.cp.manual.uitl;

import java.util.Comparator;

import com.neusoft.gbw.cp.manual.vo.ManualSchedule;

public class ManualResultSort implements Comparator<Object>{

	@Override
	public int compare(Object o1, Object o2) {
		ManualSchedule schedule1 = (ManualSchedule)o1;
		ManualSchedule schedule2 = (ManualSchedule)o2;
		return schedule1.getStarttime().compareTo(schedule2.getStarttime());
	}
}
