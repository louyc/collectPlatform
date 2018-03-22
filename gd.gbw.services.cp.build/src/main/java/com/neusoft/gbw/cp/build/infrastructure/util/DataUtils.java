package com.neusoft.gbw.cp.build.infrastructure.util;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.ExpandConstants;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskSchedule;

public class DataUtils {
//	public static long getMsgID(Object obj) {
//		return System.currentTimeMillis()+ obj.hashCode();
//	}
	
	public static long getMsgID(Object obj) { //临时将ID，减小，圣世琪不支持
		long id = Long.parseLong((System.currentTimeMillis()+ obj.hashCode() + "").substring(4));
		return id;
	}
	
	public static String getUniqueID(Object obj) {
		String time = String.valueOf(System.currentTimeMillis());
		return String.valueOf(time.hashCode()) + String.valueOf(obj.hashCode());
	}
	
//	public static long getTaskID(Object obj) {
//		return getMsgID(obj); 
//	}
	
	/**
	 * 原任务ID
	 * @param task
	 * @param operNum
	 * @return
	 */
//	public static long getOldTaskID(Task task,int operNum) {
//		ProtocolType type = null;
//		int taskID = task.getTaskFreq().getTask_id();
//		
//		int taskFreqID = task.getTaskFreq().getTaskfreq_id();
////		int timeID = task.getTaskSchedule().getTime_id();
//		StringBuffer buffer = new StringBuffer();
//		buffer.append(getPositionData(taskID, 4).substring(1)); //截取一位taskId,后边增加业务类型
//		buffer.append(getPositionData(taskFreqID, 5));
////		buffer.append(getPositionData(timeID, 5));
//		switch(operNum) {//操作类型，1为设置，2为删除，删除提取任务的原始类型
//		case 1:
//			type = task.getBuildType().getProType();
//			break;
//		case 2:
//			type = task.getBuildType().getOriProType();
//			break;
//		}
//		
//		if(type.name().startsWith("Quality"))
//			buffer.append("1");
//		if(type.name().startsWith("Spectrum"))
//			buffer.append("2");
//		if(type.name().startsWith("Stream"))
//			buffer.append("3");
//		if(type.name().startsWith("Offset"))
//			buffer.append("4");
//		return Long.parseLong(buffer.toString());
//	}
	
	/**
	 * 唯一ID确认
	 * taskName+monitorCode+freq+taskType+taskMode + schedule信息
	 * 
	 * QualityTaskSet
	 * SpectrumTaskSet
	 * StreamTaskSet
	 * OffsetTaskSet
	 * @param task
	 * @param operNum
	 * @return
	 */
	public static String getTaskID(Task task) {
		StringBuffer buffer = new StringBuffer();
		
		String taskName = task.getMeasureTask().getTask_name();
		buffer.append(taskName).append("_");
		String monitorCode = task.getTaskMonitor().getMonitor_code();
		buffer.append(monitorCode).append("_");
		String freq = task.getTaskFreq().getFreq();
		buffer.append(freq).append("_");
		String taskType = task.getBuildType().getOriProType().name();
		buffer.append(taskType).append("_");
		int taskMode = task.getMeasureTask().getTask_build_mode();
		buffer.append(taskMode);
		List<TaskSchedule> scheduleList = task.getScheduleList();
		for(TaskSchedule schedule : scheduleList) {
			String dayofweek = schedule.getDayofweek();
			buffer.append(dayofweek);
			int schedule_type = schedule.getSchedule_type();
			buffer.append(schedule_type);
			String start = schedule.getStarttime();
			buffer.append(start);
			String end = schedule.getEndtime();
			buffer.append(end);
		}
		//hashCode可能为负数
		String taskCode = (buffer.toString().hashCode() + "").replaceAll("-", "1");
		int length = taskCode.length();
		//不足8位进行补0
		for(int i=0;i<8-length;i++) {
			taskCode = taskCode + "0";
		}
		//头位可能有0,直接用hashcode7位
		String taskId = ("1" + taskCode).substring(0, 8);
//		Log.debug("---获取的任务串=" + buffer.toString() + ",taskId=" + taskId);
		return taskId;
	}
	
	/**
	 * 唯一ID，封装入CollectTask中的ID
	 * taskName+monitorCode+freq+taskType+taskMode + schedule信息
	 * @param task
	 * @param operNum
	 * @return
	 */
	public static String getTaskID(CollectTask task) {
		String taskId = task.getExpandObject(ExpandConstants.TASK_UNIQUE_ID).toString();
		return taskId;
	}

	public static String getCollectTaskID(Object obj) {
		return "COLLECT_" + getMsgID(obj);
	}
	
	public static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date()).toString();
	}
	
	public static String getCurrentTimeNoFormat() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(new Date()).toString();
	}
	
	public static List<TaskSchedule> sort(List<TaskSchedule> scheduleList) {
		Collections.sort(scheduleList, new Comparator<TaskSchedule>() {
			@Override
			public int compare(TaskSchedule o1, TaskSchedule o2) {
				String dayofweek = o1.getDayofweek();
				String oneTime = o1.getStarttime();
				String otherTime = o2.getStarttime();
				try {
					if(dayofweek.equals(BuildConstants.EVERY_DAY)) {
							if(TimeUtils.getTimeToMilliSecond(TimeUtils.getMergeTime(oneTime)) > TimeUtils.getTimeToMilliSecond(TimeUtils.getMergeTime(otherTime))) 
								return 1;
					}else {
							if(TimeUtils.getTimeToMilliSecond(oneTime) > TimeUtils.getTimeToMilliSecond(otherTime)) 
								return 0;
					}
					} catch (Exception e) {
				}
				return -1;
			}
			
		});
		return scheduleList;
	} 

	
	public static void main(String[] args) {
		String taskName1 = "乌海站_三满质量";
		String monitorCode1 = "SmP01";
		String freq1 = "222";
		String taskType1 = "Quality";
		String taskMode1 = "1";
		
		String taskName2 = "乌海站_三满质量";
		String monitorCode2 = "SmP01";
		String freq2 = "222";
		String taskType2 = "Quality";
		String taskMode2 = "1";
		
		String key1 = taskName1 + freq1 + monitorCode1 + taskMode1 + taskType1;
		String key2 = taskName2 + freq2 + monitorCode2 + taskMode2 + taskType2;
		
		System.out.println(key1.hashCode());
		System.out.println(key2.hashCode());
		int key1Code = key1.hashCode();
//		int key2Code = key2.hashCode();
		System.out.println((key1Code+"").replaceAll("-", "").substring(0, 8));
		System.out.println((key1Code+"").replaceAll("-", "").substring((key1Code+"").replaceAll("-", "").length()-9));
		
	}
}