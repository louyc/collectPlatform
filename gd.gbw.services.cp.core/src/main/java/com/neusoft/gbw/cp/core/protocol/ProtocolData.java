package com.neusoft.gbw.cp.core.protocol;

public class ProtocolData {

	private Object query;
	private int proVersion;
	private ProtocolType type;	    //协议类型
	private ProtocolType oriType;  //原始协议类型
	
	public Object getQuery() {
		return query;
	}
	public void setQuery(Object query) {
		this.query = query;
	}
	public int getProVersion() {
		return proVersion;
	}
	public void setProVersion(int proVersion) {
		this.proVersion = proVersion;
	}
	public ProtocolType getType() {
		return type;
	}
	public void setType(ProtocolType type) {
		this.type = type;
	}
	public ProtocolType getOriType() {
		return oriType;
	}
	public void setOriType(ProtocolType oriType) {
		this.oriType = oriType;
	}
}
