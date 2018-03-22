package com.neusoft.gbw.cp.conver.v5.protocol.vo.kpi;

import java.util.ArrayList;
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
 * 9.QualityTaskSet 指标收测任务子元素
 * @author Administrator
 *
 */
@XStreamAlias("QualityTask")
public class QualityTask {
	@NotNull(message = "SleepTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="SleepTime-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("SleepTime")
	private String sleepTime;
	@Valid
	@XStreamAsAttribute
	@XStreamAlias("Channel")
	private Channel channel;
	
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	@Valid
	@XStreamImplicit
	private List<SingleReportTime> singleReportTimes;
	@Valid
	@XStreamImplicit
	private List<CycleReportTime> cycleReportTimes;

	public void addSingleReportTime(SingleReportTime SingleReportTime) {
		if (this.singleReportTimes == null) {
			singleReportTimes = new ArrayList<SingleReportTime>();
		}
		singleReportTimes.add(SingleReportTime);
	}
	
	public List<SingleReportTime> getSingleReportTime() {
		return singleReportTimes;
	}
	public void addCycleReportTime(CycleReportTime CycleReportTime) {
		if (this.cycleReportTimes == null) {
			cycleReportTimes = new ArrayList<CycleReportTime>();
		}
		cycleReportTimes.add(CycleReportTime);
	}

	public List<CycleReportTime> getCycleReportTime() {
		return cycleReportTimes;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}

	public String getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(String sleepTime) {
		this.sleepTime = sleepTime;
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

	
}
