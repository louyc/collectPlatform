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
 * 31.	设备报警参数设置
 * @author Administrator
 *
 */
@XStreamAlias("EquipmentAlarmParamSet")
public class EquipmentAlarmParamSet implements IQuery {
	@Valid
	@XStreamImplicit
	private List<EquipmentAlarmParam> equipmentAlarmParams;

	public void addEquipmentAlarmParam(EquipmentAlarmParam equipmentAlarmParam) {
		if (this.equipmentAlarmParams == null) {
			equipmentAlarmParams = new ArrayList<EquipmentAlarmParam>();
		}
		equipmentAlarmParams.add(equipmentAlarmParam);
	}

	public List<EquipmentAlarmParam> getEquipmentAlarmParams() {
		return equipmentAlarmParams;
	}

	public void setEquipmentAlarmParams(List<EquipmentAlarmParam> equipmentAlarmParams) {
		this.equipmentAlarmParams = equipmentAlarmParams;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
