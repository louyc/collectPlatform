package com.neusoft.np.arsf.core.transfer.vo;

/**
* 项目名称: IT监管采集平台<br>
* 模块名称: 发送服务<br>
* 功能描述: 存储公共的常量<br>
* 创建日期: 2012-6-28 <br>
* 版权信息: Copyright (c) 2012<br>
* 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
* @author <a href="mailto: ma.zhj@neusoft.com">马仲佳</a>
* @version v1.0
* <pre>
* 修改历史
*   序号      日期          修改人       修改原因
*    1    2012-6-28下午12:56:28      马仲佳       创建
* </pre>
 */
public final class TransferConstants {
	
	public static final String SEND_SERVICE_NAME = "[数据发送服务]";
	
	public static final String REVCIEVE_SERVICE_NAME = "[数据接收服务]";
	
	/**
	 * 业务范围配置文件关键字常量
	 */
	public static final String SEND_BUSINESS_CONFIG_SCOPE = "arsf_send";
	
	public static final String RECEIVE_SEND_BUSINESS_CONFIG_SCOPE = "arsf_receive";

	public static final String RECEIVE_TYPE_JMS = "JMS";

	public static final String RECEIVE_TYPE_SOCKET = "SOCKET";

	public static final int SLEEP_TIME = 500;

	public static final int SOCKET_CONNECTION_TIME = 5000;

	public static final int RECONNECT_MAX_COUNT = 3;

	public static final int RECONNECT_SLEEP_TIME = 500;


	/**
	 * 数据接口类型
	 */
	public static final String EVENT_TOPICS = "EVENT_TOPICS";
	public static final String TOPICS = "TOPICS";
	public static final String MESSAGE = "MESSAGE";


	/**
	 * 通信类型
	 */
	public static final String RECEIVE_TYPE = "receiveType";
	public static final String COM_TYPE_JMS = "JMS";
	public static final String COM_TYPE_SOCKET = "SOCKET";
	
	public static final String TRANSFER_INIT_CONF_TOPIC = "RECEIVE_INIT_CONF";
	
	public static final String TRANSFER_MONITOR_STATUS_TOPIC = "TRANSFER_MONITOR_STATUS";
	
	public static final String SERVICE_ID = "arsf.core.send";
	
	
	public static final String TASK_SEPARATOR = "#";
	

	
	/**
	 * 管道中数据队列默认大小
	 */
	public static final int DEFAULT_QUEUE_SIZE = 10000;
	
	/**
	 * 本机IP地址常量
	 */
	public static final String LOCALHOST = "127.0.0.1";
	
	/**
	 * 重连间隔时间，单位是毫秒
	 */
	public static final long RECONNECT_TIME = 200;
	
	/**
	 * Socket发送方式中socket类型
	 */
	public static final int SOCKET_TYPE_SERVER = 0;
	public static final int SOCKET_TYPE_CLIENT = 1;
	
	/**
	 * Socket发送方式中连接类型
	 */
	public static final int SOCKET_CLIENT_CONNECT_TYPE_KEEP = 1;
	public static final int SOCKET_CLIENT_CONNECT_TYPE_SHORT = 0;
	
	/**
	 * JMS发送方式中JMS类型
	 */
	public static final int JMS_TYPE_P2P = 0;
	public static final int JMS_TYPE_TOPIC = 1;
	
	/**
	 * 数据类型
	 */
	public static final String DATAKIND_REAL = "REAL";
	public static final String DATAKIND_HIS = "HIS";
	
	/**
	 * 封装的数据发送结构中KPI_ITEM_KEY常量
	 */
	public static final String KPI_ITEM_KEY = "kpiItem";
	
	/**
	 * 发送类型
	 */
	public static final String SEND_TYPE = "sendType";
	public static final String SEND_TYPE_JMS = "JMS";
	public static final String SEND_TYPE_SOCKET = "SOCKET";
	
	/**
	 * 发送数据中必须属性
	 */
	static class SendHeadNeedProperty{
		
		/**
		 * 发送数据类型
		 */
		public static final String SEND_TYPE = "sendType";
		public static final String SEND_TYPE_JMS_P2P = "JMS_P2P";
		public static final String SEND_TYPE_JMS_TOPIC = "JMS_TOPIC";
		public static final String SEND_TYPE_SOCKET_SERVER = "SOCKET_SERVER";
		public static final String SEND_TYPE_SOCKET_CLIENT = "SOCKET_CLIENT";
		
		/**
		 * 必须属性罗列
		 */
		public static final String IP = "ip";
		public static final String PORT = "port";
		public static final String SESSIONNAME = "sessionName";
		public static final String TOPICNAME = "topicName";
		
		/**
		 * 发送数据类型（包括：字符串，字节数组，对象，三大类）
		 */
		public static final String SEND_DATA_TYPE = "sendDataType";
		public static final String SEND_DATA_TYPE_BYTE = "BYTE";
		public static final String SEND_DATA_TYPE_STRING = "STRING";
		public static final String SEND_DATA_TYPE_OBJECT = "OBJECT";

		/**
		 * 每种发送方式所需的必须参数字符串常量数组
		 */
		public static final String[] JMS_P2P = new String[]{IP,PORT,SESSIONNAME,SEND_DATA_TYPE};
		public static final String[] JMS_TOPIC = new String[]{IP,PORT,SESSIONNAME,TOPICNAME,SEND_DATA_TYPE};
		public static final String[] SOCKET_SERVER = new String[]{PORT,SEND_DATA_TYPE};
		public static final String[] SOCKET_CLIENT = new String[]{IP,PORT,SEND_DATA_TYPE};
		
	}
}
