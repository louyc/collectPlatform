package com.neusoft.gbw.cp.measure.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.measure.IMeasureSplit;
import com.neusoft.gbw.cp.measure.MeasureResult;
import com.neusoft.gbw.cp.measure.MeasureTask;
import com.neusoft.gbw.cp.measure.vo.MeaSplitData;
import com.neusoft.gbw.cp.measure.vo.MeasureGroup;

public class MeasureSplitProcessor implements IMeasureSplit {
	
	private Map<String, MeasureGroup> groupMap = null;
	private MeaResultSort sort = new MeaResultSort();

	@Override
	public void setMeasureTask(List<MeasureTask> measureList) {
		groupMap = new LinkedHashMap<String, MeasureGroup>();
		MeasureGroup group = null;
		
		for(MeasureTask task : measureList) {
			String groupKey = task.getGroupKey();
			if (groupMap.containsKey(groupKey)) {
				group = groupMap.get(groupKey);
			} else {
				group = new MeasureGroup();
			}
			
			if (task.getAttr().getOverhaul() == 1)
				group.addOverhaulList(new MeaSplitData(task));
			else
				group.addTaskList(new MeaSplitData(task));
			
			groupMap.put(groupKey, group);
		}
	}

	@Override
	public List<MeasureResult> getSplitResult(String startTime, String endTime) throws Exception {
		List<MeasureResult> resultList = new ArrayList<MeasureResult>();
		List<MeasureResult> tempList = null;
		for(MeasureGroup group : groupMap.values()) {
			tempList = getGroupResult(startTime, endTime, group);
			Collections.sort(tempList, sort);
			resultList.addAll(tempList);
		}
		
		return resultList;
	}
	
	private List<MeasureResult> getGroupResult(String startTime, String endTime, MeasureGroup group) throws Exception {
		//ʵ��ʱ����˸�ʼ�ͽ���ʱ��
		List<MeaSplitData> tempTaskList = filterDataByTime(startTime, endTime, group.getTaskList());
		//����ʱ����˸�ʼ�ͽ���ʱ��
		List<MeaSplitData> tempOverhaulList = filterDataByTime(startTime, endTime, group.getOverhaulList());
		//�ٳ���޵�ʱ���
		tempTaskList = splitOverTime(tempTaskList, tempOverhaulList);
		//����ղⵥԪ
		List<MeasureResult> resultList = splitMeasureUnit(startTime, endTime, tempTaskList);
		
		return resultList;
	}
	
	private List<MeasureResult> splitMeasureUnit(String startTime, String endTime, List<MeaSplitData> tempTaskList) throws Exception {
		List<MeasureResult> resultList = new ArrayList<MeasureResult>();
		
		MeasureResult result = null;
		List<String> list = null;
		String start = null, end = null;
		for(MeaSplitData data : tempTaskList) {
			start = data.getStartTime();
			end = data.getEndTime();
			if (start.equals(end))
				continue;
			
			int unit = data.getTask().getMeasureUnitTime();
			if (TimeSplitUtils.isSpanDay(startTime, endTime)) {
				start = TimeSplitUtils.getDefaultTime(startTime.split(" ")[0]);
				end = TimeSplitUtils.getAfterTimeByDay(TimeSplitUtils.getDefaultTime(endTime.split(" ")[0]), 1);
			} else {
				start = TimeSplitUtils.getDefaultTime(startTime.split(" ")[0]);
				end = TimeSplitUtils.getAfterTimeByDay(start, 1);
			}
			
			list = TimeSplitUtils.getTimeSepList(start, end, unit);
			
			for(String time : list) {
				String sTime = data.getStartTime();
				String eTime = data.getEndTime();
				String unitEndTime = TimeSplitUtils.getAfterTimeByMin(time, unit);
				if (TimeSplitUtils.isScopeTime(sTime, eTime, time, unitEndTime) > 0) {
					result = new MeasureResult();
					result.setTask(data.getTask());
					result.setStartMTime(time);
					result.setEndMTime(unitEndTime);
					resultList.add(result);
				}
			}
		}
		
		Iterator<MeasureResult> iter = resultList.iterator();
		while(iter.hasNext()) {
			MeasureResult element = iter.next();
			if (TimeSplitUtils.isScopeTime(startTime, endTime, element.getStartMTime(), element.getEndMTime()) == 0) {
				iter.remove();
			}
		}
		
		return resultList;
	}
	
	private List<MeaSplitData> splitOverTime(List<MeaSplitData> tempTaskList, List<MeaSplitData> tempOverhaulList) {
		List<MeaSplitData> tempList = new ArrayList<MeaSplitData>();
		//�ٳ���޵�ʱ���
		Iterator<MeaSplitData> iter = tempTaskList.iterator();
		while(iter.hasNext()) {
			MeaSplitData mTask = iter.next();
			Iterator<MeaSplitData> it = tempOverhaulList.iterator();
			while(it.hasNext()) {
				MeaSplitData overTask = it.next();
				String scheduleStartTime = mTask.getStartTime();
				String scheduleEndTime = mTask.getEndTime();
				String overStartTime = overTask.getStartTime();
				String overEndTime = overTask.getEndTime();
				
				int index = TimeSplitUtils.isScopeTime(scheduleStartTime, scheduleEndTime, overStartTime, overEndTime);
				if (index == 0)
					continue;
				if (index == 6) {
					iter.remove();
					break;
				}
				
				switch(index) {
				case 1:
					mTask.setStartTime(overEndTime);
					mTask.setEndTime(scheduleEndTime);
					break;
				case 2:
					mTask.setStartTime(overEndTime);
					mTask.setEndTime(scheduleEndTime);
					break;
				case 3:
					mTask.setStartTime(scheduleStartTime);
					mTask.setEndTime(overStartTime);
					
					MeaSplitData data = new MeaSplitData(mTask.getTask());
					data.setStartTime(overEndTime);
					data.setEndTime(scheduleEndTime);
					tempList.add(data);
					break;
				case 4:
					mTask.setStartTime(scheduleStartTime);
					mTask.setEndTime(overStartTime);
					break;
				case 5:
					mTask.setStartTime(scheduleStartTime);
					mTask.setEndTime(overStartTime);
					break;
				}
			}
		}
		
		if (!tempList.isEmpty()) {
			List<MeaSplitData> list = splitOverTime(tempList, tempOverhaulList);
			tempTaskList.addAll(list);
		}
		
		return tempTaskList;
	}
	
	private List<MeaSplitData> filterDataByTime(String startTime, String endTime, List<MeaSplitData> taskList) throws Exception {
		boolean isSpanDay = false;
		int startWeek = TimeSplitUtils.getWeekofDate(startTime);
		int endWeek = TimeSplitUtils.getWeekofDate(endTime);
		if (TimeSplitUtils.isSpanDay(startTime, endTime)) {
			isSpanDay = true;
		}
		
		List<MeaSplitData> tempTaskList = new ArrayList<MeaSplitData>();
		String scheduleStartTime = null, scheduleEndTime = null;
		for(MeaSplitData data : taskList) {
			int type = data.getTask().getAttr().getScheduleType();
			switch(type) {
			case 1:
				scheduleStartTime = data.getTask().getAttr().getStartTime();
				scheduleEndTime = data.getTask().getAttr().getEndTime();
				if (TimeSplitUtils.isScopeTime(scheduleStartTime, scheduleEndTime, startTime, endTime) > 0) {
					data.setStartTime(scheduleStartTime);
					data.setEndTime(scheduleEndTime);
					tempTaskList.add(data);
				}
				break;
			case 2:
				int dayOfWeek = data.getTask().getAttr().getDayOfWeek();
				if (dayOfWeek == -1) {
					//��ʾÿ��ֱ�ӱȽ�ʱ���
					if (isSpanDay) {
						//����
						String startSpanTime = TimeSplitUtils.getTimeByWeek(data.getTask().getAttr().getStartTime(), endTime);
						String endSpanTime = TimeSplitUtils.getTimeByWeek(data.getTask().getAttr().getEndTime(), endTime);
						if (TimeSplitUtils.isScopeTime(startSpanTime, endSpanTime, startTime, endTime) > 0) {
							data.setStartTime(startSpanTime);
							data.setEndTime(endSpanTime);
							tempTaskList.add(data);
						}
						
						scheduleStartTime = TimeSplitUtils.getTimeByWeek(data.getTask().getAttr().getStartTime(), startTime);
						scheduleEndTime = TimeSplitUtils.getTimeByWeek(data.getTask().getAttr().getEndTime(), startTime);
						if (TimeSplitUtils.isScopeTime(scheduleStartTime, scheduleEndTime, startTime, endTime) > 0) {
							data.setStartTime(scheduleStartTime);
							data.setEndTime(scheduleEndTime);
							tempTaskList.add(data);
						}
					} else {
						//������
						scheduleStartTime = TimeSplitUtils.getTimeByWeek(data.getTask().getAttr().getStartTime(), startTime);
						scheduleEndTime = TimeSplitUtils.getTimeByWeek(data.getTask().getAttr().getEndTime(), startTime);
						if (TimeSplitUtils.isScopeTime(scheduleStartTime, scheduleEndTime, startTime, endTime) > 0) {
							data.setStartTime(scheduleStartTime);
							data.setEndTime(scheduleEndTime);
							tempTaskList.add(data);
						}
					}
				}
				
				if (isSpanDay) {
					//����
					if (dayOfWeek == startWeek) {
						//��ǰ�ܼ����һ��ת������ܼ����
						scheduleStartTime = TimeSplitUtils.getTimeByWeek(data.getTask().getAttr().getStartTime(), startTime);
						scheduleEndTime = TimeSplitUtils.getTimeByWeek(data.getTask().getAttr().getEndTime(), startTime);
						if (TimeSplitUtils.isScopeTime(scheduleStartTime, scheduleEndTime, startTime, endTime) > 0) {
							data.setStartTime(scheduleStartTime);
							data.setEndTime(scheduleEndTime);
							tempTaskList.add(data);
						}
					}
					
					if (dayOfWeek == endWeek) {
						//��ǰ�ܼ���ڶ���ת������ܼ����
						String startSpanTime = TimeSplitUtils.getTimeByWeek(data.getTask().getAttr().getStartTime(), endTime);
						String endSpanTime = TimeSplitUtils.getTimeByWeek(data.getTask().getAttr().getEndTime(), endTime);
						if (TimeSplitUtils.isScopeTime(startSpanTime, endSpanTime, startTime, endTime) > 0) {
							data.setStartTime(startSpanTime);
							data.setEndTime(endSpanTime);
							tempTaskList.add(data);
						}
					}
				} else {
					//������
					if (dayOfWeek == startWeek) {
						//��ǰ�ܼ����һ���ڶ�����ܼ����
						scheduleStartTime = TimeSplitUtils.getTimeByWeek(data.getTask().getAttr().getStartTime(), startTime);
						scheduleEndTime = TimeSplitUtils.getTimeByWeek(data.getTask().getAttr().getEndTime(), startTime);
						if (TimeSplitUtils.isScopeTime(scheduleStartTime, scheduleEndTime, startTime, endTime) > 0) {
							data.setStartTime(scheduleStartTime);
							data.setEndTime(scheduleEndTime);
							tempTaskList.add(data);
						}
					}
				}
				
				break;
			}
		}
		
		return tempTaskList;
	}
}
