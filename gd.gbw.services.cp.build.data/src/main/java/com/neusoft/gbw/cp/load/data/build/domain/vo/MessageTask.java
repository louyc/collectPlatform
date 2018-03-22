package com.neusoft.gbw.cp.load.data.build.domain.vo;

public class MessageTask {
	
	private MessageType type;
	private Object object;
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
}
