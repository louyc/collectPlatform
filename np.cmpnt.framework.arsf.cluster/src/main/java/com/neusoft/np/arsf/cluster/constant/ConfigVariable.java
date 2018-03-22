package com.neusoft.np.arsf.cluster.constant;

public class ConfigVariable {

	//平台名称(任意字符串，只要集群的两个或多个平台名称重复即可)
	public static String PLATFORM_NAME;
	//平台集群通信地址字符串(指代浮动的IP地址) IP地址,掩码地址,网关地址
	public static String CLUSTER_VIRTUAL_INFO;
	//平台本地通信地址字符串(指Master与Slave本地的IP地址)  IP地址,掩码地址,网关地址
	public static String CLUSTER_LOCAL_INFO;
	//平台网卡名称(对应以上IP地址对应网卡的名称)
	public static String PLATFORM_INTERFACE_NAME;
	//平台主备份状态  Master：主平台；Slave：从动平台
	public static String PLATFORM_CLUSTER_TYPE;
	//当选择为Master类型，配置Master通信心跳端口
	public static int MASTER_TRANSFER_PORT = 0;
	//创建集群表结构
	public static String CLUSTER_SQL;
	//另一个平台名称
	public static String OTHER_PLATFORM_NAME;
}
