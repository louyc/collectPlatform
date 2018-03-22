package com.neusoft.gbw.cp.conver.v7.protocol.vo.other;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IQuery;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 35.	初始化参数设置
 * @author Administrator
 *
 */
@XStreamAlias("EquipmentInitParamSet")
public class EquipmentInitParamSet implements IQuery {
	@Valid
	@XStreamImplicit
	private List<InitEquipment> initEquipments;

	public void InitEquipment(InitEquipment initEquipment) {
		if (this.initEquipments == null) {
			initEquipments = new ArrayList<InitEquipment>();
		}
		initEquipments.add(initEquipment);
	}

	public List<InitEquipment> getInitEquipments() {
		return initEquipments;
	}

	public void setInitEquipments(List<InitEquipment> initEquipments) {
		this.initEquipments = initEquipments;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
