package com.neusoft.gbw.cp.build.domain.conver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.app.evaluation.intf.dto.SpectrumRealtimeQueryDTO;
import com.neusoft.gbw.cp.build.domain.mode.BuildContext;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.constants.ConfigVariable;
import com.neusoft.gbw.cp.build.infrastructure.exception.ConverException;
import com.neusoft.gbw.cp.build.infrastructure.util.BusinessUtils;
import com.neusoft.gbw.cp.build.infrastructure.util.TimeUtils;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.TaskType;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.load.data.build.domain.model.LoadContext;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildInfo;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildPrepareInfo;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildType;
import com.neusoft.gbw.cp.load.data.build.domain.vo.LeakageCollect;
import com.neusoft.gbw.cp.load.data.build.domain.vo.MeasureUnitTime;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskMonitor;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskSchedule;
import com.neusoft.gbw.cp.measure.IMeasureSplit;
import com.neusoft.gbw.cp.measure.MeasureResult;
import com.neusoft.gbw.cp.measure.MeasureSplitFactory;
import com.neusoft.gbw.cp.measure.MeasureTask;
import com.neusoft.gbw.cp.measure.ScheduleAttr;
import com.neusoft.gbw.cp.schedule.vo.TimeRemindMsg;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.gbw.domain.monitor.intf.dto.MoniInspectResultDTO;
import com.neusoft.gbw.domain.task.effect.intf.dto.RealTimeStreamDTO;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;

public class ManualAndAutoTaskConver extends BasicTaskConver implements ITaskConver {

	@Override
	public List<BuildInfo> conver(Object obj) throws ConverException {
		if (!(obj instanceof BuildPrepareInfo)) {
			return null;
		}
		BuildPrepareInfo info = (BuildPrepareInfo) obj;
		List<BuildInfo> infoList = null;

		switch(info.getPlatBuildType()) {
		case first_start_build:
			infoList = converManualTask();  //改完
			break;
		case period_del_task_build:
			infoList = converAutoDelTask();  //改完
			break;
		case period_build:
			infoList = converAutoTask(info.getMsg()); //改完
			break;
		case burst_task_build:
			infoList = converBurstTask(info.getMsg()); //已改
			break;
		case leakage_task_build:
			infoList = converLeakageTask(info.getMsg());  //已改
			break;
		case auto_recover_data:    //自动回收质量任务    lyc  070406  203
			infoList = converRecoverTaskData(info.getMsg());
			break;
		case auto_monitor_inspect:
			infoList = converInspectMonitor(info.getExpandObj());
			break;
		default:
			break;
		}

		return infoList;
	}

	private boolean isCollect() {
		LeakageCollect leakageCollect = BuildContext.getInstance().getLeakageCollect();
		int leakCollectStatus = leakageCollect.getLeakCollectStatus();
		return  leakCollectStatus != 0;
	}

	//	@SuppressWarnings("unchecked")
	//	private List<BuildInfo> converStream(Object expandObj) throws ConverException {
	//		CollectTask task = (CollectTask)expandObj;
	//		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
	//		List<Map<String, Object>> dataList = (List<Map<String, Object>>)task.getExpandObject("RECORD_DATA");
	//		Map<String, Object> dataMap = null;
	//		for(Map<String, Object> map : dataList) {
	//			dataMap = new HashMap<String, Object>();
	//			dataMap.put("UNIT_STORE", map.get("UNIT_STORE"));
	//			dataMap.put("EQU_CODE", map.get("EQU_CODE"));
	//			dataMap.put("RECORD_ID", map.get("RECORD_ID"));
	//			dataMap.put("BAND", map.get("BAND"));
	//			dataMap.put("FREQ", map.get("FREQ"));
	//			dataMap.put("FILE_NAME", map.get("FILE_NAME"));
	//			dataMap.put("FILE_SIZE", map.get("FILE_SIZE"));
	//			dataMap.put("START_TIME", map.get("START_TIME"));
	//			dataMap.put("END_TIME", map.get("END_TIME"));
	//			dataMap.put("COLLECTTASK", task);
	//			//更新原有任务类型
	//			task.getBusTask().setType(BusinessTaskType.measure_manual_recover);
	//			BuildInfo info = buildInfo(ProtocolType.FileRetrieve, BusinessTaskType.measure_manual_recover, ProtocolType.FileRetrieve,task.getBusTask().getMonitor_id() + "", null, dataMap, TaskType.system);
	//			infoList.add(info);
	//		}
	//		return infoList;
	//	}

	private List<BuildInfo> converInspectMonitor(Object obj) throws ConverException {
		MoniInspectResultDTO statusDto = null;
		String monitorId = String.valueOf(obj);
		//构建dto对象
		MonitorDevice device = DataMgrCentreModel.getInstance().getMonitorDevice(Long.parseLong(monitorId));
		statusDto = getDto(device);
		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
		String firstCode = device.getFirstEnCode(); //add by jiahao
		//创建程序版本控制任务
		//根据不同的任务去提取相应的接收机 modify by jiahao
		String newFirstCode = getFirstEnCode(new BuildType(ProtocolType.ProgramCommand, BusinessTaskType.measure_inspect, ProtocolType.ProgramCommand), monitorId, firstCode, 0);
		BuildInfo info = buildInfo(ProtocolType.ProgramCommand, BusinessTaskType.measure_inspect, ProtocolType.ProgramCommand, statusDto.getMonitorId(), newFirstCode, statusDto, TaskType.system);
		infoList.add(info);
		return infoList;
	}

	private List<BuildInfo> converRecoverTaskData(TimeRemindMsg msg) throws ConverException {
		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
		List<Task> taskList = loadDBRecoverTask();
		Map<String,Integer> map = new HashMap<String,Integer>();
		Log.debug("[构建服务]本批次生成定时回收任务个 数：taskSize=" + taskList.size());
		for(Task task : taskList) {
			if(null!=map.get(task.getTaskMonitor().getMonitor_id()+"_"+task.getBuildType().getProType()) &&
					map.get(task.getTaskMonitor().getMonitor_id()+"_"+task.getBuildType().getProType())==1){
				continue;
			}
			//校验站点是否可以在本采集平台上下发任务
			String monitorID = String.valueOf(task.getTaskMonitor().getMonitor_id());
			if(!BusinessUtils.judePartformIsDone(monitorID)){
				continue;
			}
			try {
				String start_time ="";
				String end_time ="";
				String b_time = "";
				String e_time = "";
				String b_date = task.getMeasureTask().getTask_create_time().split(" ")[0];  //任务创建日期
				String now_date =TimeUtils.getCurrentTime().split(" ")[0];  //当前日期
				String nowTime = TimeUtils.getCurrentTime();  //当前时间
				//去掉任务中已经停用的
				int inUse = task.getMeasureTask().getIn_use();
				if(inUse != 1) {  
					continue;
				}
				//去掉不是下发成功的任务
				if(task.getMeasureTask().getTask_status_id()!=1){
					continue;
				}
				//去掉 当天 回收成功 的任务
				if(task.getMeasureTask().getRecycle_status_id()==1 && 
						task.getMeasureTask().getRecycle_time().split(" ")[0].compareTo(now_date)==0){
					continue;
				}

				int type = 0;  //时间周期类型  1：独立   2：周期
				int exporDate = 0;
				if(task.getScheduleList().size()>1){
					for(TaskSchedule ts:task.getScheduleList()){
						if(ts.getIs_overhaul() ==0){  //1 ：检修   0：有效
							type = ts.getSchedule_type();
							if(type==1){
								start_time =ts.getStarttime();
								end_time =ts.getEndtime();
							}else{
								start_time = TimeUtils.getYMdTime()+ " " + ts.getStarttime();
								end_time = TimeUtils.getYMdTime()+ " " + ts.getEndtime();
							}
							exporDate = ts.getExpiredays();
							break;
						}
					}
				}else{
					type = task.getScheduleList().get(0).getSchedule_type();
					if(type==1){
						start_time = task.getScheduleList().get(0).getStarttime();
						end_time = task.getScheduleList().get(0).getEndtime();
					}else{
						start_time = TimeUtils.getYMdTime()+ " " + task.getScheduleList().get(0).getStarttime();
						end_time = TimeUtils.getYMdTime()+ " " + task.getScheduleList().get(0).getEndtime();
					}
					exporDate = task.getScheduleList().get(0).getExpiredays();
				}

				if(type==1) {  //独立时间  直接回收对应时间段内数据  
					if(nowTime.compareTo(start_time)<=0){  //当前时间 <= 任务开始时间
						continue;
					}
					b_time = TimeUtils.getBeforeTime(start_time, 0);
					e_time = TimeUtils.getAfterTime(start_time, ConfigVariable.RECOVER_DATA_TIME_POINT);
				}else{ //周期
					b_date = TimeUtils.getAfterDayTime(b_date, exporDate);
					if(now_date.compareTo(b_date)>0){    //当前时间 > 周期任务结束时间
						continue;
					}else{
						if(nowTime.compareTo(start_time)>=0 && nowTime.compareTo(end_time)<=0   ||  
								nowTime.compareTo(start_time)>=0 && nowTime.compareTo(end_time)>=0){   //判断是否在运行图的播放时间段内
							b_time = TimeUtils.getBeforeTime(nowTime, ConfigVariable.RECOVER_DATA_TIME + ConfigVariable.RECOVER_DATA_TIME_POINT);
							e_time = TimeUtils.getAfterTime(nowTime, ConfigVariable.RECOVER_DATA_TIME_POINT);
						}else{
							continue;
						}
					}
				}


				task.getMeasureTask().setRecover_b_time(b_time);
				task.getMeasureTask().setRecover_e_time(e_time);
			} catch (Exception e) {
				Log.error("", e);
				continue;
			}
			if(!isStartSite(monitorID)) {
				Log.warn("[构建服务]该设备不存在或处于维护状态，monitorId=" + monitorID);
				continue;
			}
			map.put(monitorID+"_"+task.getBuildType().getProType(), 1);
			String firstCode = task.getTaskFreq().getReceiver_code(); //add by jiahao
			//根据不同的任务去提取相应的接收机 modify by jiahao
			String newFirstCode = getFirstEnCode(task.getBuildType(), monitorID, firstCode, task.getMeasureTask().getTask_build_mode());
			infoList.add(buildInfo(task.getBuildType(), monitorID, newFirstCode, task, TaskType.system));
		}
		Log.debug("[构建服务]本批次生成过滤后定时回收 任务个数：taskSize=" + infoList.size());
		map.clear();
		return infoList;
	}

	/*private List<BuildInfo> converAutoTask(TimeRemindMsg msg) throws ConverException {
		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
		List<Task> taskList = loadDBAutoTask();
		Log.debug("[构建服务]本批次生成收测任务初始化任务个数：taskSize=" + taskList.size());
		//判断当前的拆分的收测单元是否播放时间重合  如果重合则不生成收测任务
		taskList = filterTimeTask(taskList, msg);
		//		//判断当前是否是首次启用，首次启用则过滤掉系统上次启动已完成的收测单元
		//		//确认是否是补采，如果是补采，则删除掉收测单元中漏采的数据，重新补采
		//		LeakageCollect leakageCollect = BuildContext.getInstance().getLeakageCollect();
		//		if(isCollect(leakageCollect.getLeakCollectStatus())) {//删除未完成的收测单元
		//			Map<String, String> unUnitMap = getUnfinishedUnit(leakageCollect);
		//			delFinishedUnitProcess(unUnitMap);
		//			taskList = filterLeakCollectTask(taskList, unUnitMap);
		//		}

		for(Task task : taskList) {
			String monitorID = String.valueOf(task.getTaskMonitor().getMonitor_id());

			String firstCode = task.getTaskFreq().getReceiver_code(); //add by jiahao
			//根据不同的任务去提取相应的接收机 modify by jiahao
			String newFirstCode = getFirstEnCode(task.getBuildType(), monitorID, firstCode, task.getMeasureTask().getTask_build_mode());
			infoList.add(buildInfo(task.getBuildType(), monitorID, newFirstCode, task, TaskType.system));
		}

		return infoList;
	}*/
	public static void main(String[] args) {
		String s = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date()).toString().split(":")[2].split(" ")[1];

		System.out.println(s);
	}
	/**
	 * 周期 效果类任务 修改  lyc
	 * @param msg
	 * @return
	 * @throws ConverException
	 */

	private List<BuildInfo> converAutoTask(TimeRemindMsg msg) throws ConverException {
		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
		String s = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(new Date()).toString().split(":")[2].split(" ")[1];
		if(s.equals(ConfigVariable.INSPECT_MONITOR_TIME) && 
				msg.getTimeinterval()==Long.valueOf(ConfigVariable.INSPECT_MONITOR_UNIT)){
			List<RealTimeStreamDTO> realDtoList = loadDBAutoStream();
			Log.debug("[构建遥控站巡检服务]=" + realDtoList.size());
			for(RealTimeStreamDTO realDto: realDtoList){
				if(!BusinessUtils.judePartformIsDone(realDto.getMonitorId())){
					continue;
				}
				//根据不同的任务去提取相应的接收机 modify by jiahao
				String firstCode = getFirstEnCode(new BuildType(ProtocolType.StreamRealtimeQuery, BusinessTaskType.measure_auto, ProtocolType.StreamRealtimeQuery), realDto.getMonitorId(), null, 0);
				BuildInfo info = buildInfo(ProtocolType.StreamRealtimeQuery, BusinessTaskType.measure_auto, ProtocolType.StreamRealtimeQuery, realDto.getMonitorId(),firstCode, realDto, TaskType.temporary);
				info.setInspectSteam(true);
				infoList.add(info);
			}
		}
		List<Task> taskList = loadDBAutoTask();
		Log.debug("[构建服务]本批次生成收测任务初始化任务个数：taskSize=" + taskList.size());
		//判断当前的拆分的收测单元是否播放时间重合  如果重合则不生成收测任务
		taskList = filterTimeTaskMore(taskList, msg);
		for(Task task : taskList) {
			List<TaskMonitor> monitorIDList = task.getTaskMonitorList();
			String firstCode = task.getTaskFreq().getReceiver_code(); //add by jiahao
			Map<String,String> map = new HashMap<String,String>();
			//根据不同的任务去提取相应的接收机 modify by jiahao
			for(TaskMonitor taskMonitor: monitorIDList){
				String monitorID = String.valueOf(taskMonitor.getMonitor_id());
				String newFirstCode = getFirstEnCode(task.getBuildType(), monitorID, firstCode, task.getMeasureTask().getTask_build_mode());
				map.put(monitorID,newFirstCode);
			}
			infoList.add(buildInfo(task.getBuildType(), map, task, TaskType.system));
		}
		return infoList;
	}

	/**
	 * 补采 效果类任务
	 * @param msg
	 * @return
	 * @throws ConverException
	 */
	private List<BuildInfo> converLeakageTask(TimeRemindMsg msg) throws ConverException {
		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
		List<Task> taskList = loadDBAutoTask();
		//判断当前的拆分的收测单元是否播放时间重合
		Log.debug("[构建服务]本批次生成补采任务初始化任务个数：taskSize=" + taskList.size());
		//根据任务状态    站点分组    时间段 筛选任务    + 站点和采集关系过滤 20170119
		taskList = filterTimeTaskMore(taskList, msg);
		//判断当前是否是首次启用，首次启用则过滤掉系统上次启动已完成的收测单元
		//确认是否是补采，如果是补采，则删除掉收测单元中漏采的数据，重新补采
		if(isCollect()) {
			taskList = filterLeakCollectTask(taskList);
		}
		for(Task task : taskList) {
			//			String monitorID = String.valueOf(task.getTaskMonitor().getMonitor_id());
			List<TaskMonitor> listmontior = task.getTaskMonitorList();
			String firstCode = task.getTaskFreq().getReceiver_code(); //add by jiahao
			//根据不同的任务去提取相应的接收机 modify by jiahao
			//			String newFirstCode = getFirstEnCode(task.getBuildType(), monitorID, firstCode, task.getMeasureTask().getTask_build_mode());
			Map<String,String> map = new HashMap<String,String>();
			for(TaskMonitor taskMonitor: listmontior){
				String monitorID = String.valueOf(taskMonitor.getMonitor_id());
				String newFirstCode = getFirstEnCode(task.getBuildType(), monitorID, firstCode, task.getMeasureTask().getTask_build_mode());
				map.put(monitorID,newFirstCode);
			}
			infoList.add(buildInfo(task.getBuildType(), map, task, TaskType.leakage));
		}
		return infoList;
	}

	/**
	 * 突发 效果类任务
	 * @param msg
	 * @return
	 * @throws ConverException
	 */
	private List<BuildInfo> converBurstTask(TimeRemindMsg msg) throws ConverException {
		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
		List<Task> taskList = loadDBAutoTask();
		Log.debug("[构建服务]本批次生成突发任务初始化任务个数：taskSize=" + taskList.size());
		//判断当前的拆分的收测单元是否播放时间重合
		taskList = filterTimeTaskMore(taskList, msg);

		taskList = filterBrustTask(taskList);

		for(Task task : taskList) {
			//			String monitorID = String.valueOf(task.getTaskMonitor().getMonitor_id());
			//			String firstCode = task.getTaskFreq().getReceiver_code(); //add by jiahao
			//			String newFirstCode = getFirstEnCode(task.getBuildType(), monitorID, firstCode, task.getMeasureTask().getTask_build_mode());
			//			infoList.add(buildInfo(task.getBuildType(), monitorID, newFirstCode, task, TaskType.burst));
			List<TaskMonitor> listmontior = task.getTaskMonitorList();
			String firstCode = task.getTaskFreq().getReceiver_code(); //add by jiahao
			//根据不同的任务去提取相应的接收机 modify by jiahao
			Map<String,String> map = new HashMap<String,String>();
			for(TaskMonitor taskMonitor: listmontior){
				String monitorID = String.valueOf(taskMonitor.getMonitor_id());
				String newFirstCode = getFirstEnCode(task.getBuildType(), monitorID, firstCode, task.getMeasureTask().getTask_build_mode());
				map.put(monitorID,newFirstCode);
			}
			infoList.add(buildInfo(task.getBuildType(), map, task, TaskType.burst));
		}

		return infoList;
	}

	private List<BuildInfo> converManualTask() throws ConverException {
		String newFirstCode = null;
		List<Task> taskList = loadDBSetTask();
		taskList = filterTask(taskList);
		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
		for(Task task : taskList) {
			String monitorID = String.valueOf(task.getTaskMonitor().getMonitor_id());
			//校验站点是否可以在本采集平台上下发任务
			if(!BusinessUtils.judePartformIsDone(String.valueOf(monitorID))){
				continue;
			}
			if(!isStartSite(monitorID)) {
				Log.warn("[构建服务]该设备不存在或处于维护状态，monitorId=" + monitorID);
				continue;
			}
			String firstCode = task.getTaskFreq().getReceiver_code(); //add by jiahao
			//根据不同的任务去提取相应的接收机 modify by jiahao
			BuildType buildType = new BuildType(task.getBuildType().getProType(), BusinessTaskType.measure_manual_set, task.getBuildType().getProType());
			//根据不同的任务去提取相应的接收机 modify by jiahao
			newFirstCode = getFirstEnCode(buildType, monitorID, firstCode, task.getMeasureTask().getTask_build_mode());

			infoList.add(buildInfo(buildType, monitorID, newFirstCode, task, TaskType.system));
		}
		return infoList;
	}

	private List<BuildInfo> converAutoDelTask() throws ConverException {
		String newFirstCode = null;
		List<Task> taskList = loadDBDelTask();
		taskList = filterDelTask(taskList);
		List<BuildInfo> infoList = new ArrayList<BuildInfo>();
		for(Task task : taskList) {
			String monitorID = String.valueOf(task.getTaskMonitor().getMonitor_id());
			//校验任务是否下发  除去已下发的全部直接删除
			if(task.getMeasureTask().getTask_status_id()!=1){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("task_id", task.getMeasureTask().getTask_id());
				sendDelTask(map);
				continue;
			}
			//校验任务是否已删除    0 当前有效、1 预设、2 过期、3 删除
			if(task.getMeasureTask().getData_status()==3){
				continue;
			}
			//校验站点是否可以在本采集平台上下发任务
//			if(!BusinessUtils.judePartformIsDone(String.valueOf(monitorID))){
//				continue;
//			}
			if(!isStartSite(monitorID)) {
				Log.warn("[构建服务]该设备不存在或处于维护状态，monitorId=" + monitorID);
				continue;
			}
			String firstCode = task.getTaskFreq().getReceiver_code(); //add by jiahao
			//根据不同的任务去提取相应的接收机 modify by jiahao
			BuildType buildType = new BuildType(ProtocolType.TaskDelete, BusinessTaskType.measure_manual_del, task.getBuildType().getProType());
			newFirstCode = getFirstEnCode(buildType, monitorID, firstCode, task.getMeasureTask().getTask_build_mode());
			infoList.add(buildInfo(buildType, monitorID, newFirstCode, task, TaskType.qualityDel));
		}
		return infoList;
	}

	public void sendDelTask(Map<String,Object> map) {
		List<StoreInfo> infoList = new ArrayList<StoreInfo>();
		infoList.add(buildStoreInfo("deleteSetTask", map));
		infoList.add(buildStoreInfo("deleteSetTaskFreq", map));
		infoList.add(buildStoreInfo("deleteSetTaskMonitor", map));
		infoList.add(buildStoreInfo("deleteSetTaskConf", map));
		infoList.add(buildStoreInfo("deleteSetTaskFreqSchedule", map));
		infoList.add(buildStoreInfo("deleteSetTaskRecover", map));
		if(infoList != null && !infoList.isEmpty()) {
			for(StoreInfo info : infoList) {
				ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, info);
			}
		}
	}

	private StoreInfo buildStoreInfo(String label, Map<String,Object> qData) {
		StoreInfo info = new StoreInfo();
		try {
			info.setDataMap(qData);
			info.setLabel(label);
		} catch (Exception e) {
			Log.error("", e);
		}
		return info;
	}

	/*private List<Task> filterTimeTask(List<Task> taskList, TimeRemindMsg msg) throws ConverException {
		int interval = msg.getTimeinterval();
		//提取站点（广播和实验）
		Map<Long, MeasureUnitTime> broadMap = getMeaUnitTimeMap(interval, 3);
		Map<Long, MeasureUnitTime> experMap = getMeaUnitTimeMap(interval, 4);
		//站点分组对应运行图类型
		//Map<Integer, String> monitorMap = getRealMeaMoniMap();
		Map<Long, String> monitorMap = getRealMeaMoniMap();

		MeasureTask mtask = null;
		List<MeasureTask> tempList = new ArrayList<MeasureTask>();
		Iterator<Task> iter = taskList.iterator();
		while(iter.hasNext()) {
			Task task = iter.next();
			//(1)去掉任务中已经停用的
			int inUse = task.getMeasureTask().getIn_use();
			if(inUse != 1) {
				iter.remove();
				continue;
			}

			task.setExpandObj(msg);

			long monitorID = task.getTaskMonitor().getMonitor_id();
			//(2)确认站点是否是进行采集
			if(!monitorMap.containsKey(monitorID)) {
				iter.remove();
				continue;
			}
			int task_type_id = task.getMeasureTask().getTask_type_id();
			//该站点配置了相对应的广播和实验分组
			if(monitorMap.get(monitorID).indexOf(task_type_id + "") == -1){
				iter.remove();
				continue;
			}
			//(3)确认是否是广播和实验任务，根据不同的任务确定该时间间隔是否包含此站点
			//确定该任务是广播或者是实验任务，跟据不同的任务类型获取站点是否在这个时间间隔之中
			if (task_type_id == 3 && !broadMap.containsKey(monitorID)) {
				iter.remove();
				continue;
			}
			if (task_type_id == 4 && !experMap.containsKey(monitorID)) {
				iter.remove();
				continue;
			}


			mtask = buildMeasureTask(task, msg);
			tempList.add(mtask);
		}

		IMeasureSplit split = MeasureSplitFactory.newMeasureSplit();
		split.setMeasureTask(tempList);
		List<Task> list = new ArrayList<Task>();
		try {
			List<MeasureResult> resultList = split.getSplitResult(msg.getRemindtime(), msg.getEndTime());
			for(MeasureResult result : resultList) {
				list.add((Task)result.getTask().getBusObj());
			}
		} catch (Exception e) {
			throw new ConverException(e.getMessage());
		}
		return list;
	}*/
	/**
	 * 效果类 任务 修改  lyc   过滤站点
	 * @param taskList
	 * @param msg
	 * @return
	 * @throws ConverException
	 */

	private List<Task> filterTimeTaskMore(List<Task> taskList, TimeRemindMsg msg) throws ConverException {
		int interval = msg.getTimeinterval();
		//提取站点（广播和实验）
		Map<Long, MeasureUnitTime> broadMap = getMeaUnitTimeMap(interval, 3);
		Map<Long, MeasureUnitTime> experMap = getMeaUnitTimeMap(interval, 4);
		//站点分组对应运行图类型
		//Map<Integer, String> monitorMap = getRealMeaMoniMap();
		Map<Long, String> monitorMap = getRealMeaMoniMap();

		MeasureTask mtask = null;
		List<MeasureTask> tempList = new ArrayList<MeasureTask>();
		Iterator<Task> iter = taskList.iterator();
		while(iter.hasNext()) {
			Task task = iter.next();
			//(1)去掉任务中已经停用的
			int inUse = task.getMeasureTask().getIn_use();
			if(inUse != 1) {
				iter.remove();
				continue;
			}
			task.setExpandObj(msg);
			List<TaskMonitor> listMonitor = task.getTaskMonitorList();
			if(null ==listMonitor){
				continue;
			}
			List<TaskMonitor> listMonitorNew = new ArrayList<>();
			for(int i=0;i<listMonitor.size();i++){
				long monitorID = listMonitor.get(i).getMonitor_id();
				//(2)确认站点是否是进行采集
				if(!monitorMap.containsKey(monitorID)) {
					continue;
				}
				int task_type_id = task.getMeasureTask().getTask_type_id();
				//该站点配置了相对应的广播和实验分组
				if(monitorMap.get(monitorID).indexOf(task_type_id + "") == -1){
					continue;
				}
				//(3)确认是否是广播和实验任务，根据不同的任务确定该时间间隔是否包含此站点
				if (task_type_id == 3 && !broadMap.containsKey(monitorID)) {
					continue;
				}
				if (task_type_id == 4 && !experMap.containsKey(monitorID)) {
					continue;
				}
				//校验站点是否可以在本采集平台上下发任务
				if(!BusinessUtils.judePartformIsDone(String.valueOf(monitorID))){
					continue;
				}
				listMonitorNew.add(listMonitor.get(i));
			}
			if(null!=listMonitorNew && listMonitorNew.size()==0){
				iter.remove();
				continue;
			}
			task.setTaskMonitorList(listMonitorNew);
			mtask = buildMeasureTask(task, msg);
			tempList.add(mtask);
		}

		IMeasureSplit split = MeasureSplitFactory.newMeasureSplit();
		split.setMeasureTask(tempList);
		List<Task> list = new ArrayList<Task>();
		try {
			List<MeasureResult> resultList = split.getSplitResult(msg.getRemindtime(), msg.getEndTime());
			for(MeasureResult result : resultList) {
				list.add((Task)result.getTask().getBusObj());
			}
		} catch (Exception e) {
			throw new ConverException(e.getMessage());
		}
		return list;
	}

	private List<Task> filterLeakCollectTask(List<Task> taskList) throws ConverException {
		List<Task> newTask = new ArrayList<Task>();
		for(Task task: taskList) {
			if(checkMeasureUnit(task)) 
				continue;
			newTask.add(task);
		}
		return newTask;
	}

	private boolean checkMeasureUnit(Task task) {
		boolean flag = false;
		Map<String, String> unFinishUnitMap = BusinessUtils.getUnfinishedUnit();
		Map<String, String> finishUnitMap = BusinessUtils.getFinishedUnit();
		//如果收测单元状态为完成，则进行过滤
		Map<Integer, Boolean> unitStatus = DataMgrCentreModel.getInstance().getUnitStatus();
		int mode_type = task.getMeasureTask().getTask_build_mode();
		//如果本次单元没有生成，则不需要过滤
		if(unitStatus.get(mode_type) != null && !unitStatus.get(mode_type)) {
			return flag;
		}
		//如果生成了本次收测单元，在对未完成的收测单元进行校验
		String key = getUnitKey(task);
		//如果未完成，则过滤掉不在未完成数据中的任务
		if(unFinishUnitMap.size() >= 0 && !unFinishUnitMap.containsKey(key)
				&& finishUnitMap.containsKey(key)) 
			return true;
		return flag;
	}

	/**
	 * 过滤突发频率任务，
	 * 过滤规则：（1）获取所有任务
	 * （2）获取上次收测单元的所有收测数据
	 * （3）比对收测数据和任务，如果是新增的突发频率，将任务保留
	 * @param taskList
	 * @return
	 * @throws ConverException
	 */
	private List<Task> filterBrustTask(List<Task> taskList) throws ConverException {
		List<Task> newTask = new ArrayList<Task>();
		for(Task task: taskList) {
			//如果已经有该频率的收测任务，则过滤掉
			if(checkColTask(task)) {
				Log.debug("已有该频率的收测任务  过滤掉"+task.getMeasureTask().getTask_build_mode()
						+"站点id"+task.getTaskMonitorList().get(0).getMonitor_id()
						+"任务id "+task.getMeasureTask().getTask_id()
						+"频率"+task.getTaskFreq().getFreq()
						+"运行图id"+task.getScheduleList().get(0).getRunplan_id());
				continue;
			}
			newTask.add(task);
		}
		return newTask;
	}

	private boolean checkColTask(Task task) {
		boolean flag = false;
		Map<String, String> finishedUnit = DataMgrCentreModel.getInstance().getTaskUnit();
		String key = getUnitKey(task);
		//如果当前收测单元，是已经生成的收测单元，进行过滤
		if(finishedUnit.containsKey(key)) 
			return true;
		return flag;
	}

	private String getUnitKey(Task task) {
		StringBuffer str = new StringBuffer();
		str.append(task.getMeasureTask().getTask_build_mode());
		str.append("_");
		//		str.append(task.getTaskMonitor().getMonitor_id());  效果类 任务 修改 lyc
		str.append(task.getTaskMonitorList().get(0).getMonitor_id());
		str.append("_");
		str.append(task.getMeasureTask().getTask_id());
		str.append("_");
		str.append(task.getScheduleList().get(0).getRunplan_id());
		return str.toString();
	}

	private MeasureTask buildMeasureTask(Task task, TimeRemindMsg msg) {
		MeasureTask mtask = new MeasureTask();
		ScheduleAttr attr = new ScheduleAttr();
		mtask.setGroupKey(task.getTaskFreq().getFreq());
		mtask.setBusObj(task);
		mtask.setMeasureUnitTime(msg.getTimeinterval());
		mtask.setAttr(attr);

		TaskSchedule schedule = task.getScheduleList().get(0);
		attr.setDayOfWeek(schedule.getDayofweek()==null?-1:Integer.parseInt(schedule.getDayofweek()));
		attr.setScheduleType(schedule.getSchedule_type());
		attr.setStartTime(schedule.getStarttime());
		attr.setEndTime(schedule.getEndtime());
		attr.setOverhaul(schedule.getIs_overhaul());

		return mtask;
	}

	/**
	 * @param interval
	 * @param type 3:广播，4：实验
	 * @return
	 */
	private Map<Long, MeasureUnitTime> getMeaUnitTimeMap(int interval, int type) {
		List<MeasureUnitTime> list = null;
		if(type == 3)
			list = LoadContext.getInstance().getBroadIntervalMap().get(interval);
		else
			list = LoadContext.getInstance().getExporIntervalMap().get(interval);

		Map<Long, MeasureUnitTime> map = new HashMap<Long, MeasureUnitTime>();
		if(list == null)
			return map;

		for(MeasureUnitTime unit : list)
			map.put(unit.getMonitorID(), unit);
		return map;
	}

	private Map<Long, String> getRealMeaMoniMap() {
		Map<String, String> map = DataMgrCentreModel.getInstance().getRealMeasureSite();
		//Map<Integer, String> siteMap = new HashMap<Integer, String>();
		Map<Long, String> siteMap = new HashMap<Long, String>();
		//int monitorId = 0;
		long monitorId = 0;
		int runplan_type = 0;
		for(String key : map.keySet()) {
			//monitorId = Integer.parseInt(key.split("_")[0]);
			monitorId = Long.parseLong(key.split("_")[0]);
			runplan_type = Integer.parseInt(key.split("_")[1]);
			if(!siteMap.containsKey(monitorId))
				siteMap.put(monitorId, runplan_type + "");
			else
				siteMap.put(monitorId, siteMap.get(monitorId) + "_" + runplan_type);
		}
		return siteMap;
	}

	private List<Task> filterTask(List<Task> taskList) {
		Iterator<Task> iter = taskList.iterator();
		while(iter.hasNext()) {
			Task task = iter.next();

			//去掉任务中已经停用的
			int inUse = task.getMeasureTask().getIn_use();
			if(inUse != 1) {
				iter.remove();
				continue;
			}
			//过滤已经下发的任务
			if (task.getTaskFreq().getSync_status() == BuildConstants.SYNT_SET_OVER_STATUS){
				iter.remove();
				continue;
			}
		}
		return taskList;
	}

	private List<Task> filterDelTask(List<Task> taskList) {
		Iterator<Task> iter = taskList.iterator();
		while(iter.hasNext()) {
			Task task = iter.next();
			//去掉任务中已经停用的
			int inUse = task.getMeasureTask().getIn_use();
			if(inUse != 0) {
				iter.remove();
			}
			if(null!=task.getTaskMonitorList()&& task.getTaskMonitorList().size()>0 && null!=task.getTaskMonitorList().get(0)){
				boolean b = BusinessUtils.judePartformIsDone(String.valueOf(task.getTaskMonitorList().get(0).getMonitor_id()));
				if(!b){
					iter.remove();
				}
			}
		}
		return taskList;
	}

	private MoniInspectResultDTO getDto(MonitorDevice device) {
		MoniInspectResultDTO statusDto = new MoniInspectResultDTO();
		statusDto.setMonitorId(device.getMonitor_id() + "");
		statusDto.setTimeStamp(TimeUtils.getCurrentTime());//创建巡检时间
		return statusDto;
	}

	private boolean isStartSite(String monitorID) throws ConverException {
		boolean startFlag = false;
		MonitorDevice device = DataMgrCentreModel.getInstance().getMonitorDevice(Long.parseLong(monitorID));
		if (device != null)
			startFlag = true;
		return startFlag;
	}

	private List<Task> loadDBSetTask() {
		DataMgrCentreModel model = DataMgrCentreModel.getInstance();
		return model.getDeviceTaskSet();
	}
	private List<Task> loadDBDelTask() {
		DataMgrCentreModel model = DataMgrCentreModel.getInstance();
		return model.getDelTaskSet();
	}

	private List<Task> loadDBAutoTask() {
		DataMgrCentreModel model = DataMgrCentreModel.getInstance();
		return model.getPlatformStream();
	}

	private List<RealTimeStreamDTO> loadDBAutoStream() {
		DataMgrCentreModel model = DataMgrCentreModel.getInstance();
		return model.getInspectStream();
	}

	private List<Task> loadDBRecoverTask() {
		DataMgrCentreModel model = DataMgrCentreModel.getInstance();
		return model.getDeviceTaskRecover();
	}
}
