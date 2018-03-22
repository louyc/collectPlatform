package com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 9.QualityTask 指标收测独立时间设置子元素
 * @author Administrator
 *
 */
@XStreamAlias("SingleReportTime")
public class SingleReportTime {
	@NotNull(message = "SingleReportTime-StartDateTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="SingleReportTime-StartDateTime首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("StartDateTime")
	private String startDateTime;
	@NotNull(message = "SingleReportTime-EndDateTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="SingleReportTime-EndDateTime首尾字符不能为空")
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
	@NotNull(message = "SingleReportTime-ExpireDays不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="SingleReportTime-ExpireDays首尾字符不能为空")
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
	public String getReportMode() {
		return reportMode;
	}
	public void setReportMode(String reportMode) {
		this.reportMode = reportMode;
	}
	public String getReportInterval() {
		return reportInterval;
	}
	public void setReportInterval(String reportInterval) {
		this.reportInterval = reportInterval;
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
