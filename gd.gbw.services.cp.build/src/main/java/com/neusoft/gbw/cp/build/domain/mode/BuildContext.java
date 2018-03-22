package com.neusoft.gbw.cp.build.domain.mode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.build.domain.build.IQueryBuild;
import com.neusoft.gbw.cp.build.domain.build.collect.AbstractTaskBuilder;
import com.neusoft.gbw.cp.build.domain.services.BuildPrepare;
import com.neusoft.gbw.cp.build.domain.services.TaskBuildMgr;
import com.neusoft.gbw.cp.load.data.build.domain.vo.LeakageCollect;
import com.neusoft.gbw.cp.schedule.vo.TimeSetMsg;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;

public class BuildContext {

	private static class BuildContextHolder {
		private static final BuildContext INSTANCE = new BuildContext();
	}

	private BuildContext() {
		init();
	}

	private void init() {
		BuildPrepare build = new BuildPrepare();
		//构建所有任务类型
		this.taskBuildMap = build.prepareTaskBuider();
		//构建协议对象
		this.queryBuildMap = build.prepareQueryBuilder();
		//TODO 通信协议校验 （待移除）
		this.queryVerifierMap = build.prepareQueryVerifier();
		//装载处理事件对象
		eventList = new ArrayList<BaseEventHandler>();
		//加载重启补采全部效果任务
		leakageCollect = new LeakageCollect(1, 0);
	}

	public static BuildContext getInstance() {
		return BuildContextHolder.INSTANCE;
	}

	private Map<Integer, TimeSetMsg> remindMap = null;
	private TaskBuildMgr taskBuildMgr = null;
	private List<BaseEventHandler> eventList = null;
	private Map<Integer, IQueryBuild> queryBuildMap = null;
	private Map<Integer, Object> queryVerifierMap = null;
	private Map<String, AbstractTaskBuilder> taskBuildMap = null;
	private LeakageCollect leakageCollect = null;
	

	public Map<Integer, TimeSetMsg> getRemindMap() {
		return remindMap;
	}

	public void setRemindMap(Map<Integer, TimeSetMsg> remindMap) {
		this.remindMap = remindMap;
	}

	public TaskBuildMgr getTaskBuildMgr() {
		return taskBuildMgr;
	}

	public void setTaskBuildMgr(TaskBuildMgr taskBuildMgr) {
		this.taskBuildMgr = taskBuildMgr;
	}

	public List<BaseEventHandler> getEventList() {
		return eventList;
	}

	public void addEventList(BaseEventHandler handler) {
		this.eventList.add(handler);
	}

	public Map<Integer, IQueryBuild> getQueryBuildMap() {
		return queryBuildMap;
	}

	public Map<String, AbstractTaskBuilder> getTaskBuildMap() {
		return taskBuildMap;
	}

	public Map<Integer, Object> getQueryVerifierMap() {
		return queryVerifierMap;
	}

	public LeakageCollect getLeakageCollect() {
		return leakageCollect;
	}

	public void setLeakageCollect(LeakageCollect leakageCollect) {
		this.leakageCollect = leakageCollect;
	}
}
