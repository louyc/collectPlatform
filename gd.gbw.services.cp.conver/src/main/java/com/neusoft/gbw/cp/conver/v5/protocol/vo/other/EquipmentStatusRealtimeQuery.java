package com.neusoft.gbw.cp.conver.v5.protocol.vo.other;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IQuery;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * 33.	设备状态实时查询
 * @author Administrator
 *
 */
@XStreamAlias("EquipmentStatusRealtimeQuery")
public class EquipmentStatusRealtimeQuery implements IQuery{
	@NotNull(message = "ReportInterval不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="ReportInterval-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("ReportInterval")
	private String reportInterval;
	@NotNull(message = "SampleInterval不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="SampleInterval-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("SampleInterval")
	private String sampleInterval;
	@NotNull(message = "ExpireTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="ExpireTime-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("ExpireTime")
	private String expireTime;
	public String getReportInterval() {
		return reportInterval;
	}

	
	public String getSampleInterval() {
		return sampleInterval;
	}


	public void setSampleInterval(String sampleInterval) {
		this.sampleInterval = sampleInterval;
	}


	public String getExpireTime() {
		return expireTime;
	}


	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}


	public void setReportInterval(String reportInterval) {
		this.reportInterval = reportInterval;
	}


	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}

}
