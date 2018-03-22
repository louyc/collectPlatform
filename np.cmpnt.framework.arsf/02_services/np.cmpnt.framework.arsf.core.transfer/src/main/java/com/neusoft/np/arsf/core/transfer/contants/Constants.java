package com.neusoft.np.arsf.core.transfer.contants;

public interface Constants {

	String SERVICE_NAME = "[数据接收服务]";

	String BUSINESS_SCOPE = "cp_transfer";

	String RECEIVE_TYPE_JMS = "JMS";

	String RECEIVE_TYPE_SOCKET = "SOCKET";

	int SLEEP_TIME = 500;

	int SOCKET_CONNECTION_TIME = 5000;

	int RECONNECT_MAX_COUNT = 3;

	int RECONNECT_SLEEP_TIME = 500;

	int RECONNECT_TIME = 1000;

	/**
	 * Socket发送方式中socket类型
	 */
	int SOCKET_TYPE_SERVER = 0;
	int SOCKET_TYPE_CLIENT = 1;

	/**
	 * 数据接口类型
	 */
	String EVENT_TOPICS = "EVENT_TOPICS";
	String TOPICS = "TOPICS";
	String MESSAGE = "MESSAGE";

	/**
	 * JMS发送方式中JMS类型
	 */
	int JMS_TYPE_P2P = 0;
	int JMS_TYPE_TOPIC = 1;

	/**
	 * 通信类型
	 */
	String RECEIVE_TYPE = "receiveType";
	String COM_TYPE_JMS = "JMS";
	String COM_TYPE_SOCKET = "SOCKET";
}
