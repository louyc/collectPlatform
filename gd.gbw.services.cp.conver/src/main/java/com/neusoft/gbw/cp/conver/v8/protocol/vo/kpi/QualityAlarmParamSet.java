package com.neusoft.gbw.cp.conver.v8.protocol.vo.kpi;

/**
 * 3.指标报警参数设置
 */
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IQuery;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 3.指标报警参数设置
 */

@XStreamAlias("QualityAlarmParamSet")
public class QualityAlarmParamSet implements IQuery {
	@Valid
	@XStreamImplicit
	private List<QualityAlarmParam> qualityAlarmParams;

	public void addQualityAlarmParam(QualityAlarmParam qualityAlarmParam) {
		if (this.qualityAlarmParams == null) {
			qualityAlarmParams = new ArrayList<QualityAlarmParam>();
		}
		qualityAlarmParams.add(qualityAlarmParam);
	}

	public List<QualityAlarmParam> getQualityAlarmParam() {
		return qualityAlarmParams;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}

	public List<QualityAlarmParam> getQualityAlarmParams() {
		return qualityAlarmParams;
	}

	public void setQualityAlarmParams(List<QualityAlarmParam> qualityAlarmParams) {
		this.qualityAlarmParams = qualityAlarmParams;
	}
}
