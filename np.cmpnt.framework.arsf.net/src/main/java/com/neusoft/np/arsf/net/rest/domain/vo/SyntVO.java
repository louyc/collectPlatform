package com.neusoft.np.arsf.net.rest.domain.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SyntVO {

	private String taskId;

	private String typeId;

	private String request;

	private String response;

	public SyntVO() {
	}

	public SyntVO(String taskId, String typeId, String request, String response) {
		this.taskId = taskId;
		this.typeId = typeId;
		this.request = request;
		this.response = response;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

}
