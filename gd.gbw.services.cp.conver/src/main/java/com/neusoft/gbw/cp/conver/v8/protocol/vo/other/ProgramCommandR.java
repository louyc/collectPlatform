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
 * 50.版本查询返回
 * @author Administrator
 *
 */
@XStreamAlias("ProgramCommandR")
public class ProgramCommandR implements IReport {
	@Valid
	@XStreamImplicit
	private List<ProgramInfo> programInfos;
	@Valid
	@XStreamImplicit
	private List<AliveInfo> aliveInfos;

	public void addProgramInfo(ProgramInfo ProgramInfo) {
		if (this.programInfos == null) {
			programInfos = new ArrayList<ProgramInfo>();
		}
		programInfos.add(ProgramInfo);
	}

	public List<ProgramInfo> getProgramInfo() {
		return programInfos;
	}

	public void addAliveInfo(AliveInfo AliveInfo) {
		if (this.aliveInfos == null) {
			aliveInfos = new ArrayList<AliveInfo>();
		}
		aliveInfos.add(AliveInfo);
	}

	public List<AliveInfo> getAliveInfo() {
		return aliveInfos;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}