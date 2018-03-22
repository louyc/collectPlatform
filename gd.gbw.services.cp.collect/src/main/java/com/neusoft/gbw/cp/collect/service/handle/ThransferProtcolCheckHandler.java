package com.neusoft.gbw.cp.collect.service.handle;

import com.neusoft.gbw.cp.conver.vo.Report;

public class ThransferProtcolCheckHandler {
	
	/**
	 * 协议校验
	 * （1）23所V6版本采集点返回的任务协议头中的是Version
	 * @param report
	 * @return
	 */
	protected static Report chectProtcol(Report report) {
		if(report.getVersion().equals("Version"))
			report.setVersion("6");
		return report;
	}
}
