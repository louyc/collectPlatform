package com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 9.QualityTask 指标收测循环时间设置子元素
 * @author Administrator
 *
 */
@XStreamAlias("CycleReportTime")
public class CycleReportTime {
	@NotNull(message = "CycleReportTime-DayOfWeek不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="CycleReportTime-DayOfWeek首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("DayOfWeek")
	private String dayOfWeek;
	@NotNull(message = "CycleReportTime-StartTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="CycleReportTime-StartTime首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("StartTime")
	private String startTime;
	@NotNull(message = "CycleReportTime-EndTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="CycleReportTime-EndTime首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("EndTime")
	private String endTime;
	@NotNull(message = "CycleReportTime-ReportMode不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="CycleReportTime-ReportMode首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("ReportMode")
	private String reportMode;
	@XStreamAsAttribute
	@XStreamAlias("ReportInterval")
	private String reportInterval;
	@XStreamAsAttribute
	@XStreamAlias("ReportTime")
	private String reportTime;
	@NotNull(message = "CycleReportTime-ExpireDays不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="CycleReportTime-ExpireDays首尾字符不能为空")
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
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
