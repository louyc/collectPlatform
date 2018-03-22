package com.neusoft.gbw.cp.conver.v8.protocol.vo.other;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * 31.设备报警参数条目子元素
 * @author Administrator
 *
 */
@XStreamAlias("EquipmentAlarmParam")
public class EquipmentAlarmParam {
	@Valid
	@XStreamImplicit
	private List<AlarmParam> alarmParams;
		public void addAlarmParam(AlarmParam alarmParam) {
			if (this.alarmParams == null) {
				alarmParams = new ArrayList<AlarmParam>();
			}
			alarmParams.add(alarmParam);
		}
		

	public List<AlarmParam> getAlarmParams() {
			return alarmParams;
		}

		public void setAlarmParams(List<AlarmParam> alarmParams) {
			this.alarmParams = alarmParams;
		}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
