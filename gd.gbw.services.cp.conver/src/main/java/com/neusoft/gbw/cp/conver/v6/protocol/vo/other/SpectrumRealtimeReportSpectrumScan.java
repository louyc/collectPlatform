package com.neusoft.gbw.cp.conver.v6.protocol.vo.other;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**?
 * 14.频谱扫描收测数据子元素
 * @author Administrator
 *
 */
@XStreamAlias("SpectrumScan")
public class SpectrumRealtimeReportSpectrumScan {
	@NotNull(message = "ScanDateTime不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="ScanDateTime-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("ScanDateTime")
	private String scanDateTime;
	@Valid
	@XStreamImplicit
	private List<ScanResult> scanResults;
	
	public void addRealtimeScanResult(ScanResult realtimeScanResult) {
		if (this.scanResults == null) {
			scanResults = new ArrayList<ScanResult>();
		}
		 scanResults.add(realtimeScanResult);
	}

	

	public List<ScanResult> getScanResults() {
		return scanResults;
	}



	public void setScanResults(List<ScanResult> scanResults) {
		this.scanResults = scanResults;
	}



	public String getScanDateTime() {
		return scanDateTime;
	}

	public void setScanDateTime(String scanDateTime) {
		this.scanDateTime = scanDateTime;
	}



	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}

}
