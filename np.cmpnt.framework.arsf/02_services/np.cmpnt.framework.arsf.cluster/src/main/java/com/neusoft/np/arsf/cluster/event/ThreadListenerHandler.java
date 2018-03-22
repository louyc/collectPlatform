package com.neusoft.np.arsf.cluster.event;

import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.base.bundle.NPBaseConstant;
import com.neusoft.np.arsf.cluster.context.FrameContext;
import com.neusoft.np.arsf.cluster.vo.PlatformStatus;

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
		FrameContext.getInstance().changePlatformStatus(PlatformStatus.DEAD);
		Log.debug("平台接收到业务线程崩溃，当前平台状态处于需要重启状态");
	}
	
}