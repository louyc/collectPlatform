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
 * 46.文件查询返回
 * @author Administrator
 *
 */
@XStreamAlias("FileQueryR")
public class FileQueryR implements IReport{
	@Valid
	@XStreamImplicit
	private List<ResultInfo> resultInfos;

	public void addResultInfo(ResultInfo ResultInfo) {
		if (this.resultInfos == null) {
			resultInfos = new ArrayList<ResultInfo>();
		}
		resultInfos.add(ResultInfo);
	}

	public List<ResultInfo> getResultInfo() {
		return resultInfos;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
