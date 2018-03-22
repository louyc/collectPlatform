package com.neusoft.gbw.cp.collect.service.handle;

import com.neusoft.gbw.cp.collect.model.CollectTaskContext;
import com.neusoft.gbw.cp.collect.service.control.StationControlMgr;
import com.neusoft.gbw.cp.collect.service.transfer.TransferDownMgr;
import com.neusoft.gbw.cp.collect.vo.SiteConfig;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class CollectTaskDispatchHandler extends NMService {

	private TransferDownMgr mgr = null;
	
	public CollectTaskDispatchHandler(TransferDownMgr mgr) {
		this.mgr = mgr;
	}

	/* 
	 * 
	 * 站点控制分发功能，判断是否含有处理模块，如果没有实例化
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {  //站点 任务分发 功能 lyc
		CollectTask task = null;
		StationControlMgr stationCtrlMgr = null;
		SiteConfig conf = null;
		while(isThreadRunning()) {
			try {
				task = mgr.take();
			} catch (InterruptedException e) {
				Log.error("", e);
				break;
			}
			try {
				String monitorID = task.getBusTask().getMonitor_id()+"";
				//获取站点控制模块
				stationCtrlMgr = getStationControlMgr(monitorID);
				if (stationCtrlMgr == null) {
					//获取站点配置
					conf = getSiteConfig(monitorID);
					if (conf == null) {
						Log.warn("不存在指定的指定站点的相关的信息，请查看此MonitorID=" + monitorID);
						continue;
					}
					stationCtrlMgr = new StationControlMgr(conf);
					stationCtrlMgr.init();
					stationCtrlMgr.start();
					//将接收机放入控制模块对象
					CollectTaskContext.getModel().add(stationCtrlMgr);
				}
				
				stationCtrlMgr.put(task);
			}catch(Exception e) {
				Log.error("站点分发总控制线程运行中出现异常      CollectTaskDispatchHandler", e);
			}
		}
	}
	
	/**
	 * 获取站点控制模块，这里是站点数据处理的业务
	 * @param key
	 * @return
	 */
	private StationControlMgr getStationControlMgr(String key) {
		return CollectTaskContext.getModel().getStationControlMgr(key);
	}
	
	private SiteConfig getSiteConfig(String key) {
		return CollectTaskContext.getModel().getSiteConfig(key);
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String arg0) {
		this.serviceName = arg0;
	}
}
