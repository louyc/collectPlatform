package com.neusoft.gbw.cp.conver.v7.protocol.vo.other;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 47.获取文件名称子元素
 * @author Administrator
 *
 */
@XStreamAlias("File")
public class FileRetrieve_File {
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
