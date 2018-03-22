package com.neusoft.gbw.cp.conver.v6.protocol.vo.offset;

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
 * 25.频偏收测任务子元素
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
	@Valid
	@XStreamImplicit
	private List<OffsetCycleReportTime> offsetCycleReportTimes;
	@Valid
	@XStreamImplicit
	private List<OffsetSingleReportTime> offsetSingleReportTimes;

	public String getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(String sleepTime) {
		this.sleepTime = sleepTime;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public void addOffsetSingleReportTime(OffsetSingleReportTime offsetSingleReportTime) {
		if (this.offsetSingleReportTimes == null) {
			offsetSingleReportTimes = new ArrayList<OffsetSingleReportTime>();
		}
		offsetSingleReportTimes.add(offsetSingleReportTime);
	}
	
	public List<OffsetSingleReportTime> getOffsetSingleReportTimes() {
		return offsetSingleReportTimes;
	}
	
	public void addOffsetCycleReportTime(OffsetCycleReportTime offsetCycleReportTime) {
		if (this.offsetCycleReportTimes == null) {
			offsetCycleReportTimes = new ArrayList<OffsetCycleReportTime>();
		}
		offsetCycleReportTimes.add(offsetCycleReportTime);
	}

	public List<OffsetCycleReportTime> getOffsetCycleReportTimes() {
		return offsetCycleReportTimes;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}

}
