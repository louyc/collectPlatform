package com.neusoft.gbw.cp.load.data.build.domain.dao.bean;

public class MonMachineTable {

	private long monitor_id;			//鐠佹儳顦琁D
	private int machine_id;			//閹恒儲鏁归張绡扗
	private String machine_code;	//閹恒儲鏁归張铏圭椽閻拷
	private String model_id;		//閹恒儲鏁归張鍝勭�閸欑īD
	private String model_name;		//閹恒儲鏁归張鍝勭�閸欙拷
	private String usage_mode;		//閹恒儲鏁归張铏规暏闁拷閿涳拷1閸忋劑鍎�1 閹稿洦鐖ｆ禒璇插,2瑜版洟鐓舵禒璇插,3妫版垼姘ㄦ禒璇插,4妫版垵浜告禒璇插,5鐎圭偞妞傞幐鍥ㄧ垼,6鐎圭偞妞傛０鎴ｆ皑,7鐎圭偞妞傝ぐ鏇㈢叾閿涳拷
	private int is_default;			//閺勵垰鎯佹稉娲帛鐠併倖甯撮弨鑸垫簚
	public long getMonitor_id() {
		return monitor_id;
	}
	public void setMonitor_id(long monitor_id) {
		this.monitor_id = monitor_id;
	}
	public int getMachine_id() {
		return machine_id;
	}
	public void setMachine_id(int machine_id) {
		this.machine_id = machine_id;
	}
	public String getMachine_code() {
		return machine_code;
	}
	public void setMachine_code(String machine_code) {
		this.machine_code = machine_code;
	}
	public String getModel_id() {
		return model_id;
	}
	public void setModel_id(String model_id) {
		this.model_id = model_id;
	}
	public String getModel_name() {
		return model_name;
	}
	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}
	public int getIs_default() {
		return is_default;
	}
	public void setIs_default(int is_default) {
		this.is_default = is_default;
	}
	public String getUsage_mode() {
		return usage_mode;
	}
	public void setUsage_mode(String usage_mode) {
		this.usage_mode = usage_mode;
	}
}
