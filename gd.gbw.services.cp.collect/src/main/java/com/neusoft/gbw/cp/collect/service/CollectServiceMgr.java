package com.neusoft.gbw.cp.collect.service;

import com.neusoft.gbw.cp.collect.model.CollectTaskModel;
import com.neusoft.gbw.cp.collect.service.transfer.TransferDownMgr;
import com.neusoft.gbw.cp.collect.service.transfer.TransferUpMgr;
import com.neusoft.gbw.cp.collect.service.transfer.servlet.HttpTranferServer;

public class CollectServiceMgr {
	
	private HttpTranferServer httpServer = null;
	private TransferDownMgr transferDownMgr = null;
	private TransferUpMgr transferUpMgr = null;
	
	public void setHttpServer(HttpTranferServer httpServer) {
		this.httpServer = httpServer;
	}

	public TransferDownMgr getTransferDownMgr() {
		return transferDownMgr;
	}

	public TransferUpMgr getTransferUpMgr() {
		return transferUpMgr;
	}

	/**
	 * 启动初始化配置文件
	 */
	public void init() {
		//启动加载配置文件
		new ConfigLoader().loadConfig();
	}

	/**
	 * 启动采集平台管理模块
	 */
	public void start() {
		init();
		//启动下行协议管理模块
		if (transferDownMgr == null) {
			transferDownMgr = new TransferDownMgr();
			transferDownMgr.start();
		}
		//启动上行协议管理模块
		if(transferUpMgr == null) {
			transferUpMgr = new TransferUpMgr();
			transferUpMgr.start();
		}

		//启动HTTP服务(将队列的管理对象传入对象)，启动站点往采集平台发送数据服务
		httpServer = new HttpTranferServer(transferUpMgr);
		httpServer.startServer();
	}
	
	public void stop() {
		clear();
	}
	
	private void clear() {
		httpServer.stopServer();
		transferDownMgr.stop();
		//清除超时管理对象
		CollectTaskModel.getModel().clear();
	}
}
