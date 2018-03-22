package com.neusoft.gbw.cp.station.service;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

import com.neusoft.gbw.cp.station.constants.ConfigVariable;
import com.neusoft.gbw.cp.station.service.build.ReportBuilder;
import com.neusoft.gbw.cp.station.service.transfer.servlet.ServletClientHandler;
import com.neusoft.np.arsf.base.bundle.Log;

public class AutoReportProcess {
	
	
	@SuppressWarnings("unused")
	private BlockingQueue<String> queue;
	private ReportBuilder builder = null;
	private ServletClientHandler client = null;
	
	
	public AutoReportProcess(BlockingQueue<String> queue,ServletClientHandler client,ReportBuilder build) {
		this.queue = queue;
		this.client = client;
		this.builder = build;
	}

	public void start() {
		if(ConfigVariable.IS_ACTIVE_REPORT) {
//			disposeQualityAlarm();
			disposeEquAlarm();
		}
		//临时主动上报
//		startQualityRrealQuery();
	}
	public void startQualityRrealQuery() {
		disposeQualityRrealQuery();
	}
	
	private void disposeQualityAlarm() {
		//创建周期timer
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Map<String, String> map = builder.QualityAlarmBuilder();
				for(String xml : map.values()) {
					Log.info("主动上报指标报警数据： " + xml);
					client.dispachDate(xml);
					sleep(sleepTime());
				}
			}
		}, new Date(),ConfigVariable.REPORT_TIME_INTERVAL*1000*60);
	}
	
	private void disposeEquAlarm() {
		//创建周期timer
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Map<String, String> map = builder.EquAlarmBuilder();
				for(String xml : map.values()) {
					Log.info("主动上报设备报警数据： " + xml);
					client.dispachDate(xml);
					sleep(sleepTime());
				}
			}
		}, new Date(),ConfigVariable.REPORT_TIME_INTERVAL*1000*60);
	}
	
	private void disposeQualityRrealQuery() {
		//创建周期timer
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				String report = builder.builderQualityRrealQuery();
				Log.info("主动上报实时指标数据： " + report);
				client.dispachDate(report);
			}
		},new Date(),1000);
	}
	
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private int sleepTime() {
		return ((int)(Math.random()*6)+1) * 1000;
	}
	
	public static void main(String[] args) {
		System.out.println(((int)(Math.random()*(ConfigVariable.REPORT_ALARM_NUM-1))));
		System.out.println();
	}
	
}
