package com.neusoft.gbw.cp.conver.v6.protocol.vo.spectrum;

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
 * 12.	频谱历史查询返回
 * @author Administrator
 *
 */
@XStreamAlias("SpectrumHistoryReport")
public class SpectrumHistoryReport implements IReport{
	
	@XStreamAsAttribute
	@XStreamAlias("EquCode")
	private String equCode;
	@XStreamAsAttribute
	@XStreamAlias("TaskID")
	private String taskID;
	@Valid
	@XStreamImplicit
	private List<SpectrumScan> spectrumScans;
	
	public void addSpectrumScan(SpectrumScan spectrumScan) {
		if (this.spectrumScans == null) {
			spectrumScans = new ArrayList<SpectrumScan>();
		}
		spectrumScans.add(spectrumScan);
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
	public List<SpectrumScan> getSpectrumScan() {
		return spectrumScans;
	}
	public void setSpectrumScans(List<SpectrumScan> spectrumScans) {
		this.spectrumScans = spectrumScans;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
