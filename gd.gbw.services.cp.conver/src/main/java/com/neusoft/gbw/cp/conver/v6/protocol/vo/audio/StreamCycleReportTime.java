package com.neusoft.gbw.cp.conver.v6.protocol.vo.audio;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 录音收测循环时间设置子元素
 * @author Administrator
 *
 */
@XStreamAlias("CycleReportTime")
public class StreamCycleReportTime {
	@NotNull(message = "DayOfWeek不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="DayOfWeek-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("DayOfWeek")
	private String dayOfWeek;
	@NotNull(message = "StartTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="StartTime-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("StartTime")
	private String startTime;
	@NotNull(message = "EndTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="EndTime-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("EndTime")
	private String endTime;
	@NotNull(message = "RecordLength不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="RecordLength-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("RecordLength")
	private String recordLength;
	
	@NotNull(message = "CycleReportTime-ExpireDays不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="CycleReportTime-ExpireDays首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("ExpireDays")
	private String expireDays;

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

	public String getExpireDays() {
		return expireDays;
	}

	public void setExpireDays(String expireDays) {
		this.expireDays = expireDays;
	}
	

	public String getRecordLength() {
		return recordLength;
	}

	public void setRecordLength(String recordLength) {
		this.recordLength = recordLength;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
