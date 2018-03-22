package com.neusoft.gbw.cp.process.inspect.vo;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import com.neusoft.gbw.cp.process.inspect.constants.InspectConstants;

public class InspectTaskTimeOut {
	
	private long monitor_id;
	private int version_id;
	private String times_tamp;
	private Map<String, Integer> status; //key:inspect_code ,value 0:为未完成， 1：完成 
	private Timer timer; //计时器

	public InspectTaskTimeOut() {
		status = new HashMap<String, Integer>();
		status.put(InspectConstants.inspectProject.EQU_UPS_CODE, 0);
		status.put(InspectConstants.inspectProject.EQU_RECEIVE_CODE, 0);
		status.put(InspectConstants.inspectProject.EQU_AM_CARD_CODE, 0);
		status.put(InspectConstants.inspectProject.EQU_FM_CARD_CODE, 0);
		status.put(InspectConstants.inspectProject.EQU_VOICE_CARD_CODE, 0);
		status.put(InspectConstants.inspectProject.EQU_OFFSET_CARD_CODE, 0);
		status.put(InspectConstants.inspectProject.PROGRAM_VERSION_CODE, 0);
		status.put(InspectConstants.inspectProject.PROGRAM_RUNNING_CODE, 0);
		status.put(InspectConstants.inspectProject.TOTAL_TASK_RUNNING_STATE_CODE, 0);
		status.put(InspectConstants.inspectProject.QUALITY_TASK_RUNNING_STATE_CODE, 0);
		status.put(InspectConstants.inspectProject.STREAM_TASK_RUNNING_STATE_CODE, 0);
		status.put(InspectConstants.inspectProject.STREAM_VADIO_PLAY, 0);
		status.put(InspectConstants.inspectProject.DEVICE_LOG, 0);
		status.put(InspectConstants.inspectProject.TASK_EXSIT_STATE_CODE, 0);
		status.put(InspectConstants.inspectProject.TASK_RUNNING_STATE_CODE, 0);
	}

	public int getVersion_id() {
		return version_id;
	}

	public void setVersion_id(int version_id) {
		this.version_id = version_id;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public long getMonitor_id() {
		return monitor_id;
	}

	public void setMonitor_id(long monitor_id) {
		this.monitor_id = monitor_id;
	}

	public Map<String, Integer> getStatus() {
		return status;
	}

	public void setStatus(String inspectCode) {
		status.put(inspectCode, 1);
	}

	public String getTimes_tamp() {
		return times_tamp;
	}

	public void setTimes_tamp(String times_tamp) {
		this.times_tamp = times_tamp;
	}

	public void setStatus(Map<String, Integer> status) {
		this.status = status;
	}
	
}
