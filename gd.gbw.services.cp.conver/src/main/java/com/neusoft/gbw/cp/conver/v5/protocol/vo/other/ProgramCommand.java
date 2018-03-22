package com.neusoft.gbw.cp.conver.v5.protocol.vo.other;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.vo.IQuery;
import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 49.版本查询
 * @author Administrator
 *
 */
@XStreamAlias("ProgramCommand")
public class ProgramCommand implements IQuery {
	@Valid
	@XStreamImplicit
	private List<ProgramInfoQuery> programInfoQuerys;
	@Valid
	@XStreamImplicit
	private List<Command> commands;
//	@Valid
//	@XStreamImplicit
//	private List<ReviseTime> reviseTimes;

	public void addProgramInfoQuery(ProgramInfoQuery programInfoQuery) {
		if (this.programInfoQuerys == null) {
			programInfoQuerys = new ArrayList<ProgramInfoQuery>();
		}
		programInfoQuerys.add(programInfoQuery);
	}

	public List<ProgramInfoQuery> getProgramInfoQuery() {
		return programInfoQuerys;
	}

	public void addCommand(Command Command) {
		if (this.commands == null) {
			commands = new ArrayList<Command>();
		}
		commands.add(Command);
	}

	public List<Command> getCommand() {
		return commands;
	}

//	public void addReviseTime(ReviseTime ReviseTime) {
//		if (this.reviseTimes == null) {
//			reviseTimes = new ArrayList<ReviseTime>();
//		}
//		reviseTimes.add(ReviseTime);
//	}
//
//	public List<ReviseTime> getReviseTime() {
//		return reviseTimes;
//	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
