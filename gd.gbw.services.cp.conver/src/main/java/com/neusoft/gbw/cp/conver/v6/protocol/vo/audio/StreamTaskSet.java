package com.neusoft.gbw.cp.conver.v6.protocol.vo.audio;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IQuery;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 21.	录音任务设置
 * @author Administrator
 *
 */
@XStreamAlias("StreamTaskSet")
public class StreamTaskSet implements IQuery {
	@NotNull(message = "TaskID不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="TaskID-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("TaskID")
	private String taskID;
	@NotNull(message = "Action不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Action-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Action")
	private String action;
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@NotNull(message = "Bps不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Bps-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Bps")
	private String Bps;
	@XStreamAsAttribute
	@XStreamAlias("Encode")
	private String encode;
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
	@Valid
	@XStreamAsAttribute
	@XStreamAlias("StreamTask")
	private StreamTask streamTask;

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

	public String getBps() {
		return Bps;
	}

	public void setBps(String bps) {
		Bps = bps;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public StreamTask getStreamTask() {
		return streamTask;
	}

	public void setStreamTask(StreamTask streamTask) {
		this.streamTask = streamTask;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
