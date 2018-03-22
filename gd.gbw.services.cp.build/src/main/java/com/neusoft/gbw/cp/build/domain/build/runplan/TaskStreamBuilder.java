package com.neusoft.gbw.cp.build.domain.build.runplan;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.CollectTaskType;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.np.arsf.base.bundle.Log;

public class TaskStreamBuilder {
	
	public static CollectTask buildTaskStreamOver(CollectTask task) {
		String url = (String)task.getExpandObject(ExpandConstants.STREAM_STOP_CONTROL_KEY);
		Query query = (Query)task.getData().getQuery();
		int version = task.getData().getProVersion();
		switch(version) {
		case 8:
			com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamRealtimeQuery v8Stream = (com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamRealtimeQuery)query.getQuery();
			v8Stream.getRealtimeStreams().get(0).setLastURL(url);
			v8Stream.getRealtimeStreams().get(0).setAction(BuildConstants.STREAM_STOP);
			break;
		case 7:
			com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamRealtimeQuery v7Stream = (com.neusoft.gbw.cp.conver.v7.protocol.vo.audio.StreamRealtimeQuery)query.getQuery();
			v7Stream.getRealtimeStreams().get(0).setLastURL(url);
			v7Stream.getRealtimeStreams().get(0).setAction(BuildConstants.STREAM_STOP);
			break;
		case 6:
			com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamRealtimeQuery v6Stream = (com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamRealtimeQuery)query.getQuery();
			v6Stream.getRealtimeStreams().get(0).setLastURL(url);
			v6Stream.getRealtimeStreams().get(0).setAction(BuildConstants.STREAM_STOP);
			break;
		case 5:
			com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamRealtimeQuery v5Stream = (com.neusoft.gbw.cp.conver.v5.protocol.vo.audio.StreamRealtimeQuery)query.getQuery();
			v5Stream.getRealtimeStreams().get(0).setLastURL(url);
			v5Stream.getRealtimeStreams().get(0).setAction(BuildConstants.STREAM_STOP);
			break;
		}
		
		task.getAttrInfo().setColTaskType(CollectTaskType.stop);
		Log.debug("[构建服务]收测单元停止释放接收机任务构建，" + getLogContent(task));
		return task;
	}
	
	
	
	public static CollectTask buildTSV8RecordTask(CollectTask task) {
		Query query = (Query)task.getData().getQuery();
		com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamRealtimeQuery stream = (com.neusoft.gbw.cp.conver.v8.protocol.vo.audio.StreamRealtimeQuery)query.getQuery();
		stream.getRealtimeStreams().get(0).setEncode(BuildConstants.PCM_ENCODE);
		stream.getRealtimeStreams().get(0).setBps(BuildConstants.V8_BPS);
		return task;
	}
	
	private static String getLogContent(CollectTask task) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("TaskID=" + task.getBusTask().getTask_id() + ",");
		buffer.append("TaskFreq=" + task.getBusTask().getFreq() + ",");
		buffer.append("MonitorID=" + task.getBusTask().getMonitor_id() + ",");
		buffer.append("MonitorCode=" + task.getBusTask().getMonitor_code() + ",");
		buffer.append("EquCode=" + task.getAttrInfo().getFirstEquCode() + ",");
		buffer.append("TaskKey=" + getCollectOccupID(task));
		return buffer.toString();
	}
	
	
	private static String getCollectOccupID(CollectTask task) {
		Object id = task.getExpandObject(ExpandConstants.COLLECT_OCCUP_KEY);
		return id == null?null : id.toString();
	}


//	public static List<CollectTask> buildCollectTaskRTStream(Task task) {
//		List<CollectTask> collectTaskList = new ArrayList<CollectTask>();
//		List<TaskSchedule> scheduleList = task.getTaskScheduleList();
//		CollectTask collectTask = null;
//		for(TaskSchedule schedule : scheduleList) {
//			try {
//				collectTask = buildCollectTask(task, schedule);
//			} catch (TimeException e) {
//				Log.error(e.getMessage(), e);
//				continue;
//			}
//			collectTaskList.add(collectTask);
//		}
//		
//		return collectTaskList;
//	}
//	
//	private static CollectTask buildCollectTask(Task task, TaskSchedule schedule) throws TimeException {
//		CollectTask collectTask = new CollectTask();
//		collectTask.setTransfer(getTransferInfo(task));
//		collectTask.setSchedule(getScheduleInfo());
//		collectTask.setCollectType(CollectType.realtime);
//		collectTask.setCollectTaskID(DataUtils.getCollectTaskID(collectTask));
//		collectTask.setQuery(buildRealTimeStreamQuery(task));
//		collectTask.setBusTask(buildBusinessTask(task, schedule));
//		
//		return collectTask;
//	}
}
