package com.neusoft.gbw.cp.station.service;

import com.neusoft.gbw.cp.station.vo.ProtocolType;

public class ProtocolData {

	private long msgID;
	private ProtocolType type;
	private String srcCode;
	private String dstCode;
	
	public long getMsgID() {
		return msgID;
	}
	public void setMsgID(long msgID) {
		this.msgID = msgID;
	}
	public ProtocolType getType() {
		return type;
	}
	public void setType(ProtocolType type) {
		this.type = type;
	}
	public String getSrcCode() {
		return srcCode;
	}
	public void setSrcCode(String srcCode) {
		this.srcCode = srcCode;
	}
	public String getDstCode() {
		return dstCode;
	}
	public void setDstCode(String dstCode) {
		this.dstCode = dstCode;
	}
	
}
