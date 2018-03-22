package com.neusoft.gbw.cp.conver.v7.protocol.vo.audio;

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
 * 21.录音任务子元素
 * @author Administrator
 *
 */
@XStreamAlias("StreamTask")
public class StreamTask {
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
	private List<StreamCycleReportTime> streamCycleReportTimes;
	@Valid
	@XStreamImplicit
	private List<StreamSingleReportTime> streamSingleReportTimes;

	public void addStreamCycleReportTime(StreamCycleReportTime streamCycleReportTime) {
		if (this.streamCycleReportTimes == null) {
			streamCycleReportTimes = new ArrayList<StreamCycleReportTime>();
		}
		streamCycleReportTimes.add(streamCycleReportTime);
	}

	public void addStreamSingleReportTime(StreamSingleReportTime streamSingleReportTime) {
		if (this.streamSingleReportTimes == null) {
			streamSingleReportTimes = new ArrayList<StreamSingleReportTime>();
		}
		streamSingleReportTimes.add(streamSingleReportTime);
	}

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

	public List<StreamCycleReportTime> getStreamCycleReportTimes() {
		return streamCycleReportTimes;
	}

	public void setStreamCycleReportTimes(List<StreamCycleReportTime> streamCycleReportTimes) {
		this.streamCycleReportTimes = streamCycleReportTimes;
	}

	public List<StreamSingleReportTime> getStreamSingleReportTimes() {
		return streamSingleReportTimes;
	}

	public void setStreamSingleReportTimes(List<StreamSingleReportTime> streamSingleReportTimes) {
		this.streamSingleReportTimes = streamSingleReportTimes;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
