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
 * 录音收测数据子元素
 * @author Administrator
 *
 */
@XStreamAlias("TaskRecord")
public class TaskRecord {
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
	@Valid
	@XStreamImplicit
	private List<Record> records;
	
	public void addRecord(Record record) {
		if (this.records == null) {
			records = new ArrayList<Record>();
		}
		records.add(record);
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



	public List<Record> getRecords() {
		return records;
	}

	public void setRecords(List<Record> records) {
		this.records = records;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
