package com.neusoft.gbw.cp.conver.v7.protocol.vo.kpi;

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
 * 6.指标历史查询返回
 * @author Administrator
 *
 */
@XStreamAlias("QualityHistoryReport")
public class QualityHistoryReport implements IReport{
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@XStreamAsAttribute
	@XStreamAlias("TaskID")
	private String taskID;
	
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
	@Valid
	@XStreamImplicit
	private List<QualityHistoryReportQuality> qualityHistoryReport_Qualitys;

	public void addQualityHistoryReport_Quality(QualityHistoryReportQuality qualityHistoryReport_Quality) {
		if (this.qualityHistoryReport_Qualitys == null) {
			qualityHistoryReport_Qualitys = new ArrayList<QualityHistoryReportQuality>();
		}
		qualityHistoryReport_Qualitys.add(qualityHistoryReport_Quality);
	}

	public List<QualityHistoryReportQuality> getQualityAlarm() {
		return qualityHistoryReport_Qualitys;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}

	public List<QualityHistoryReportQuality> getQualityHistoryReport_Qualitys() {
		return qualityHistoryReport_Qualitys;
	}

	public void setQualityHistoryReport_Qualitys(
			List<QualityHistoryReportQuality> qualityHistoryReport_Qualitys) {
		this.qualityHistoryReport_Qualitys = qualityHistoryReport_Qualitys;
	}
	
}
