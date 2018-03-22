package com.neusoft.gbw.cp.conver.v7.protocol.vo.other;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IReport;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 38. 设备历史日志查询返回
 * 
 * @author Administrator
 * 
 */
@XStreamAlias("EquipmentLogHistoryQueryR")
public class EquipmentLogHistoryQueryR implements IReport {
	@Valid
	@XStreamImplicit
	private List<LogItem> logItems;

	public List<LogItem> getLogItems() {
		return logItems;
	}

	public void setLogItems(List<LogItem> logItems) {
		this.logItems = logItems;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyleConfig.DEFAULT_STYLE);
	}
}
