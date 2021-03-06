package com.neusoft.gbw.cp.conver.v8.protocol.vo.other.special;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.RealtimeStatusEquipment;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * 34.设备实时状态子元素(针对向对应厂商对接口大小写不明确，定义的特殊类)
 * @author Administrator
 *
 */
@XStreamAlias("EquipmentRealtimeStatus")
public class EquipmentRealtimeStatus {
	@NotNull(message = "CheckDateTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="CheckDateTime-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("CheckDateTime")
	private String checkDateTime;
	@Valid
	@XStreamImplicit
	private List<RealtimeStatusEquipment> statusEquipments;
	
	public void addStatusEquipment(RealtimeStatusEquipment statusEquipment) {
		if (this.statusEquipments == null) {
			statusEquipments = new ArrayList<RealtimeStatusEquipment>();
		}
		statusEquipments.add(statusEquipment);
	}

	public String getCheckDateTime() {
		return checkDateTime;
	}

	public void setCheckDateTime(String checkDateTime) {
		this.checkDateTime = checkDateTime;
	}

	public List<RealtimeStatusEquipment> getEquipments() {
		return statusEquipments;
	}

	public void setEquipments(List<RealtimeStatusEquipment> equipments) {
		this.statusEquipments = equipments;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
