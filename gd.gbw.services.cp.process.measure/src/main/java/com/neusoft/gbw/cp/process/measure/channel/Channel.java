package com.neusoft.gbw.cp.process.measure.channel;

public interface Channel {
	
	public ChannelType getType();
	
	public void init();

	public void open();
	
	public void close();
	
	public void put(Object obj);
	
	public Object take() throws InterruptedException;
}
