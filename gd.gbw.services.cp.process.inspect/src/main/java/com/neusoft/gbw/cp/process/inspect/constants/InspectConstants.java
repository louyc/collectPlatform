package com.neusoft.gbw.cp.process.inspect.constants;

public class InspectConstants {
	
	public static final int INSPECT_TASK_DISPOSE_THREAD_NUM = 15;
	
	public static final String MONITOR_SOFT_ONLINE_STATUS = "MONITOR_SOFT_ONLINE_STATUS";//
	
	public static final String JMS_SEND_MSG_TOPIC = "JMS_SEND_MSG_TOPIC";

	public static final String INSPECT_TASK_PROCESS_SCOPE = "inspect_process";

	public static final int INSPECT_TASK_SUCCESS  = 0; //巡检通过
	
	public static final int INSPECT_TASK_FAILURE  = 1; //巡检不通过
	
	public static final int INSPECT_TASK_TIME_OUT  = 2; //巡检超时

	public static final int INSPECT_TASK_NULL  = 3; //空
	
	public static final String INSERT_INSPECT_RESULT_STORE = "insertInspectResultStore";
	
	public static final String UPDATE_MONI_TRANSFER_RESULT_STORE = "updateMoniTransferResultStore";
	
	public static final String UPDATE_MONI_STATUS_STORE = "updateMoniStatusStore";
	
	
	/**
	 * 巡检项
	 * @author jh
	 *
	 */
	public static class inspectProject {
		//设备状态
		public static final String EQU_UPS_CODE = "equUPS";
		public static final String EQU_RECEIVE_CODE = "equReceive";
		public static final String EQU_AM_CARD_CODE = "equAMCard";
		public static final String EQU_FM_CARD_CODE = "equFMCard";
		public static final String EQU_VOICE_CARD_CODE = "equVoiceCard";
		public static final String EQU_OFFSET_CARD_CODE = "equOffsetCard";
		
		//程序运行状态
		public static final String PROGRAM_VERSION_CODE = "programVersion";
		public static final String PROGRAM_RUNNING_CODE = "programRunning";
		//设备日志
		public static final String DEVICE_LOG = "deviceLog";
		//任务状态
		public static final String TASK_EXSIT_STATE_CODE  = "taskExsit";
		public static final String TASK_RUNNING_STATE_CODE  = "taskRunning";
		public static final String TOTAL_TASK_RUNNING_STATE_CODE  = "taskExecutiveState";
		public static final String QUALITY_TASK_RUNNING_STATE_CODE  = "taskQualityState";
		public static final String STREAM_TASK_RUNNING_STATE_CODE  = "taskStreamState";
		public static final String STREAM_VADIO_PLAY  = "streamVadioState";
		
		//网络连接
		public static final String NETWORK_CONNECT_CODE  = "networkConnect";

	}
}
