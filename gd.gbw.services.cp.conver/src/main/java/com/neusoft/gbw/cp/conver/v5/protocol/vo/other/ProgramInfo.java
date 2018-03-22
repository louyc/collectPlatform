package com.neusoft.gbw.cp.conver.v5.protocol.vo.other;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.neusoft.gbw.cp.conver.util.ToStringStyleConfig;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 50.监测软件版本子元素
 * @author Administrator
 *
 */
@XStreamAlias("ProgramInfo")
public class ProgramInfo {
	@NotNull(message = "Company不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Company-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Company")
	private String company;
	@NotNull(message = "Name不能为空!")
	@Pattern(regexp="^[^\\s]+$",message="Name-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Name")
	private String name;
	@NotNull(message = "Version不能为空!")
	@Pattern(regexp="^[^\\s].+$",message="Version-首尾字符不能为空")
	@XStreamAsAttribute
	@XStreamAlias("Version")
	private String version;
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyleConfig.DEFAULT_STYLE);
	}
}
