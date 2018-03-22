package com.neusoft.gbw.cp.load.data.build.domain.vo;

import com.neusoft.gbw.cp.schedule.vo.TimeRemindMsg;

public class BuildPrepareInfo {

	private PlatformBuildType platBuildType;
	private TimeRemindMsg msg;
	private Object expandObj;
	
	public PlatformBuildType getPlatBuildType() {
		return platBuildType;
	}
	public void setPlatBuildType(PlatformBuildType platBuildType) {
		this.platBuildType = platBuildType;
	}
	public TimeRemindMsg getMsg() {
		return msg;
	}
	public void setMsg(TimeRemindMsg msg) {
		this.msg = msg;
	}
	public Object getExpandObj() {
		return expandObj;
	}
	public void setExpandObj(Object expandObj) {
		this.expandObj = expandObj;
	}
}
