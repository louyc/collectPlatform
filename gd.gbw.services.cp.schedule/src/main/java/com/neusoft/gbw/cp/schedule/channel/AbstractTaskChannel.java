package com.neusoft.gbw.cp.schedule.channel;

import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

public abstract class AbstractTaskChannel implements Channel {

	private BlockingQueue<CollectTask> dataQueue = null;
	protected ChannelType type;
	private int size = 10000;
	
	public void init() {
		dataQueue = ARSFToolkit.getBlockingQueue(type.name() + "_queue", size);
	}
	
	public AbstractTaskChannel(ChannelType type) {
		this.type = type;
	}

	public void put(Object obj) {
		if (obj instanceof CollectTask) {
			try {
				dataQueue.put((CollectTask)obj);
			} catch (InterruptedException e) {
			}
			//添加捕捉异常
			catch(ClassCastException e){
				Log.debug("ClassCastException  类型转换异常        AbstractTaskChannel");
			}catch(IllegalArgumentException e){
				Log.debug("IllegalArgumentException 不合法 不合适参数异常      AbstractTaskChannel");
			}catch(NullPointerException e){
				Log.debug("NullPointerException     空指针异常        AbstractTaskChannel");
			}
		}
	}
	
	public CollectTask take() throws InterruptedException {
		
		return dataQueue.take();
	}
	//添加 try cath
	public void clear() {
		try{
		this.dataQueue.clear();
		}catch(UnsupportedOperationException e){
			Log.debug("UnsupportedOperationException  不支持操作 功能 异常      AbstractTaskChannel");
		}
	}
}
