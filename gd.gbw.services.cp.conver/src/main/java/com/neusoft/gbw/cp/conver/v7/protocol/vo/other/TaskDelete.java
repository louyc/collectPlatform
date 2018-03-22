package com.neusoft.gbw.cp.conver.v7.protocol.vo.other;


import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IQuery;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 41.任务删除
 * @author Administrator
 *
 */
@XStreamAlias("TaskDelete")
public class TaskDelete implements IQuery {
	@XStreamAsAttribute
	@XStreamAlias("Band")
	private String band;
	@XStreamAsAttribute
	@XStreamAlias("Freq")
	private String freq;
	@XStreamAsAttribute
	@XStreamAlias("TaskID")
	private String taskID;
	@XStreamAsAttribute
	@XStreamAlias("StartDateTime")
	private String startDateTime;
	@XStreamAsAttribute
	@XStreamAlias("EndDateTime")
	private String endDateTime;
	@XStreamAsAttribute
	@XStreamAlias("SrcCode")
	private String srcCode;
	/**
	 * Date date任务下发日期的删除条件 为空时，表示删除条件为所有下发日期的任务
	 * 格式为YYYY-MM-DD
	 */
	@XStreamAsAttribute
	@XStreamAlias("Date")
	private String date;
	@XStreamAsAttribute
	@XStreamAlias("TaskType")
	private String taskType;

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public String getFreq() {
		return freq;
	}

	public void setFreq(String freq) {
		this.freq = freq;
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

	public String getSrcCode() {
		return srcCode;
	}

	public void setSrcCode(String srcCode) {
		this.srcCode = srcCode;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}

}
