package com.neusoft.gbw.cp.measure;

public class MeasureTask {

	private String groupKey;		//����������ı�ʶ��ÿ�����ֻ��������
	private Object busObj;			//ʵ��ҵ������
	private ScheduleAttr attr;		//����������ԣ����ڡ��ƻ���
	private int measureUnitTime;	//�ղⵥԪʱ����
	
	public String getGroupKey() {
		return groupKey;
	}
	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}
	public Object getBusObj() {
		return busObj;
	}
	public void setBusObj(Object busObj) {
		this.busObj = busObj;
	}
	public ScheduleAttr getAttr() {
		return attr;
	}
	public void setAttr(ScheduleAttr attr) {
		this.attr = attr;
	}
	public int getMeasureUnitTime() {
		return measureUnitTime;
	}
	public void setMeasureUnitTime(int measureUnitTime) {
		this.measureUnitTime = measureUnitTime;
	}
}
