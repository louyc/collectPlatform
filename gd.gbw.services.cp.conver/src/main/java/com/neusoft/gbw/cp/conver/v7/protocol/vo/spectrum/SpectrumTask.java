package com.neusoft.gbw.cp.conver.v7.protocol.vo.spectrum;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 15.频谱扫描任务子元素
 * @author Administrator
 *
 */
@XStreamAlias("SpectrumTask")
public class SpectrumTask {
	@NotNull(message = "Type不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Type-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("TaskType")
	private String taskType;
	@XStreamAsAttribute
	@XStreamAlias("Band")
	private String band;
	@XStreamAsAttribute
	@XStreamAlias("StartFreq")
	private String startFreq;
	@XStreamAsAttribute
	@XStreamAlias("EndFreq")
	private String endFreq;
	@XStreamAsAttribute
	@XStreamAlias("StepFreq")
	private String stepFreq;
	@NotNull(message = "SleepTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="SleepTime-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("SleepTime")
	private String sleepTime;
	@Valid
	@XStreamAsAttribute
	@XStreamAlias("Channel")
	private List<Channel> channels;
	@Valid
	@XStreamImplicit
	private List<CycleReportTime> cycleReportTimes;
	@Valid
	@XStreamImplicit
	private List<SingleReportTime> singleReportTimes;

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public String getStartFreq() {
		return startFreq;
	}

	public void setStartFreq(String startFreq) {
		this.startFreq = startFreq;
	}

	public String getEndFreq() {
		return endFreq;
	}

	public void setEndFreq(String endFreq) {
		this.endFreq = endFreq;
	}

	public String getStepFreq() {
		return stepFreq;
	}

	public void setStepFreq(String stepFreq) {
		this.stepFreq = stepFreq;
	}

	public String getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(String sleepTime) {
		this.sleepTime = sleepTime;
	}

	public List<Channel> getChannel() {
		return channels;
	}

	public void setChannel(List<Channel> channels) {
		this.channels = channels;
	}

	public List<SingleReportTime> getSingleReportTimes() {
		return singleReportTimes;
	}

	public void setSingleReportTimes(List<SingleReportTime> singleReportTimes) {
		this.singleReportTimes = singleReportTimes;
	}

	public List<CycleReportTime> getCycleReportTimes() {
		return cycleReportTimes;
	}

	public void setCycleReportTimes(List<CycleReportTime> cycleReportTimes) {
		this.cycleReportTimes = cycleReportTimes;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}

}
