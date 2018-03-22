package com.neusoft.gbw.cp.conver.v6.protocol.vo.other;

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
@XStreamAlias("EquipmentStatusRealtimeReport")
public class EquipmentStatusRealtimeReport implements IReport{
	@Valid
	@XStreamImplicit
	private List<EquipmentRealtimeStatus> equipmentRealtimeStatuses;
		
	public void addEquipmentRealtimeStatus(EquipmentRealtimeStatus equipmentRealtimeStatus) {
		if (this.equipmentRealtimeStatuses == null) {
			equipmentRealtimeStatuses = new ArrayList<EquipmentRealtimeStatus>();
		}
		equipmentRealtimeStatuses.add(equipmentRealtimeStatus);
	}

	public List<EquipmentRealtimeStatus> getEquipmentRealtimeStatuses() {
		return equipmentRealtimeStatuses;
	}

	public void setEquipmentRealtimeStatuses(
			List<EquipmentRealtimeStatus> equipmentRealtimeStatuses) {
		this.equipmentRealtimeStatuses = equipmentRealtimeStatuses;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
	
}
