package com.neusoft.gbw.cp.schedule.channel;

public interface Channel {
	
	public void init();

	public void open();
	
	public void close();
	
	public void put(Object obj);
	
	public Object take() throws InterruptedException;
}
