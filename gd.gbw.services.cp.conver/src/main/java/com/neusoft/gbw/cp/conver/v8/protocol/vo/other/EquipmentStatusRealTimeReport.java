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
 * 34.	设备状态实时结果主动上报
 * @author Administrator
 *
 */
@XStreamAlias("EquipmentStatusRealTimeReport")
public class EquipmentStatusRealTimeReport implements IReport{
	@Valid
	@XStreamImplicit
	private List<EquipmentRealTimeStatus> equipmentRealtimeStatuses;
		
	public void addEquipmentRealtimeStatus(EquipmentRealTimeStatus equipmentRealtimeStatus) {
		if (this.equipmentRealtimeStatuses == null) {
			equipmentRealtimeStatuses = new ArrayList<EquipmentRealTimeStatus>();
		}
		equipmentRealtimeStatuses.add(equipmentRealtimeStatus);
	}

	public List<EquipmentRealTimeStatus> getEquipmentRealtimeStatuses() {
		return equipmentRealtimeStatuses;
	}

	public void setEquipmentRealtimeStatuses(
			List<EquipmentRealTimeStatus> equipmentRealtimeStatuses) {
		this.equipmentRealtimeStatuses = equipmentRealtimeStatuses;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
	
}
