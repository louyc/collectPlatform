package com.neusoft.gbw.cp.process.measure.event.handler;

import com.neusoft.gbw.cp.collect.stream.vo.VoiceStream;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.process.measure.channel.condition.ConditionPoolException;
import com.neusoft.gbw.cp.process.measure.context.TaskProcessContext;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class RecordVoiceStreamHandler implements BaseEventHandler {
	
	private TaskProcessContext context = TaskProcessContext.getInstance();
	
	@Override
	public String getTopicName() {
		return EventServiceTopic.COLLECT_MEASURE_STREAM_RESPONSE_TOPIC;
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
				//解时间锁
				Log.debug("录音平台回来后 解锁的线程 音频流 id ："+id);
				context.getTsp().update(id, stream);
				context.getCtp().signal(id);
			} catch (ConditionPoolException e) {
				Log.error(this.getClass().getName()+"唤醒已经休眠的线程失败", e);
			}
		}
		return false;
	}
}