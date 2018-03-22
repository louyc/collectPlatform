package com.neusoft.np.arsf.service.avro;

import java.util.List;

public interface IAvroASyncProducer {

	/**
	 * 异步发送，方法无反馈消息
	 */
	void sendEvent(AvroEvent event);

	/**
	 * 异步发送，方法无反馈消息
	 */
	void sendEvent(List<AvroEvent> events);

}
