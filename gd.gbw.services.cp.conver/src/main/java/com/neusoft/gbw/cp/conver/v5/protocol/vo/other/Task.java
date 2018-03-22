package com.neusoft.gbw.cp.conver.v5.protocol.vo.other;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 43.待查询状态的任务子元素
 * @author Administrator
 *
 */
@XStreamAlias("Task")
public class Task {
	@NotNull(message = "TaskID不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="TaskID-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("TaskID")
	private String TaskID;

	public String getTaskID() {
		return TaskID;
	}

	public void setTaskID(String taskID) {
		TaskID = taskID;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
