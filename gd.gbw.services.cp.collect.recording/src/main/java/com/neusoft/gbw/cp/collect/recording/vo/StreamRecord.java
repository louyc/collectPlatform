package com.neusoft.gbw.cp.collect.recording.vo;

import java.util.List;

public class StreamRecord {
	
	private List<byte[]> stream;
	private int recordLength;
	private int recordStatus;
	
	public List<byte[]> getStream() {
		return stream;
	}
	public void setStream(List<byte[]> stream) {
		this.stream = stream;
	}
	public int getRecordLength() {
		return recordLength;
	}
	public void setRecordLength(int recordLength) {
		this.recordLength = recordLength;
	}
	public int getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(int recordStatus) {
		this.recordStatus = recordStatus;
	}
	
}
