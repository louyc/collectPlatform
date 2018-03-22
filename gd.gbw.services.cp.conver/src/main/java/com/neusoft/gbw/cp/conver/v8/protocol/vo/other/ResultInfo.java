package com.neusoft.gbw.cp.conver.v8.protocol.vo.other;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 46.文件查询结果子元素
 * @author Administrator
 *
 */
@XStreamAlias("ResultInfo")
public class ResultInfo {
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
	@NotNull(message = "FileName不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="FileName-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("FileName")
	private String fileName;
	@NotNull(message = "Size不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="Size-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Size")
	private String size;
	@NotNull(message = "TaskID不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="TaskID-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("TaskID")
	private String taskID;
	@NotNull(message = "Band不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Band-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Band")
	private String band;
	@NotNull(message = "Freq不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Freq-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Freq")
	private String freq;
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
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
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
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
	
}
