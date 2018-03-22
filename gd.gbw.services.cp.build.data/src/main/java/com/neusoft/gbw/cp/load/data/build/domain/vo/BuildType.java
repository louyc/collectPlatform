package com.neusoft.gbw.cp.load.data.build.domain.vo;

import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;

public class BuildType {

	private ProtocolType proType;
	private BusinessTaskType busTaskType;
	private ProtocolType oriProType;	//原始任务类型，在做删除任务时，保留原来的任务类型
	
	public BuildType(ProtocolType proType) {
		this.proType = proType;
	} 
	
	public BuildType(ProtocolType proType, BusinessTaskType busTaskType, ProtocolType oriProType) {
		this.proType = proType;
		this.busTaskType = busTaskType;
		this.oriProType = oriProType;
	}
	
	public BuildType(ProtocolType proType, BusinessTaskType busTaskType) {
		this.proType = proType;
		this.busTaskType = busTaskType;
	}
	
	public ProtocolType getProType() {
		return proType;
	}
	public void setProType(ProtocolType proType) {
		this.proType = proType;
	}
	public BusinessTaskType getBusTaskType() {
		return busTaskType;
	}
	public void setBusTaskType(BusinessTaskType busTaskType) {
		this.busTaskType = busTaskType;
	}
	public String getKey() {
		return proType.name() + "_" + busTaskType.name();
	}
	public ProtocolType getOriProType() {
		return oriProType;
	}
	public void setOriProType(ProtocolType oriProType) {
		this.oriProType = oriProType;
	}

	@Override
	public String toString() {
		return "BuildType [proType=" + proType + ", busTaskType=" + busTaskType
				+ ", oriProType=" + oriProType + "]";
	}
}
