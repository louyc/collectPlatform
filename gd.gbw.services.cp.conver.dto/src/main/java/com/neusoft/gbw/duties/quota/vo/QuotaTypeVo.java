package com.neusoft.gbw.duties.quota.vo;

import java.util.List;

import com.neusoft.gbw.infrastructure.base.BaseDTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
@XStreamAlias("QuotaTypeVo")
public class QuotaTypeVo extends BaseDTO {

	private static final long serialVersionUID = 1L;
	@XStreamAsAttribute
	@XStreamAlias("quotaType")
	private String quotaType;		//类型  1:载波电平 2:调制度 3:调幅度 5:调制度最大值 6:频偏 8:带宽  9:频谱
	@XStreamImplicit
	private List<QuotaItemValueVo> quotaItemValueArray;
	
	public String getQuotaType() {
		return quotaType;
	}
	public void setQuotaType(String quotaType) {
		this.quotaType = quotaType;
	}
	public List<QuotaItemValueVo> getQuotaItemValueArray() {
		return quotaItemValueArray;
	}
	public void setQuotaItemValueArray(List<QuotaItemValueVo> quotaItemValueArray) {
		this.quotaItemValueArray = quotaItemValueArray;
	}
	@Override
	public String toString() {
		return "QuotaTypeVo [quotaType=" + quotaType + ", quotaItemValueArray="
				+ quotaItemValueArray + "]";
	}
}
