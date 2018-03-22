package com.neusoft.gbw.cp.process.measure.vo.auto;

import com.neusoft.gbw.cp.core.collect.CollectData;

public class TemporaryCollectData {
	
	private String soundPath;
	private CollectData data;
	
	public String getSoundPath() {
		return soundPath;
	}
	public void setSoundPath(String soundPath) {
		this.soundPath = soundPath;
	}
	public CollectData getData() {
		return data;
	}
	public void setData(CollectData data) {
		this.data = data;
	}
}
