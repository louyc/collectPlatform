package com.neusoft.gbw.cp.collect.stream.vo;

import com.neusoft.gbw.cp.core.collect.CollectTask;

public class VoiceStream {

	private StreamParam info;
	private int dataLength;
	private int collectStatus;
	private CollectTask ct;
	
	public CollectTask getCt() {
		return ct;
	}
	public void setCt(CollectTask ct) {
		this.ct = ct;
	}
	public StreamParam getInfo() {
		return info;
	}
	public void setInfo(StreamParam info) {
		this.info = info;
	}
	public int getCollectStatus() {
		return collectStatus;
	}
	public void setCollectStatus(int collectStatus) {
		this.collectStatus = collectStatus;
	}
	public int getDataLength() {
		return dataLength;
	}
	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}
	
}
