package com.neusoft.gbw.cp.build.application;

import com.neusoft.gbw.cp.build.domain.mode.BuildContext;
import com.neusoft.gbw.cp.build.domain.services.ConfigLoader;
import com.neusoft.gbw.cp.build.domain.services.SyntInitOtherService;
import com.neusoft.gbw.cp.build.domain.services.TaskBuildMgr;
import com.neusoft.gbw.cp.build.domain.services.TimeRemindMgr;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;

public class TaskBuilderCentre {
	
	private TaskBuildMgr manageService = null;
	private TimeRemindMgr remindService = null;
	private TaskReceiveMgr mgr = null;
	
	public TaskReceiveMgr getMgr() {
		return mgr;
	}

	private boolean init() {
		try {
			//加载配置
			new ConfigLoader().loadConfig();
			//从数据库加载数据
			DataMgrCentreModel.getInstance().synt();
			BuildContext.getInstance();
		} catch (Exception e) {
			Log.error("服务初始化失败", e);
			return false;
		}
		
		return true;
	}
	
	public void start() {
		if (!init())
			return;
		//初始化服务信息
		syntInitService();
		//消息提醒设置
		startRemindTime();
		//开始创建任务 把线程启动
		startTaskBuild();
		
		if(mgr == null) {
			mgr = new TaskReceiveMgr();
			mgr.init();
			mgr.start();
		}
	}
	
	private void syntInitService() {
		SyntInitOtherService othService = new SyntInitOtherService();
		othService.initMonitorMachine();
		othService.initCheckTransfer();    //站点巡检任务 定时任务   已改
		othService.initSyntDB();
		othService.initQualityType();
		othService.initAlarmType();
		othService.initFtpServer();
		othService.initRecordAddr();
		othService.initRealMeasureSite();
		
		othService.initRecoverTimeData();
		othService.initStationInfo();
		othService.initJMSReceiveConfig();
		//刷新相关配置信息
		othService.initTaskTimeInterval();
		
		//  lyc三满任务删除    定时任务
		othService.initDeleteTask();  
		//  lyc定时质量任务回收
		othService.initRecoverTaskData();
	}
	
	private void startTaskBuild() {
		manageService = new TaskBuildMgr();
		BuildContext.getInstance().setTaskBuildMgr(manageService);
		manageService.start();
	}
	
	private void startRemindTime() {
		remindService = new TimeRemindMgr();
		remindService.start();
	}
	
	public void stop() {
		ARSFToolkit.getServiceCentre().removeServiceByName(BuildConstants.REQUEST_THREAD_NAME);
		if(mgr != null) {
			mgr.stop();
		}
	}
}
