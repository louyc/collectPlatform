package com.neusoft.gbw.cp.station.service.transfer;

public interface ITransfer {
	void init();
	
	void start();
	
	void stop();
	
	public void put(Object obj);
	
	public String take() throws InterruptedException;
}
