package com.neusoft.gbw.duties.quota.vo;

import com.neusoft.gbw.infrastructure.base.BaseDTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
@XStreamAlias("QuotaItemValueVo")
public class QuotaItemValueVo extends BaseDTO {

	private static final long serialVersionUID = 1L;
	@XStreamAsAttribute
	@XStreamAlias("achieveTime")
	private String achieveTime;			//数据时间格式：yyyy-MM-dd HH:mm:ss:sss
	@XStreamAsAttribute
	@XStreamAlias("achieveValue")
	private String achieveValue;		//实际数值
	@XStreamAsAttribute
	@XStreamAlias("achieveMAXValue")
	private String achieveMAXValue;		//最大值（调制度最大值）
	
	public String getAchieveTime() {
		return achieveTime;
	}
	public void setAchieveTime(String achieveTime) {
		this.achieveTime = achieveTime;
	}
	public String getAchieveValue() {
		return achieveValue;
	}
	public void setAchieveValue(String achieveValue) {
		this.achieveValue = achieveValue;
	}
	public String getAchieveMAXValue() {
		return achieveMAXValue;
	}
	public void setAchieveMAXValue(String achieveMAXValue) {
		this.achieveMAXValue = achieveMAXValue;
	}
	@Override
	public String toString() {
		return "QuotaItemValueVo [achieveTime=" + achieveTime
				+ ", achieveValue=" + achieveValue + ", achieveMAXValue="
				+ achieveMAXValue + "]";
	}

}
