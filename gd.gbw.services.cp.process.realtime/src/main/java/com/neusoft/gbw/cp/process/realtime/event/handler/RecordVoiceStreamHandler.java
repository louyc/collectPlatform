package com.neusoft.gbw.cp.process.realtime.event.handler;

import com.neusoft.gbw.cp.collect.stream.vo.VoiceStream;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.process.realtime.context.TaskProcessContext;
import com.neusoft.gbw.cp.process.realtime.service.condition.ConditionPoolException;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class RecordVoiceStreamHandler implements BaseEventHandler {
	
	private TaskProcessContext context = TaskProcessContext.getInstance();
	
	@Override
	public String getTopicName() {
		return EventServiceTopic.COLLECT_REALTIME_STREAM_RESPONSE_TOPIC;
	}

	@Override
	public boolean processEvent(Object arg0) {
		if(arg0 == null) 
			return false;
		if(arg0 instanceof VoiceStream) {
			VoiceStream stream = (VoiceStream)arg0;
			String id = stream.getInfo().getId();
//			VoiceStream saveStream = context.getTsp().getT(id);
			try {
				context.getTsp().update(id, stream);
				context.getCtp().signal(id);
			} catch (ConditionPoolException e) {
				Log.error(this.getClass().getName()+"唤醒休眠的线程失败", e);
			}
		}
		return false;
	}
}