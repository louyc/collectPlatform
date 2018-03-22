package com.neusoft.gbw.cp.build.infrastructure.constants;

public class BuildConstants {

	public static final int XML_VERSION_5 = 5;
	
	public static final int XML_VERSION_6 = 6;
	
	public static final int XML_VERSION_7 = 7;
	
	public static final int XML_VERSION_8 = 8;
	
	//上下行
	public static final String MSG_DOWN_TYPE = "RadioDown";
	public static final String MSG_UP_TYPE = "RadioUp";
	
//	//数据中心编码 
//	public static final String MSG_SRC_CODE = "NEU116";
	
	//波段  0短波,1中波,2调频
	public static final String CHANNEL_BAND_SHORT = "0";
	public static final String CHANNEL_BAND_CENTRE = "1";
	public static final String CHANNEL_BAND_FREQ = "2";
	
	//收测任务操作指令：设置、删除
	public static final String ACTION_MEASURE_SET = "Set";
	public static final String ACTION_MEASURE_DEL = "Del";
	
	//Socket下行通信端口
	public static final int TRANFER_SOCKET_PORT = 800;
	
	//频段标识
	public static final int SPECTRUM_TASK = 0;
	
	//周期类型 独立、周期
	public static final int CYCLE_TYPE = 2;
	public static final int SINGLE_TYPE = 1;
	//每天
	public static final String EVERY_DAY = "-1";
	
	//检修类型
	public static final int OVERHAUL_TYPE = 1;
	
	//任务类型ID  3:广播，4：实验，5：地方三满，6：频谱
	public static final int INTEGRATE_TASK_TYPE = 5;
	public static final int SPECTRUM_TASK_TYPE = 6;
	
	//质量任务类型
	public static final int QUALITY_KPI_TYPE = 1;
	public static final int QUALITY_OFFSET_TYPE = 2;
	public static final int QUALITY_ALL_TYPE = 3;
	//下发同步状态
	public static final int SYNT_SET_OVER_STATUS = 1;
	
	//最高优先级
	public static final String MAX_PRIORITY = "100000";
	
	public static final int QUEUE_SIZE = 10000;
	public static final String REQUEST_THREAD_NAME = "REQUEST_MSG_THREAD";
	//采集点的手动设置任务上报属性
	public static final String AUTO_REPORT_MODE = "1";//主动上报
	public static final String NO_REPORT_MODE = "2";//不主动上报
	public static final String WAIT_TIME = "00:00:00";
	
	//任务来源类型
	public static final int MANUANL = 0;
	public static final int BROADCAST = 3;
	public static final int EXPRIMENT = 4;
	public static final int OVERSEAS = 7;
	//音频的开始结束标识
	public static final String STREAM_START = "Start";
	public static final String STREAM_STOP = "Stop";
	
	//默认应用的接收机
	public static final int RECEIVE_DEFAULT = 1;
	
	//时间提醒消息主题
	public static final String REMIND_TIME_TOPIC = "REMIND_TIME_TOPIC";
	
	public static final String PROTOCOL_SOCKET = "socket";
	public static final String PROTOCOL_SERVLET = "servlet";
	
	//泰顺厂商ID
	public static final int TAI_SHUN_MANUFACTURER_ID = 2;
	//实时收测设备key
	public static final String REAL_MEASURE_SITE_IDS = "REAL_MEASURE_SITE_IDS";
	
	//时间间隔标签
	public static final String RUNPLAN_CNR_INTERVAL = "cp.evalutaion.cnrInterval";
	public static final String RUNPLAN_OPP_INTERVAL = "cp.evalutaion.oppInterval";
	public static final String RUNPLAN_DEFAULT_INTERVAL = "cp.evaluation.defaultInterval";
	
	public static final String JMS_RECEIVE_MSG_TOPIC = "JMS_RECEIVE_MSG_TOPIC";

	public static final String SWITCH_TYPE = "switch_type";
	
	public static final String SWITCH_VALUE = "switch_value";
	
	public static final String RUNPLAN_ID = "runplan_id";
	
	public static final String STATION_NAME = "station_name";
	
	//V7音频文件录音编码方式
	public static final String MPEG2_ENCODE = "MPEG2";
	//V8音频文件录音编码方式
	public static final String PCM_ENCODE = "PCM";
	
	public static final String V8_BPS = "176400";
	
	public static final String V5_BPS = "32000";
	
	public static final String IS_FORCE = "1"; //强制占用
	
	public static final String NO_FORCE = "0"; //正常
	
	public static final int OCCUP_TIME_OUT = 10; //接收机占用超时
	
	public static final String NO_COLLECT_SITE= "NO_COLLECT_SITE";
	
	public static final String TASK_SAMPLE_CIRCLE = "taskSampleCircle";
	
	public static final String TASK_SAMPLE_NUMBER = "taskSampleNumber";
	
	public static final String SAMPLE_CIRCLE = "sample_circle";
	
	public static final String SAMPLE_NUMBER = "sample_number";
	
	public static final String TASK_RECORDLENGTH_LISTEN = "taskRecordLength_listen";

	public static final String COLLECT_TASK_BPS = "taskBps_gather";
	
	public static final String RECEIVE_TASK_BPS = "taskBps_listen";
	
	public static final String MONITOR_SOFT_ONLINE_STATUS = "MONITOR_SOFT_ONLINE_STATUS";
	public static final String MONITOR_INSPECT_STATUS = "MONITOR_INSPECT_STATUS";
	
	public static final class TaskBuild {
		public static final int START_PLATFORM_BUILD = 1;
		public static final int START_PERIOD_BUILD = 2;
		public static final int START_BURST_BUILD = 3;
		public static final int START_LEAKAGE_BUILD = 4;
		public static final int START_PERIOD_DEL_TASK_BUILD = 5;
	}
	
	public static final class EquAlarmParamSetTask {
		public static final String ELECTRICITY_EXCEPTION_TYPE = "1";
		public static final String ELECTRICITY_EXCEPTION_DESC = "供电异常";
		
		public static final String RECEIVER_EXCEPTION_TYPE = "2";
		public static final String RECEIVER_EXCEPTION_DESC = "接收机异常";
		
		public static final String AM_MODULATION_EXCEPTION_TYPE = "3";
		public static final String AM_MODULATION_EXCEPTION_DESC = "调幅度卡异常";
		
		public static final String FM_MODULATION_EXCEPTION_TYPE = "4";
		public static final String FM_MODULATION_EXCEPTION_DESC = "调制度卡异常";
		
		public static final String VOICE_EXCEPTION_TYPE = "5";
		public static final String VOICE_EXCEPTION_DESC = "语音压缩卡异常";
		
		public static final String FREQ_DEV_EXCEPTION_TYPE = "6";
		public static final String FREQ_DEV_EXCEPTION_DESC = "频偏卡异常";
		
		public static final String INPUTLINELEVELUPTHRESHOLD = "InputLineLevelUpThreshold";
		public static final String INPUTLINELEVELDOWNTHRESHOLD = "InputLineLevelDownThreshold";
		public static final String ABNORMITYLENGTH = "AbnormityLength";
		
	}
	
	public static final class QualityAlarmParamSetTask {
		public static final String LEVEL_TYPE = "1";
		public static final String LEVEL_DESC = "Level";
		
		public static final String FM_MODULATION_TYPE = "2";
		public static final String FM_MODULATION_DESC = "FM-Modulation";
		
		public static final String AM_MODULATION_TYPE = "3";
		public static final String AM_MODULATION_DESC = "AM-Modulation";
		
		public static final String ATTENUATION_TYPE = "4";
		public static final String ATTENUATION_DESC = "Attenuation";
		
		public static final String BASE_DOWNTHRESHOLD = "DownThreshold";
		public static final String BASE_ABNORMITYLENGTH = "AbnormityLength";
		public static final String BASE_SAMPLELENGTH = "SampleLength";
		public static final String BASE_UPTHRESHOLD = "UpThreshold";
		public static final String BASE_DOWNABNORMITYRATE = "DownAbnormityRate";
		public static final String BASE_UPABNORMITYRATE = "UpAbnormityRate";
		
		public static final String LEVELDOWNTHRESHOLD = "levelDownThreshold";
		public static final String LEVELABNORMITYLENGTH = "levelAbnormityLength";
		
		public static final String FMMODULATIONSAMPLELENGTH = "fmModulationSampleLength";
		public static final String FMMODULATIONDOWNTHRESHOLD = "fmModulationDownThreshold";
		public static final String FMMODULATIONUPTHRESHOLD = "fmModulationUpThreshold";
		public static final String FMMODULATIONABNORMITYLENGTH = "fmModulationAbnormityLength";
		public static final String FMMODULATIONDOWNABNORMITYRATE = "fmModulationDownAbnormityRate";
		public static final String FMMODULATIONUPABNORMITYRATE = "fmModulationUpAbnormityRate";
		
		public static final String AMMODULATIONDOWNTHRESHOLD = "amModulationDownThreshold";
		public static final String AMMODULATIONSAMPLELENGTH = "amModulationSampleLength";
		public static final String AMMODULATIONUPTHRESHOLD = "amModulationUpThreshold";
		public static final String AMMODULATIONDOWNABNORMITYRATE = "amModulationDownAbnormityRate";
		public static final String AMMODULATIONUPABNORMITYRATE = "amModulationUpAbnormityRate";
		public static final String AMMODULATIONABNORMITYLENGTH = "amModulationAbnormityLength";
		public static final String ATTENUATION = "Attenuation";
		
	}
	
	public static final class CollectTaskExp {
		public static final String STREAM_URL = "URL";
	}
	
	public static final class QualityTask {
		
		public static final String LEVEL_KPI = "taskLevel";
		public static final String AM_MODULATION_KPI = "taskAMModulation";
		public static final String FM_MODULATION_KPI = "taskFMModulation";
		public static final String BANDWIDTH_KPI = "taskBandwidth";
		//V5版本调幅度，调制度百分比
		public static final String AM_MODULATION_RATE = "AMModulationRate";
		public static final String FM_MODULATION_RATE = "FMModulationRate";
		public static final String ATTENUATION = "Attenuation";
		
		
		public static final String QUAL_TIME_INTERVAL = "taskSleepTime_index";
		public static final String OFFSET_TIME_INTERVAL = "taskSleepTime_offset";
		public static final String OFFSET_TASK = "taskFreqOffset";
	}
	
	public static final class EffectTask {
		
		public static final String COLLECT_STREAM_TASK = "taskCollectRecord";
		public static final String RECIEVER_STREAM_TASK = "taskReceiveRecord";
		public static final String EFFECT_TIME_INTERVAL = "taskSleepTime_gather";
		public static final String EFFECT_RECORDLENGTH = "taskRecordLength_gather";
	}
	
	public static final class SpectrumTask {
		
		public static final String SPEC_TIME_INTERVAL = "taskSleepTime_step";
		public static final String SPEC_FREQ_STEP = "taskFreqStep";
	}
	
	public static final class RestType {
		
		public static final String REST_REALTIME_STREAM = "getRealTimeStream";
		public static final String REST_REALTIME_RECORD = "getRealTimeRecord";
		public static final String REST_UPDATE_TASK = "editTaskEvents";
		public static final String REST_DELETE_TASK = "deleteTaskEvents";
		public static final String REST_SET_TASK = "sendTaskEvents";
		public static final String REST_RECOVER_TASK = "recoveryTaskEvents";
		public static final String REST_DROP_CLIENT_TASK = "dropMonitorClientLinks";
		public static final String REST_EQU_PARAM_SET_TASK = "equParamSetEvents";
		public static final String REST_EQU_INIT_SET_TASK = "equInitSetEvents";
		public static final String REST_QUALITY_PARAM_SET_TASK = "qualityParamSetEvents";
	}
}
