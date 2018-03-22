package com.neusoft.gbw.cp.process.measure.channel.manual;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.conver.vo.Return;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.process.measure.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.measure.exception.MeasureDisposeException;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.np.arsf.base.bundle.Log;

public class UpdateSetStatusProcessor extends RecoverTaskProcessor implements ITaskProcess {
	
	@Override
	public Object disposeV8(CollectData data) throws MeasureDisposeException {
		List<StoreInfo> list = null;
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_success:
			list = dispose(data);
			sendStore(list);
			break;
		default:
			break;
		}
		return null;
	}

	@Override
	public Object disposeV7(CollectData data) throws MeasureDisposeException {
		List<StoreInfo> list = null;
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_success:
			list = dispose(data);
			sendStore(list);
			break;
		default:
			break;
		}
		return null;
	}

	@Override
	public Object disposeV6(CollectData data) throws MeasureDisposeException {
		List<StoreInfo> list = null;
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_success:
			list = dispose(data);
			sendStore(list);
			break;
		default:
			break;
		}
		return null;
	}

	@Override
	public Object disposeV5(CollectData data) throws MeasureDisposeException {
		List<StoreInfo> list = null;
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_success:
			list = dispose(data);
			sendStore(list);
			break;
		default:
			break;
		}
		return null;
	}
	
	//Key: task_id,  value: size
//	private Map<Integer, Integer> taskMap = null;
//	private CollectTaskModel model = null;
	
	public UpdateSetStatusProcessor() {
//		taskMap = new HashMap<Integer, Integer>();
//		model = CollectTaskModel.getInstance();
	}

	/**
	 * 更新处理两部分的状态
	 * 1. 当下发失败，更新主任务表中下发状态相关信息；
	 * 2. 更新任务周期表中同步下发状态；
	 */
	private List<StoreInfo> dispose(CollectData data) {
		Report report = (Report) data.getData().getReportData();
		Return ret = report.getReportReturn();
		List<StoreInfo> infoList = new ArrayList<StoreInfo>();
		CollectTask task = data.getCollectTask();
		if (ret.getValue().equals("0") || ret.getValue().equals("101") ) {
			//设置成功  (设置成功或者任务已存在都为成功)
//			infoList.add(updataTaskFreqScheduleSetStatus(task, true));
			infoList.add(updataTaskFreqSetStatus(task, true));
		} else {
			Log.warn("下发设置任务失败，任务信息=" + task.getBusTask().toString());
			//设置失败
			infoList.add(disposeTaskSetStatus(task, false));
//			infoList.add(updataTaskFreqScheduleSetStatus(task, false));
			infoList.add(updataTaskFreqSetStatus(task, false));
		}
		
		return infoList;
	}
	
//	private StoreInfo updateTaskSetStatus(CollectTask task) {
//		StoreInfo info = null;
//		int taskID = task.getBusTask().getTask_id();
//		int size = 1;
//		if (taskMap.containsKey(taskID)) {
//			size = taskMap.get(taskID);
//			if (isEqual(taskID, size)) {
//				//task数量相等，更新任务表下发状态
//				info = disposeTaskSetStatus(task, true);
//			} else {
//				//不相等，更新当前数量
//				taskMap.put(taskID, ++size);
//			}
//		} else {
//			if (isEqual(taskID, size)) {
//				//task数量为1时，更新任务表下发状态
//				info = disposeTaskSetStatus(task, true);  // add yanghao
//			} else {
//				taskMap.put(taskID, ++size);
//			}
//		}
//		
//		return info;
//	}
	
	private StoreInfo disposeTaskSetStatus(CollectTask task, boolean isSuccess) {
		StoreInfo info = new StoreInfo();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("task_id", task.getBusTask().getTask_id());
		dataMap.put("issue_user_id", "system");
		dataMap.put("issue_time", getCurrentTime());
		dataMap.put("task_status_id", isSuccess ? 1 : 0);
		//然后返回StoreInfo对象，一个collectData产生一个StoreInfo，
		info.setLabel("updateTaskSyncStatusRecords");
		info.setDataMap(dataMap);
		return info;
	}
	
//	private StoreInfo updataTaskFreqScheduleSetStatus(CollectTask task, boolean isSuccess) {
//		//构建设置类型的update存储对象
//		StoreInfo info = new StoreInfo();
//		HashMap<String, Object> dataMap = new HashMap<String, Object>();
//		dataMap.put("time_id", task.getBusTask().getTime_id());
//		dataMap.put("sync_status", isSuccess ? 1 : 0);
//		//然后返回StoreInfo对象，一个collectData产生一个StoreInfo，
//		info.setLabel("updateTaskFreqScheduleSyncStatusRecords");
//		info.setDataMap(dataMap);
//		return info;
//	}
	
	private StoreInfo updataTaskFreqSetStatus(CollectTask task, boolean isSuccess) {
		//构建设置类型的update存储对象
		StoreInfo info = new StoreInfo();
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("freq_id", task.getBusTask().getTaskfreq_id());
		dataMap.put("sync_status", isSuccess ? 1 : 0);
		//然后返回StoreInfo对象，一个collectData产生一个StoreInfo，
		info.setLabel("updateTaskFreqSyncStatusRecords");
		info.setDataMap(dataMap);
		return info;
	}
	
//	private boolean isEqual(int taskID, int size) {
//		int num = model.getSizeByTaskID(taskID);  
//		return num == size;
//	}
	
	private String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}
}
