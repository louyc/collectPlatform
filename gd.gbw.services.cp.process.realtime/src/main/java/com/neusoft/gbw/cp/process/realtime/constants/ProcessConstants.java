package com.neusoft.gbw.cp.process.realtime.constants;

public class ProcessConstants {

	public static final int DATA_RECOVERY_QUEUE_SIZE = 10000;
	
//	public static final int STREAM_QUEUE_SIZE = 10;
	
	public static final String REAL_TIME_PROCESS_SCOPE = "realtime_process";
	
	public static final String SRC_CODE= "realtime";
	
	public static final int REAL_TIME_THREAD_NUM = 15;
	
	public static final String JMS_SEND_MSG_TOPIC = "JMS_SEND_MSG_TOPIC";
	
	public static final int TAI_SHUN_MANUFACTURER_ID = 2;
	
	public static final String IS_FORCE = "1"; //强制占用
	
	public static final String NO_FORCE = "0"; //正常占用
	
	public static final String STREAM_TYPE = "0";//0：音频、1：录音
	
	public static final String RECORD_TYPE = "1";
	
	public static final String START_COMMAND = "Start";
	
	public static final String STOP_COMMAND = "Stop";
	
	public static final String IS_STREAM_TYPE = "0"; //音频
	
	public static final String IS_RECORD_TYPE = "1"; //录音
	
	public static final int TRANSFER_TIME_OUT_TIME = 15000;
	
	public static final String TASK_SEND_ERROR_TYPE = "1";
	
	public static final String TASK_SEND_ERROR_DESC = "系统连接站点通信失败";
	
	//播录音返回类型   0:正常、1：当前URL占用、2：其他占用、3：通信超时、4、其他异常、5、播音超时
	public static final String REPORT_SUCCESS = "0";
	
	public static final String REPORT_SUCCESS_DESC = "正常";
	
	public static final String URL_OCCUPY = "1";
	
	public static final String URL_OCCUPY_DESC = "当前播放地址url占用";
	
	public static final String OTHER_OCCUPY = "2";
	
	public static final String OTHER_OCCUPY_DESC = "其他占用";
	
	public static final String TRANSFER_TIME_OUT = "3";
	
	public static final String TRANSFER_TIME_OUT_DESC = "通信超时";
	
	public static final String OTHER_EXCEPTION = "4";
	
	public static final String OTHER_EXCEPTION_DESC = "录音数据异常";
	
	public static final String STREAM_LEAKAGE = "音频漏采";
	
	public static final String STREAM_TIME_OUT = "5";
	
	public static final String STREAM_TIME_OUT_DESC = "播音超时";
	
	public static final String RECORD_TIME_OUT_DESC = "录音超时";
	
	public static final String MANUAL_SUBPATH = "manual_subpath";
	
	public static final String RECORDING_STORE_ADDRESS = "recording_store_address";
	
	public static final String RECORDING_PLAY_ADDRESS = "recording_play_address";
	
	public static final String MEASURE_SUBPATH = "measure_subpath";
	
	public static final String COLLECT_SUBPATH = "collect_subpath";
	
	public static final int SOUND_RECORD_WAIT_TIME = 50;
	
	public static final int dataPoolInitialDelay = 1;
	
	public static final int dataPoolPeriod = 3;
	
	public static final String JMS_TYPE = "JMS";

	public static final String REST_TYPE = "REST";

	/**
	 * 存储主题
	 * @author jh
	 *
	 */
	public static class StoreTopic {
		
		public static final String UPDATE_MESURE_UNIT_STATUS_ID_STORE = "updateMesureUnitStatusIdStore";
		
		public static final String INSERT_MESURE_UNIT_STORE = "insertMesureUnitStore";
		
		public static final String UPDATE_MESURE_UNIT_URL_RECORDS = "updateMesureUnitURLRecords";

		public static final String INSERT_MEASURE_TASK_RECOVER_TIME = "insertManualTaskRecoverTime";

		public static final String DELETE_MEASURE_TASK_RECOVER_TIME = "deleteManualTaskRecoverTime";
	}
}
