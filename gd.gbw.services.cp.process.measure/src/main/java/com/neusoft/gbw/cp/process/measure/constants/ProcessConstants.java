package com.neusoft.gbw.cp.process.measure.constants;

public class ProcessConstants {

	public static final int DATA_RECOVERY_QUEUE_SIZE = 10000;
	
	public static final String MEASURE_PROCESS_SCOPE = "measure_process";
	
	public static final String SRC_CODE= "measure";
	
	public static final int STORE_MESURE_UNIT_WAIT_TIME = 3000;
	
	public static final int TASK_STATUS_NOT_OVER_WAIT_TIME = 5000;
	
	public static final int ONLINE_LISTENER_TASK_WAIT_TIME = 50000;
	
	public static final int IS_TAISHUN_MONITOR = 2;
	
	public static final String JMS_SEND_MSG_TOPIC = "JMS_SEND_MSG_TOPIC";
	
	public static final String JMS_RECEIVE_EVALUATION_MSG_TOPIC = "JMS_RECEIVE_EVALUATION_MSG_TOPIC";
	
	public static final String JMS_SEND_EVALUATION_MSG_TOPIC = "JMS_SEND_EVALUATION_MSG_TOPIC";
	
	public static final String JMS_SEND_RECORD_MONITOR_MSG_TOPIC = "JMS_SEND_RECORD_MONITOR_MSG_TOPIC";
	
	public static final String ONLINE_LISTENER_OPERATION_TABLE_NAME = "gbta_online_exp_info_v";
	
	public static final int C_JMS_EVAL_RECORD_REQUEST_MSG = 3000;
	
	public static final int C_JMS_EVAL_RECORD_RESPONSE_MSG = 3001;
	
	public static final String MANUAL_SUBPATH = "manual_subpath";
	
	public static final String RECORDING_HTTP_ADDRESS = "recording_http_address";
	
	public static final String RECORDING_STORE_ADDRESS = "recording_store_address";
	
	public static final String RECORDING_PLAY_ADDRESS = "recording_play_address";
	
	public static final String COLLECT_SUBPATH = "collect_subpath";
	
	public static final String MEASURE_SUBPATH = "measure_subpath";

	public static final int SET_RECOVER_THREAD_NUM = 50;

	//任务量多时 在创建录音任务  到发送录音任务成功 耗时12s左右   实际等待录音平台录音超时时间就30s多  容易造成超时  20160802   50s--->90s---->120s   
	public static final int SOUND_RECORD_WAIT_TIME = 120;    

	public static final int RECORD_TASK_TIME_LENGTH = 30;
	
	public static final int RECOVER_DATA_DEL_WAIT_TIME = 3000;
//	//实时收测设备key
//	public static final String REAL_MEASURE_SITE_IDS = "REAL_MEASURE_SITE_IDS";
	
	public static final int dataPoolInitialDelay = 1;
	
	public static final int dataPoolPeriod = 3;
	
	public static final int MANUAL_RECOVER_TASK_TIME_OUT = 360000;
	
	public static final String TASK_RECOVER_SUCCESS = "0";  
	public static final String TASK_RECOVER_TIME_OUT = "2";  
	public static final String TASK_RECOVER_FAUIL= "1";  
	
	
	public static class LeakageReason {
		
		public static final String DATE_COLLECT_SUCCESS = "数据采集成功";
		
		public static final String DATE_COLLECT_FAUIL = "数据采集失败";
		
		public static final String RECORD_FILE_COLLECT_SUCCESS = "录音文件采集成功";
		
		public static final String RECORD_FILE_CREATE_FAUIL = "录音文件生成失败";
		
		public static final String DATE_COLLECT_TIME_OUT = "数据采集超时";
		
		public static final String DATE_NO_COLLECT_TIME_OUT = "数据未采集超时";
		
		public static final String DATE_COLLECT_ERROR = "数据采集错误，解析返回结果失败";
		
		public static final String DATE_COLLECT_NULL = "数据返回为空";
		
		public static final String DATE_COLLECT_ACTIVE_REPORT = "数据主动上报";
		
		public static final String DATE_COLLECT_NO_FOUND_TASK = "未找到数据采集任务";
		
		public static final String COLLECT_TASK_ANALYTICAL_FAILURE = "采集任务解析失败";
		
		public static final String COLLECT_TASK_SEND_FAILURE = "采集任务发送失败";
		
		public static final String TRANSFER_BASE_INFO_VALIDATE_FAILURE = "通讯信息验证失败";
		
		public static final String COLLECT_URL_ANALYTICAL_FAILURE = "音频路径解析失败";
		
		public static final String COLLECT_FILE_SIZE_NOT_ENOUGH = "音频文件大小不足";
	}
	
	/**
	 * 存储主题
	 * @author jh
	 *
	 */
	public static class StoreTopic {
		
		public static final String INSERT_MEASURE_MANUAL_UNIT_STORE = "insertMeasureManualUnitStore";
		
		public static final String INSERT_MEASURE_AUTO_UNIT_STORE = "insertMeasureAutoUnitStore";  //创建自动收测单元
		
		public static final String UPDATE_MEASURE_MANUAL_UNIT_STATUS = "updateMeasureManualUnitStatus";//手动   之前数据更新成4
		
		public static final String UPDATE_MEASURE_AUTO_UNIT_STATUS = "updateMeasureAutoUnitStatus";  //自动实时采集  之前数据更新成4

		public static final String UPDATE_MEASURE_AUTO_FAIL_STATUS = "updateMeasureAutoFailStatus";  //自动实时采集  之前数据更新成4   采集原因       没人调用
		
		public static final String UPDATE_MEASURE_AUTO_SCORE_STATUS = "updateMeasureAutoScoreStatus";  //  没人调用  
		
		public static final String UPDATE_MEASURE_MANUAL_UNIT_URL_RECORDS = "updateMeasureManualUnitURLRecords";//手动1  更新 待评估  采集原因

		public static final String UPDATE_MEASURE_AUTO_UNIT_URL_RECORDS = "updateMeasureAutoUnitURLRecords";  //自动1  更新 待评估   采集原因
		
		public static final String UPDATE_MEASURE_ONLINE_UNIT_URL_RECORDS = "updateMeasureOlineUnitURLRecords"; //不更新状态

		public static final String UPDATE_MEASURE_AUTO_SAFE_ASSESS_STATUS = "updateMeasureAutoSafeAssessStatus"; // 正常采集  -1   自动 评估失败
		
		public static final String UPDATE_MEASURE_MANUAL_SAFE_ASSESS_STATUS = "updateMeasureManualSafeAssessStatus"; //正常采集   -1  评估失败
		
		public static final String UPDATE_MEASURE_AUTO_LEAKAGE_REASON_STATUS = "updateMeasureAutoLeakageReasonStatus";  //第二次采集 -1   自动 更新为评估失败   采集原因
		
		public static final String UPDATE_MEASURE_MANUAL_LEAKAGE_REASON_STATUS = "updateMeasureManualLeakageReasonStatus";//第二次采集 -1 手动  评估失败   采集原因

		public static final String UPDATE_MANUAL_TASK_RECOVER_TIME = "updateManualTaskRecoverTime";
		
		public static final String UPDATE_RECORD_FILE_URL = "updateRecordFileUrl";
		// 遥控站巡检   更新
		public static final String UPDATE_AUTO_STREAM_URL_RECORDS = "updateAutoStreamUnitURLRecords";// 遥控站巡检  1  更新 待评估  采集原因
		public static final String UPDATE_AUTO_STREAM_STATUS ="updateAutoSteamStatus"; //-1   自动 评估失败;    没用
		public static final String UPDATE_AUTO_STREAM_REASON_STATUS = "updateAutoStreamReasonStatus";//第二次采集 -1 手动  评估失败   采集原因
		public static final String UPDATE_MONITOR_SUC_STATE = "updateMoniTransferResultSucStore";    //更新成功站点状态
		public static final String UPDATE_MONITOR_FAIL_STATE = "updateMoniTransferResultFailStore";  //更新失败站点状态
		public static final String UPDATE_INSPECT_RESULT = "updateInspectResultStore";               //更新巡检结果表
		
	}
}
