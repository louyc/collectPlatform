package com.neusoft.gbw.cp.collect.recording.constant;

public class RecordConstants {

	public static final String CHECK_INFO = "<collect-stream-resquest/>";
	public static final String CONN_INFO = "<collect-stream-response/>";
	

	public static final String RECORD_TASK_QUEUE="录音任务线程";
	public static final int RECORD_TASK_QUEUE_SIZE = 5;
	
	public static final String RECORD_DATA_QUEUE="录音数据线程";
	public static final int RECORD_DATA_QUEUE_SIZE = 5;
	
	public static final String OPER_TYPE_MEASURE= "measure";
	public static final String OPER_TYPE_REALTIME= "realtime";
}
