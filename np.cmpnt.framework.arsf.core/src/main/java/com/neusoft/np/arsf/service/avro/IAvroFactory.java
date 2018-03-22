package com.neusoft.np.arsf.service.avro;

public interface IAvroFactory {

	IAvroSyncProducer createSyncProducer(String host, int port) throws AvroNetException;

	IAvroSyncProducer createSyncProducer(EventTarget target) throws AvroNetException;

	IAvroASyncProducer createASyncProducer(String host, int port) throws AvroNetException;

	IAvroASyncProducer createASyncProducer(EventTarget target) throws AvroNetException;

	// IAvroSyncProducer createProducer(List<EventTarget> targets) throws AvroNetException;

}
