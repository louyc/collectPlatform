package com.neusoft.gbw.cp.measure.vo;

import java.util.ArrayList;
import java.util.List;

public class MeasureGroup {

	private List<MeaSplitData> taskList = null;
	private List<MeaSplitData> overhaulList = null;
	
	public MeasureGroup() {
		taskList = new ArrayList<MeaSplitData>();
		overhaulList = new ArrayList<MeaSplitData>();
	}
	
	public List<MeaSplitData> getTaskList() {
		return taskList;
	}
	public void addTaskList(MeaSplitData data) {
		if (taskList == null)
			taskList = new ArrayList<MeaSplitData>();
		this.taskList.add(data);
	}
	public List<MeaSplitData> getOverhaulList() {
		return overhaulList;
	}
	public void addOverhaulList(MeaSplitData data) {
		if (overhaulList == null)
			overhaulList = new ArrayList<MeaSplitData>();
		this.overhaulList.add(data);
	} 
}
