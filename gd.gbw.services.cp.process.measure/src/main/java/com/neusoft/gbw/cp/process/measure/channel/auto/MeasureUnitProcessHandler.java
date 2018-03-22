package com.neusoft.gbw.cp.process.measure.channel.auto;

import java.util.List;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class MeasureUnitProcessHandler extends NMService{
	
	private List<CollectTask> taskList = null;
	private MesureUnitProcessor process = null;
//	private MeaUnitChannel channel = null;
	
	public MeasureUnitProcessHandler(List<CollectTask> taskList) {
		this.taskList = taskList;
//		this.channel = channel;
		process = new MesureUnitProcessor();
	}

	@Override
	public void run() {
		int mode = taskList.get(0).getBusTask().getTask_build_mode();
		int unitTime = taskList.get(0).getBusTask().getMeasure_unit_time();
		String begin_time = taskList.get(0).getBusTask().getUnit_begin();
		switch(mode) {
		case 0:
			if (!process.updateAndStoreMesureAutoUnit(taskList)){
				Log.warn("自动生成收测单元失败， BeginTime=" + begin_time + ", UnitTime=" + unitTime);
			}else{
				Log.debug("measure下发给collect的效果任务自动 list"+taskList.size());
				ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.LIST_COLLECT_TASK_TOPIC, taskList);
			}
//			updateMeasureUnitCount(mode,unitTime,begin_time);
			break;
		case 1:
			if (!process.updateAndStoreMesureManualUnit(taskList)){
				Log.warn("手动生成收测单元失败， BeginTime=" + begin_time + ", UnitTime=" + unitTime);
//				updateMeasureUnitCount(mode,unitTime,begin_time);
			}else{
				Log.debug("measure下发给collect的效果任务手动 list"+taskList.size());
				ARSFToolkit.sendEvent(EventServiceTopic.TaskControl.LIST_COLLECT_TASK_TOPIC, taskList);
			}
			break;
		}
//		taskList.clear();
	}
	
//	private void updateMeasureUnitCount(int mode, int unitTime, String begin_time) {
//		int outTime = 60; //初始化录音超时时间60秒
//		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
//		Map<Integer, Integer> recordMap = new HashMap<Integer, Integer>();
//		for(CollectTask task : taskList) {
//			int orgId = task.getBusTask().getTask_origin_id();
//			if (map.containsKey(orgId)) {
//				int count = map.get(orgId);
//				map.put(orgId, ++count);
//			} else {
//				map.put(orgId, 1);
//			}
//			//计算超时时间
//			int monitorId = task.getBusTask().getMonitor_id();
//			int recordLength = task.getBusTask().getRecordLength();
//			if (recordMap.containsKey(monitorId)) 
//				outTime += recordLength;
//			else recordMap.put(monitorId, monitorId);
//		}
//		
//		//根据任务来源区分实际收测单元的数量
//		Iterator<Map.Entry<Integer, Integer>> iter = map.entrySet().iterator();
//		while(iter.hasNext()) {
//			Map.Entry<Integer, Integer> element = iter.next();
//			int size = element.getValue();
//			if (size == 0) {
//				continue;
//			}
//			
//			int type = element.getKey();
////			String key = mode + "_" + type;
////			int recordLength = ConfigVariable.SOUND_RECORD_WAIT_TIME * size + 2;
//			//key增加收测时间间隔(可能含有多次时间间隔的数据同时生成收测单元) 手动自动类型 + 收测单元间隔  + 运行图类型
//			//key增加收测开始时间(如果系统重启，当前收测单元生成后下一次右侧单元也开始生成，增加开始时间不会将收测单元覆盖) 手动自动类型 + 收测单元间隔  + 开始时间 + 运行图类型
//			String key = mode + "_" + unitTime + "_" + begin_time + "_" + type;
//			//同步本次任务数量
//			MesureRecordModel.getInstance().syncMesureTask(key, size, channel.createMeaUnitTimeOut(key, mode,type, outTime));
//		}
//	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String arg0) {
		this.serviceName = arg0;
	}
}
