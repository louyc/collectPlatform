package com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IQuery;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *  15.	频谱任务设置
 * @author Administrator
 *
 */
@XStreamAlias("SpectrumTaskSet")
public class SpectrumTaskSet implements IQuery {
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@NotNull(message = "TaskID不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="TaskID-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("TaskID")
	private String taskID;
	@NotNull(message = "Action不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="Action-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Action")
	private String action;
	@NotNull(message = "ValidStartDateTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="ValidStartDateTime-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("ValidStartDateTime")
	private String validStartDateTime;
	@NotNull(message = "ValidEndDateTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="ValidEndDateTime-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("ValidEndDateTime")
	private String validEndDateTime;
	@NotNull(message = "SampleNumber不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="SampleNumber-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("SampleNumber")
	private String sampleNumber;
	@NotNull(message = "Unit不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Unit-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Unit")
	private String unit;
	@Valid
	@XStreamAsAttribute
	@XStreamAlias("SpectrumTask")
	private SpectrumTask spectrumTask;

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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getValidStartDateTime() {
		return validStartDateTime;
	}

	public void setValidStartDateTime(String validStartDateTime) {
		this.validStartDateTime = validStartDateTime;
	}

	public String getValidEndDateTime() {
		return validEndDateTime;
	}

	public void setValidEndDateTime(String validEndDateTime) {
		this.validEndDateTime = validEndDateTime;
	}

	public String getSampleNumber() {
		return sampleNumber;
	}

	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public SpectrumTask getSpectrumTask() {
		return spectrumTask;
	}

	public void setSpectrumTask(SpectrumTask spectrumTask) {
		this.spectrumTask = spectrumTask;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
