package com.neusoft.gbw.cp.measure;

public class ScheduleAttr {

	private int scheduleType;	//�������� 1������ʱ�䣻2��ѭ��ʱ��
	private int dayOfWeek;		//-1:ȫ������(ÿ��) 0������ 1����һ ���� 6������
	private String startTime;	//��ʼʱ��,���������ʽ����hh:mm:ss���� ���������ʽ����yyyy-mm-dd hh:mm:ss����
	private String endTime;		//��ֹʱ��,���������ʽ����hh:mm:ss���� ���������ʽ����yyyy-mm-dd hh:mm:ss����
	private int overhaul;		//�Ƿ��Ǽ���ʱ��Σ�1 �� 0 ��
	
	public int getScheduleType() {
		return scheduleType;
	}
	public void setScheduleType(int scheduleType) {
		this.scheduleType = scheduleType;
	}
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getOverhaul() {
		return overhaul;
	}
	public void setOverhaul(int overhaul) {
		this.overhaul = overhaul;
	}
}
