package com.neusoft.gbw.cp.load.data.build.infrastructure.constants;

public class BuildConstants {

	//每天
	public static final String EVERY_DAY = "-1";
	
	public static final String REQUEST_THREAD_NAME = "REQUEST_MSG_THREAD";
	
	//任务类型ID  3:广播，4：实验，5：地方三满，6：频谱
	public static final int INTEGRATE_TASK_TYPE = 5;
	public static final int SPECTRUM_TASK_TYPE = 6;
	
	public static final String REMIND_TIME_TOPIC = "REMIND_TIME_TOPIC";
	//PG 用 
	public static final String PROTOCOL_SERVLET = "servlet";
	//oracle 用
	//public static final String PROTOCOL_SERVLET = "SERVLET";
	
	public static final String REAL_MEASURE_SITE_IDS = "REAL_MEASURE_SITE_IDS";
	
// PG  用  ： selectRecordStoreAddrList key值大小写问题
 //	 public static final String SWITCH_TYPE = "switch_type";
 //	 public static final String SWITCH_VALUE = "switch_value";
	
//oracle 用  ：TaskDataMgr 类   selectRecordStoreAddrList key值大小写问题
    public static final String SWITCH_TYPE = "SWITCH_TYPE";
	
	public static final String SWITCH_VALUE = "SWITCH_VALUE";
	
	//PG 用  selectMeasureStationList key 值大小写 问题 
	//public static final String RUNPLAN_ID = "runplan_id";	
	//public static final String STATION_NAME = "station_name";
	
// oracle 用 ： selectMeasureStationList  key 值大小写 问题 
   public static final String RUNPLAN_ID = "RUNPLAN_ID";
	
	public static final String STATION_NAME = "STATION_NAME";
	
	
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
	
}
