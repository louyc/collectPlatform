package com.neusoft.gbw.cp.conver.v6.protocol.vo.offset;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("SingleReportTime")
public class OffsetSingleReportTime {
	@NotNull(message = "StartDateTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="StartDateTime-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("StartDateTime")
	private String startDateTime;
	@NotNull(message = "EndDateTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="EndDateTime-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("EndDateTime")
	private String endDateTime;
	@NotNull(message = "SingleReportTime-ReportMode不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="SingleReportTime-ReportMode首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("ReportMode")
	private String reportMode;
	@XStreamAsAttribute
	@XStreamAlias("ReportInterval")
	private String reportInterval;
	@XStreamAsAttribute
	@XStreamAlias("ReportTime")
	private String reportTime;

	public String getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	

	public String getReportInterval() {
		return reportInterval;
	}

	public void setReportInterval(String reportInterval) {
		this.reportInterval = reportInterval;
	}

	public String getReportMode() {
		return reportMode;
	}

	public void setReportMode(String reportMode) {
		this.reportMode = reportMode;
	}

	public String getReportTime() {
		return reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
	

}
