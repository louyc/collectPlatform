package com.neusoft.gbw.cp.conver.v8.protocol.vo.other;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IReport;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * 30.	设备报警查询返回
 * @author Administrator
 *
 */
@XStreamAlias("EquipmentAlarmHistoryReport")
public class EquipmentAlarmHistoryReport implements IReport{
	@Valid
	@XStreamImplicit
	private List<EquipmentAlarm> equipmentAlarms;
	public void addEquipmentAlarm(EquipmentAlarm equipmentAlarm) {
		if (this.equipmentAlarms == null) {
			equipmentAlarms = new ArrayList<EquipmentAlarm>();
		}
		equipmentAlarms.add(equipmentAlarm);
	}
	public List<EquipmentAlarm> getEquipmentAlarms() {
		return equipmentAlarms;
	}
	public void setEquipmentAlarms(List<EquipmentAlarm> equipmentAlarms) {
		this.equipmentAlarms = equipmentAlarms;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
