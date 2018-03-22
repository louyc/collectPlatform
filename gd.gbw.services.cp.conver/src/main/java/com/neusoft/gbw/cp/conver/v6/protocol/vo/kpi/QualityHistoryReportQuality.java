package com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi;

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
 * 6. 指标收测数据子元素
 * @author Administrator
 *
 */
@XStreamAlias("Quality")
public class QualityHistoryReportQuality {
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
	@NotNull(message = "CheckDateTime不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="CheckDateTime-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("CheckDateTime")
	private String checkDateTime;
	@Valid
	@XStreamImplicit
	private List<QualityHistoryReportQualityQualityIndex> QualityHistoryReport_Quality_QualityIndexs;

	public void addQualityHistoryReport_Quality_QualityIndex(QualityHistoryReportQualityQualityIndex QualityHistoryReport_Quality_QualityIndex) {
		if (this.QualityHistoryReport_Quality_QualityIndexs == null) {
			QualityHistoryReport_Quality_QualityIndexs = new ArrayList<QualityHistoryReportQualityQualityIndex>();
		}
		QualityHistoryReport_Quality_QualityIndexs.add(QualityHistoryReport_Quality_QualityIndex);
	}

	public List<QualityHistoryReportQualityQualityIndex> getQualityHistoryReport_Quality_QualityIndex() {
		return QualityHistoryReport_Quality_QualityIndexs;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
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

	public String getCheckDateTime() {
		return checkDateTime;
	}

	public void setCheckDateTime(String checkDateTime) {
		this.checkDateTime = checkDateTime;
	}

	public List<QualityHistoryReportQualityQualityIndex> getQualityHistoryReport_Quality_QualityIndexs() {
		return QualityHistoryReport_Quality_QualityIndexs;
	}

	public void setQualityHistoryReport_Quality_QualityIndexs(
			List<QualityHistoryReportQualityQualityIndex> qualityHistoryReport_Quality_QualityIndexs) {
		QualityHistoryReport_Quality_QualityIndexs = qualityHistoryReport_Quality_QualityIndexs;
	}
	
}
