package com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi;

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
 * 5.指标历史查询
 * @author Administrator
 *
 */
@XStreamAlias("QualityHistoryQuery")
public class QualityHistoryQuery implements IQuery{
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@XStreamAsAttribute
	@XStreamAlias("TaskID")
	private String taskID;
	@XStreamAsAttribute
	@XStreamAlias("Freq")
	private String freq;
	@XStreamAsAttribute
	@XStreamAlias("Band")
	private String band;
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
	@NotNull(message = "SampleNumber不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="SampleNumber-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("SampleNumber")
	private String sampleNumber;
	@NotNull(message = "Unit不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Unit-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Unit")
	private String unit;
	@Valid
	@XStreamImplicit
	private List<QualityHistoryQueryQualityIndex> QualityHistoryQuery_QualityIndexs;

	public void addQualityHistoryQuery_QualityIndex(QualityHistoryQueryQualityIndex QualityHistoryQuery_QualityIndex) {
		if (this.QualityHistoryQuery_QualityIndexs == null) {
			QualityHistoryQuery_QualityIndexs = new ArrayList<QualityHistoryQueryQualityIndex>();
		}
		QualityHistoryQuery_QualityIndexs.add(QualityHistoryQuery_QualityIndex);
	}

	public List<QualityHistoryQueryQualityIndex> getQualityHistoryQuery_QualityIndex() {
		return QualityHistoryQuery_QualityIndexs;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
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

	public String getFreq() {
		return freq;
	}

	public void setFreq(String freq) {
		this.freq = freq;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
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

	public String getSampleNumber() {
		return sampleNumber;
	}

	public void setSampleNumber(String sampleNumber) {
		this.sampleNumber = sampleNumber;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public List<QualityHistoryQueryQualityIndex> getQualityHistoryQuery_QualityIndexs() {
		return QualityHistoryQuery_QualityIndexs;
	}

	public void setQualityHistoryQuery_QualityIndexs(List<QualityHistoryQueryQualityIndex> QualityHistoryQuery_QualityIndexs) {
		this.QualityHistoryQuery_QualityIndexs = QualityHistoryQuery_QualityIndexs;
	}
	
	
}
