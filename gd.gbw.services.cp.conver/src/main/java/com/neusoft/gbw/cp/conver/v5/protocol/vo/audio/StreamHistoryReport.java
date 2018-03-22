package com.neusoft.gbw.cp.conver.v5.protocol.vo.audio;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IReport;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * 18.	录音历史查询返回
 * @author Administrator
 *
 */
@XStreamAlias("StreamHistoryReport")
public class StreamHistoryReport implements IReport{
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@XStreamAsAttribute
	@XStreamAlias("TaskID")
	private String taskID;
	@Valid
	@XStreamImplicit
	private List<TaskRecord> taskRecords;
	
	public void addTaskRecord(TaskRecord taskRecord) {
		if (this.taskRecords == null) {
			taskRecords = new ArrayList<TaskRecord>();
		}
		taskRecords.add(taskRecord);
	}
	
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
	
	
	public List<TaskRecord> getTaskRecords() {
		return taskRecords;
	}

	public void setTaskRecords(List<TaskRecord> taskRecords) {
		this.taskRecords = taskRecords;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
