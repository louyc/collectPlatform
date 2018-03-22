package com.neusoft.gbw.cp.conver.v5.protocol.vo.other;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 49.命令子元素
 * @author Administrator
 *
 */
@XStreamAlias("Command")
public class Command {
	@NotNull(message = "Name不能为空!")
	@Pattern(regexp="(^\\S*)(\\S$)",message="Name-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Name")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
