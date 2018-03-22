package com.neusoft.np.arsf.service.avro;

public class EventTarget {

	private String host;
	private int port;

	public EventTarget() {
	}

	public EventTarget(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public EventTarget host(String host) {
		this.host = host;
		return this;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "EventTarget [host=" + host + ", port=" + port + "]";
	}

	public String getName() {
		return "host-" + host + ".port-" + port;
	}
}
