package com.neusoft.gbw.cp.build.interfaces.event.handler;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.base.bundle.NPBaseConstant;

public class ThreadListenerHandler implements BaseEventHandler{

	@Override
	public String getTopicName() {
		return NPBaseConstant.EventTopic.THREAD_THROWABLE_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if (arg0 instanceof String) {
			dispose((String)arg0);
		}
		return true;
	}
	
	private void dispose(String msg) {
		Log.debug("采集平台被强制关闭>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	    String name = ManagementFactory.getRuntimeMXBean().getName();
	    String pid = name.split("@")[0];
//	    String command = "cmd.exe /c ntsd -c q -p " + pid;
	    String command = "cmd.exe /c taskkill /F /PID " + pid;
	    try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			e.printStackTrace();
			Log.debug("错误类：ThreadListenerHandler  异常   : IOException  ");
		}catch(Exception e){
			Log.debug("错误类：ThreadListenerHandler  异常: IllegalArgumentException SecurityException NullPointerException  ");
		}
	}
	
}
