package com.neusoft.gbw.cp.conver.v5.protocol.vo.offset;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IReport;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * 24.	频偏历史查询返回
 * @author Administrator
 *
 */
@XStreamAlias("OffsetHistoryReport")
public class OffsetHistoryReport implements IReport{
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@XStreamAsAttribute
	@XStreamAlias("TaskID")
	private String taskID;
	@Valid
	@XStreamImplicit
	private List<OffsetIndex> offsetIndexs;
	
	public void addOffsetIndex(OffsetIndex offsetIndex) {
		if (this.offsetIndexs == null) {
			offsetIndexs = new ArrayList<OffsetIndex>();
		}
		offsetIndexs.add(offsetIndex);
	}
	public String getEquCode() {
		return equCode;
	}
	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public List<OffsetIndex> getOffsetIndexs() {
		return offsetIndexs;
	}
	public void setOffsetIndexs(List<OffsetIndex> offsetIndexs) {
		this.offsetIndexs = offsetIndexs;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
