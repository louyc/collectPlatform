package com.neusoft.gbw.cp.core.collect;

import java.util.List;

public class CollectAttrInfo {

	private TransferType transferType;	//通信类型
	private TransferAttr transferAttr;	//通信参数
//	private OccupEquType occupEquType;	//是否占用接收机
	private CollectTaskType colTaskType;//采集任务类型，一般任务，关闭任务
	private String firstEquCode;  		//首选接收机，不为空
	private List<String> equCodeList;	//接收机编码,默认可用多个接收机
	private int transferTimeOut;        //通信超時(秒)
	private int equTimeOut;				//接收机占用超时(秒)
	private boolean isCheckOccpy;       //是否检测站点接收机占用
	
	public TransferType getTransferType() {
		return transferType;
	}
	public void setTransferType(TransferType transferType) {
		this.transferType = transferType;
	}
	public TransferAttr getTransferAttr() {
		return transferAttr;
	}
	public void setTransferAttr(TransferAttr transferAttr) {
		this.transferAttr = transferAttr;
	}
	public CollectTaskType getColTaskType() {
		return colTaskType;
	}
	public void setColTaskType(CollectTaskType colTaskType) {
		this.colTaskType = colTaskType;
	}
	public List<String> getEquCodeList() {
		return equCodeList;
	}
	public void setEquCodeList(List<String> equCodeList) {
		this.equCodeList = equCodeList;
	}
	public int getTransferTimeOut() {
		return transferTimeOut;
	}
	public void setTransferTimeOut(int transferTimeOut) {
		this.transferTimeOut = transferTimeOut;
	}
	public String getFirstEquCode() {
		return firstEquCode;
	}
	public void setFirstEquCode(String firstEquCode) {
		this.firstEquCode = firstEquCode;
	}
	public boolean isCheckOccpy() {
		return isCheckOccpy;
	}
	public void setCheckOccpy(boolean isCheckOccpy) {
		this.isCheckOccpy = isCheckOccpy;
	}
	public int getEquTimeOut() {
		return equTimeOut;
	}
	public void setEquTimeOut(int equTimeOut) {
		this.equTimeOut = equTimeOut;
	}
//	public OccupEquType getOccupEquType() {
//		return occupEquType;
//	}
//	public void setOccupEquType(OccupEquType occupEquType) {
//		this.occupEquType = occupEquType;
//	}
	@Override
	public String toString() {
		return "CollectAttrInfo [transferType=" + transferType
				+ ", transferAttr=" + transferAttr + ", firstEquCode="
				+ firstEquCode + ", equCodeList=" + equCodeList
				+ ", transferTimeOut=" + transferTimeOut + ", equTimeOut="
				+ equTimeOut + ", isCheckOccpy=" + isCheckOccpy + "]";
	}
}
