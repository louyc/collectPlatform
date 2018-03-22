package com.neusoft.gbw.cp.conver.v7.protocol.vo.other;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 44.任务当前执行状态子元素
 * @author Administrator
 *
 */
@XStreamAlias("TaskStatus")
public class TaskStatus {
	@NotNull(message = "TaskID不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="TaskID-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("TaskID")
	private String taskID;
	@NotNull(message = "Status不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Status-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Status")
	private String status;
	@XStreamAsAttribute
	@XStreamAlias("Desc")
	private String desc;
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
	
}
