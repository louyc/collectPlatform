package com.neusoft.gbw.cp.conver.v5.protocol.vo.spectrum;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IQuery;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * 11.	频谱历史查询
 * @author Administrator
 *
 */
@XStreamAlias("SpectrumHistoryQuery")
public class SpectrumHistoryQuery implements IQuery{
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@XStreamAsAttribute
	@XStreamAlias("TaskID")
	private String taskID;
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
	@NotNull(message = "SampleNumber不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="SampleNumber-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("SampleNumber")
	private String sampleNumber;
//	@NotNull(message = "Unit不能为空!")
//	@Pattern(regexp="^[^\\s]+$",message="Unit-首尾字符不能为空")
//	@XStreamAsAttribute
//	@XStreamAlias("Unit")
//	private String unit;
	
	public String getEquCode() {
		return equCode;
	}
	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
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
	public String getSampleNumber() {
		return sampleNumber;
	}
	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}
//	public String getUnit() {
//		return unit;
//	}
//	public void setUnit(String unit) {
//		this.unit = unit;
//	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
