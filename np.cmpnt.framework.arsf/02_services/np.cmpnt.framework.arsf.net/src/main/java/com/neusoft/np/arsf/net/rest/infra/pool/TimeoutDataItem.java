package com.neusoft.np.arsf.net.rest.infra.pool;

public class TimeoutDataItem<T> {

	private long timestamp;

	private T data;

	public TimeoutDataItem() {
	}

	public TimeoutDataItem(T data) {
		this.timestamp = System.currentTimeMillis();
		this.data = data;
	}

	public boolean timeout(int timeout) {
		return (System.currentTimeMillis() - timestamp) >= timeout;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "TimeoutDataItem [timestamp=" + timestamp + ", data=" + data + "]";
	}

}
