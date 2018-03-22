package com.neusoft.gbw.cp.conver.v6.protocol.vo.kpi;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IReport;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * 2.指标报警历史查询返回
 * @author Administrator
 *
 */
@XStreamAlias("QualityAlarmHistoryReport")
public class QualityAlarmHistoryReport implements IReport{
	@Valid
	@XStreamImplicit
	private List<QualityAlarm> qualityAlarms;

	public void addQualityAlarm(QualityAlarm qualityAlarm) {
		if (this.qualityAlarms == null) {
			qualityAlarms = new ArrayList<QualityAlarm>();
		}
		qualityAlarms.add(qualityAlarm);
	}

	public List<QualityAlarm> getQualityAlarm() {
		return qualityAlarms;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
