package com.neusoft.gbw.cp.load.data.build.domain.vo;

public class LeakageCollect {
	private int leakCollectStatus; //补采状态，1：重启补采，2：前台触发补采,0:不进行补采
	private int leakCollectType;   //补采类型，0：全部，3，广播，4实验
	
	public LeakageCollect(int leakCollectStatus, int leakCollectType) {
		this.leakCollectStatus = leakCollectStatus;
		this.leakCollectType = leakCollectType;
	}
	public int getLeakCollectStatus() {
		return leakCollectStatus;
	}
	public void setLeakCollectStatus(int leakCollectStatus) {
		this.leakCollectStatus = leakCollectStatus;
	}
	public int getLeakCollectType() {
		return leakCollectType;
	}
	public void setLeakCollectType(int leakCollectType) {
		this.leakCollectType = leakCollectType;
	}
	
	public void setStauts(int leakCollectStatus, int leakCollectType) {
		this.leakCollectStatus = leakCollectStatus;
		this.leakCollectType = leakCollectType;
	}
}
