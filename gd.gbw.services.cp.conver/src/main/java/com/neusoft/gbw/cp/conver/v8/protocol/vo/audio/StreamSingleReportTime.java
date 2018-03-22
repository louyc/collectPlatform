package com.neusoft.gbw.cp.conver.v8.protocol.vo.audio;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 录音收测独立时间设置子元素
 * @author Administrator
 *
 */
@XStreamAlias("SingleReportTime")
public class StreamSingleReportTime {
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
	@NotNull(message = "RecordLength不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="RecordLength-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("RecordLength")
	private String recordLength;
	public String getRecordLength() {
		return recordLength;
	}

	public void setRecordLength(String recordLength) {
		this.recordLength = recordLength;
	}

	@NotNull(message = "SingleReportTime-ReportMode不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="SingleReportTime-ReportMode-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("ReportMode")
	private String reportMode;
	@XStreamAsAttribute
	@XStreamAlias("ReportTime")
	private String reportTime;
	@NotNull(message = "SingleReportTime-ExpireDays不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="SingleReportTime-ExpireDays-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("ExpireDays")
	private String expireDays;

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

	public String getReportTime() {
		return reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	public String getExpireDays() {
		return expireDays;
	}

	public void setExpireDays(String expireDays) {
		this.expireDays = expireDays;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
