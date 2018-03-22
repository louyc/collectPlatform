package com.neusoft.np.arsf.service.avro;

import java.util.List;

public interface IAvroSyncProducer {

	/**
	 * 同步发送，等待返回值
	 */
	EventStatus sendSyncEvent(AvroEvent event) throws AvroNetException;

	/**
	 * 同步发送，等待返回值
	 */
	EventStatus sendSyncEvent(List<AvroEvent> events) throws AvroNetException;

}
