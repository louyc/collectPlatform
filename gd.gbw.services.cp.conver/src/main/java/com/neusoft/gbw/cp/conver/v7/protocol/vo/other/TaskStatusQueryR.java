package com.neusoft.gbw.cp.conver.v7.protocol.vo.other;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IReport;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 44.任务状态查询返回
 * @author Administrator
 *
 */
@XStreamAlias("TaskStatusQueryR")
public class TaskStatusQueryR implements IReport{
	@Valid
	@XStreamImplicit
	private List<TaskStatus> taskStatuss;
	public List<TaskStatus> getTaskStatuss() {
		return taskStatuss;
	}
	public void setTaskStatuss(List<TaskStatus> taskStatuss) {
		this.taskStatuss = taskStatuss;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
