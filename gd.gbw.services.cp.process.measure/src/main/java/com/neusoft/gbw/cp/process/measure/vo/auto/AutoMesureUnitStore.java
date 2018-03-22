package com.neusoft.gbw.cp.process.measure.vo.auto;

public class AutoMesureUnitStore {
	private String task_unit_id;
	private long task_id;
	private long monitor_id;
	private String runplan_id;
	private int runplan_type_id;
	private String unit_create_time;
	private String unit_begin;
	private String unit_end;
	private String unit_collect_time;
	private String radio_url;
	private String eval_url;
	private int unit_status_id;
	private String receiver_code;
	private int unit_interval;
	private String uncollect_reason;
	private int unit_marking_type;
	//运行图信息
	private long station_id;
	private int language_id;
	private String transmitter_no;
	private int freq;
	private int band;
	private String program_id;
	private String direction;
	private String start_time;
	private String end_time;
	private int radio_type_id;
	private int time_type_id;
	private int center_id;
	private int program_type_id;
	private int broadcast_type_id;
	private int task_type_id;
	private int service_area_id;
	private String service_area;
	private long orgMonitorId;       // 创建收测单元时的站点id
	
	public String getService_area() {
		return service_area;
	}
	public void setService_area(String service_area) {
		this.service_area = service_area;
	}
	public long getOrgMonitorId() {
		return orgMonitorId;
	}
	public void setOrgMonitorId(long orgMonitorId) {
		this.orgMonitorId = orgMonitorId;
	}
	private String monitor_alias;   //站点别名    多站点采集  lyc
	private String monitor_alias_code;//  站点编码
	
	public String getMonitor_alias_code() {
		return monitor_alias_code;
	}
	public void setMonitor_alias_code(String monitor_alias_code) {
		this.monitor_alias_code = monitor_alias_code;
	}
	public String getMonitor_alias() {
		return monitor_alias;
	}
	public void setMonitor_alias(String monitor_alias) {
		this.monitor_alias = monitor_alias;
	}
	public String getReceiver_code() {
		return receiver_code;
	}
	public void setReceiver_code(String receiver_code) {
		this.receiver_code = receiver_code;
	}
	public String getTask_unit_id() {
		return task_unit_id;
	}
	public void setTask_unit_id(String task_unit_id) {
		this.task_unit_id = task_unit_id;
	}
	public long getTask_id() {
		return task_id;
	}
	public void setTask_id(long task_id) {
		this.task_id = task_id;
	}
	public long getMonitor_id() {
		return monitor_id;
	}
	public void setMonitor_id(long monitor_id) {
		this.monitor_id = monitor_id;
	}
	public String getRunplan_id() {
		return runplan_id;
	}
	public void setRunplan_id(String runplan_id) {
		this.runplan_id = runplan_id;
	}
	public int getRunplan_type_id() {
		return runplan_type_id;
	}
	public void setRunplan_type_id(int runplan_type_id) {
		this.runplan_type_id = runplan_type_id;
	}
	public String getUnit_create_time() {
		return unit_create_time;
	}
	public void setUnit_create_time(String unit_create_time) {
		this.unit_create_time = unit_create_time;
	}
	public String getUnit_begin() {
		return unit_begin;
	}
	public void setUnit_begin(String unit_begin) {
		this.unit_begin = unit_begin;
	}
	public String getUnit_end() {
		return unit_end;
	}
	public void setUnit_end(String unit_end) {
		this.unit_end = unit_end;
	}
	public String getUnit_collect_time() {
		return unit_collect_time;
	}
	public void setUnit_collect_time(String unit_collect_time) {
		this.unit_collect_time = unit_collect_time;
	}
	public String getRadio_url() {
		return radio_url;
	}
	public void setRadio_url(String radio_url) {
		this.radio_url = radio_url;
	}
	public int getUnit_status_id() {
		return unit_status_id;
	}
	public void setUnit_status_id(int unit_status_id) {
		this.unit_status_id = unit_status_id;
	}
	public String getEval_url() {
		return eval_url;
	}
	public void setEval_url(String eval_url) {
		this.eval_url = eval_url;
	}
	public int getUnit_interval() {
		return unit_interval;
	}
	public void setUnit_interval(int unit_interval) {
		this.unit_interval = unit_interval;
	}
	public String getUncollect_reason() {
		return uncollect_reason;
	}
	public void setUncollect_reason(String uncollect_reason) {
		this.uncollect_reason = uncollect_reason;
	}
	public int getUnit_marking_type() {
		return unit_marking_type;
	}
	public void setUnit_marking_type(int unit_marking_type) {
		this.unit_marking_type = unit_marking_type;
	}
	public long getStation_id() {
		return station_id;
	}
	public void setStation_id(long station_id) {
		this.station_id = station_id;
	}
	public int getLanguage_id() {
		return language_id;
	}
	public void setLanguage_id(int language_id) {
		this.language_id = language_id;
	}
	public String getTransmitter_no() {
		return transmitter_no;
	}
	public void setTransmitter_no(String transmitter_no) {
		this.transmitter_no = transmitter_no;
	}
	public int getFreq() {
		return freq;
	}
	public void setFreq(int freq) {
		this.freq = freq;
	}
	public int getBand() {
		return band;
	}
	public void setBand(int band) {
		this.band = band;
	}
	public String getProgram_id() {
		return program_id;
	}
	public void setProgram_id(String program_id) {
		this.program_id = program_id;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public int getRadio_type_id() {
		return radio_type_id;
	}
	public void setRadio_type_id(int radio_type_id) {
		this.radio_type_id = radio_type_id;
	}
	public int getTime_type_id() {
		return time_type_id;
	}
	public void setTime_type_id(int time_type_id) {
		this.time_type_id = time_type_id;
	}
	public int getCenter_id() {
		return center_id;
	}
	public void setCenter_id(int center_id) {
		this.center_id = center_id;
	}
	public int getProgram_type_id() {
		return program_type_id;
	}
	public void setProgram_type_id(int program_type_id) {
		this.program_type_id = program_type_id;
	}
	public int getBroadcast_type_id() {
		return broadcast_type_id;
	}
	public void setBroadcast_type_id(int broadcast_type_id) {
		this.broadcast_type_id = broadcast_type_id;
	}
	public int getTask_type_id() {
		return task_type_id;
	}
	public void setTask_type_id(int task_type_id) {
		this.task_type_id = task_type_id;
	}
	public int getService_area_id() {
		return service_area_id;
	}
	public void setService_area_id(int service_area_id) {
		this.service_area_id = service_area_id;
	}
}
