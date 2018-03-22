package com.neusoft.gbw.cp.build.domain.services;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.constants.ConfigVariable;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildPrepareInfo;
import com.neusoft.gbw.cp.load.data.build.domain.vo.PlatformBuildType;
import com.neusoft.gbw.cp.schedule.vo.TimeRemindMsg;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class TaskBuildHandler extends NMService {
	
	private TimeRemindMsg msg = null;
	private int buildType = 0; //构建类型，如果是 1：首次启动任务，2：定时任务，3：突发任务,4：补采任务
	
	public TaskBuildHandler(TimeRemindMsg msg, int buildType) {
		this.msg = msg;
		this.buildType = buildType;
	}

	@Override
	public void run() {
		if (buildType == BuildConstants.TaskBuild.START_PLATFORM_BUILD) {
			Log.debug("平台启动首次加载构建任务（采集点手动任务中的设置任务）");
//			startPlatformBuild();   
		} else if(buildType == BuildConstants.TaskBuild.START_PERIOD_BUILD){
			Log.info("启动定时任务构建，Time=" + msg.getRemindtime() + ",Interval=" + msg.getTimeinterval() + "分钟");
//			if(ConfigVariable.COLLECT_ZK_USE.equals("0"))
			startPeriodBuild();
		}else if(buildType == BuildConstants.TaskBuild.START_BURST_BUILD){
			Log.info("启动突发任务构建，Time=" + msg.getRemindtime() + ",Interval=" + msg.getTimeinterval() + "分钟");
//			if(ConfigVariable.COLLECT_ZK_USE.equals("0"))
			startBurstBuild();
		}else if(buildType == BuildConstants.TaskBuild.START_LEAKAGE_BUILD){
			Log.info("启动补采任务构建，Time=" + msg.getRemindtime() + ",Interval=" + msg.getTimeinterval() + "分钟");
//			if(ConfigVariable.COLLECT_ZK_USE.equals("0"))
			startLeakageBuild();
		}else if(buildType == BuildConstants.TaskBuild.START_PERIOD_DEL_TASK_BUILD){
			Log.info("启动定时删除任务构建，Time=" + msg.getRemindtime() + ",Interval=" + msg.getTimeinterval() + "分钟");
			startPeriodDelTaskBuild();
		}
	}
	
	private void startPlatformBuild() {
		BuildPrepareInfo info = new BuildPrepareInfo();
		info.setPlatBuildType(PlatformBuildType.first_start_build);
		ITaskProcess process = TaskProcessCentre.getInstance().newManualAndAutoTskProcess();
		process.taskProcess(info);
	}
	
	private void startPeriodDelTaskBuild() {
		BuildPrepareInfo info = new BuildPrepareInfo();
		info.setPlatBuildType(PlatformBuildType.period_del_task_build);
		ITaskProcess process = TaskProcessCentre.getInstance().newManualAndAutoTskProcess();
		process.taskProcess(info);
	}
	
	private void startPeriodBuild() {
		BuildPrepareInfo info = new BuildPrepareInfo();
		info.setMsg(msg);
		info.setPlatBuildType(PlatformBuildType.period_build);
		ITaskProcess process = TaskProcessCentre.getInstance().newManualAndAutoTskProcess();
		process.taskProcess(info);
	}
	
	private void startLeakageBuild() {
		BuildPrepareInfo info = new BuildPrepareInfo();
		info.setMsg(msg);
		info.setPlatBuildType(PlatformBuildType.leakage_task_build);
		ITaskProcess process = TaskProcessCentre.getInstance().newManualAndAutoTskProcess();
		process.taskProcess(info);
	}
	
	private void startBurstBuild() {
		BuildPrepareInfo info = new BuildPrepareInfo();
		info.setMsg(msg);
		info.setPlatBuildType(PlatformBuildType.burst_task_build);
		ITaskProcess process = TaskProcessCentre.getInstance().newManualAndAutoTskProcess();
		process.taskProcess(info);
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
