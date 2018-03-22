package com.neusoft.gbw.cp.conver.v6.protocol.vo.other;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IQuery;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 45.文件查询
 * @author Administrator
 *
 */
@XStreamAlias("FileQuery")
public class FileQuery implements IQuery {
	@NotNull(message = "Type不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="Type-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Type")
	private String type;
	@NotNull(message = "StartDateTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="StartDateTime-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("StartDateTime")
	private String startDateTime;
	@NotNull(message = "EndDateTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="EndDateTime-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("EndDateTime")
	private String endDateTime;
	@XStreamAsAttribute
	@XStreamAlias("TaskID")
	private String taskID;
	@Valid
	@XStreamImplicit
	private List<Frequency> frequencys;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public void addFrequency(Frequency Frequency) {
		if (this.frequencys == null) {
			frequencys = new ArrayList<Frequency>();
		}
		frequencys.add(Frequency);
	}

	public List<Frequency> getFrequency() {
		return frequencys;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
