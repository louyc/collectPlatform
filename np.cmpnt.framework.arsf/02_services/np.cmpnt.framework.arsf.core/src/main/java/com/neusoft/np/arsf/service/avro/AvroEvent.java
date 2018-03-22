package com.neusoft.np.arsf.service.avro;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class AvroEvent {

	private Map<CharSequence, CharSequence> header = new HashMap<CharSequence, CharSequence>();
	private ByteBuffer body;

	public void addHeader(String name, String value) {
		header.put(name, value);
	}

	public void addHeader(Map<? extends CharSequence, ? extends CharSequence> map) {
		header.putAll(map);
	}
	
	public void setBody(byte[] body) {
		this.body = ByteBuffer.wrap(body);
	}

	@Override
	public String toString() {
		return "NPAvroEvent [header=" + header + "]";
	}

	public ByteBuffer getBody() {
		return body;
	}

	public void setBody(ByteBuffer body) {
		this.body = body;
	}

	public Map<CharSequence, CharSequence> getHeader() {
		return header;
	}

	public void setHeader(Map<CharSequence, CharSequence> header) {
		this.header = header;
	}
}
