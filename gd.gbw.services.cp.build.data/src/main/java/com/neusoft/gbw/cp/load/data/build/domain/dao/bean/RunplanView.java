package com.neusoft.gbw.cp.load.data.build.domain.dao.bean;

public class RunplanView {

	private String runplan_id;
	private String station_id;
	private String language_id;
	private String transmitter_no;
	private int freq;
	private int band;
	private String program_id;
	private String direction;
	private String start_time;
	private String end_time;
	private String radio_type_id;
	private String time_type_id;
	private int program_type_id;
	private int broadcast_type_id;
	private int task_type_id;
	private String service_area;
	
	public String getService_area() {
		return service_area;
	}
	public void setService_area(String service_area) {
		this.service_area = service_area;
	}
	public String getRunplan_id() {
		return runplan_id;
	}
	public void setRunplan_id(String runplan_id) {
		this.runplan_id = runplan_id;
	}

	public String getStation_id() {
		return station_id;
	}
	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}
	public String getLanguage_id() {
		return language_id;
	}
	public void setLanguage_id(String language_id) {
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
	public String getRadio_type_id() {
		return radio_type_id;
	}
	public void setRadio_type_id(String radio_type_id) {
		this.radio_type_id = radio_type_id;
	}
	public String getTime_type_id() {
		return time_type_id;
	}
	public void setTime_type_id(String time_type_id) {
		this.time_type_id = time_type_id;
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
}
