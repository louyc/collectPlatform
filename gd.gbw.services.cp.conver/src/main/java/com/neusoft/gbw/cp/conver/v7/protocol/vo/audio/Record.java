package com.neusoft.gbw.cp.conver.v7.protocol.vo.audio;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * 录音文件子元素
 * @author Administrator
 *
 */
@XStreamAlias("Record")
public class Record {
	@NotNull(message = "RecordID不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="RecordID-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("RecordID")
	private String recordID;
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
	@NotNull(message = "URL不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="URL-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("URL")
	private String URL;
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
	public String getRecordID() {
		return recordID;
	}
	public void setRecordID(String recordID) {
		this.recordID = recordID;
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
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
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
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
