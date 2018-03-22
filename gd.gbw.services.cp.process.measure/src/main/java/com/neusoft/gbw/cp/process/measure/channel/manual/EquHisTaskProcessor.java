package com.neusoft.gbw.cp.process.measure.channel.manual;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.conver.vo.IReport;
import com.neusoft.gbw.cp.conver.vo.Report;
import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.ReportStatus;
import com.neusoft.gbw.cp.process.measure.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.measure.constants.ProcessConstants;
import com.neusoft.gbw.cp.process.measure.exception.MeasureDisposeException;
import com.neusoft.gbw.cp.process.measure.vo.manual.EquipHistoryDataStore;
import com.neusoft.gbw.cp.store.vo.StoreInfo;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;

/**
 * 设备历史日志返回接收处理类
 * @author lyc
 *
 */
public class EquHisTaskProcessor extends RecoverTaskProcessor implements ITaskProcess {

	//	private final static String spectrum = "taskSpectrum";
	//	private final static String quality_desc = "频谱任务回收";
//	private CollectTaskModel model = CollectTaskModel.getInstance();
	//	private FtpDownProcess ftpProcess = new FtpDownProcess();

	@Override
	public Object disposeV8(CollectData data) throws MeasureDisposeException {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_active_report:
			list.addAll(saveDataV8(data));
			//处理任务数据，更新任务状态
//			disposeTaskData(list, data, TaskType.SpectrumTask, true);
			//先构建删除数据，并执行
			delData(list);
			sleep(ProcessConstants.RECOVER_DATA_DEL_WAIT_TIME);
			//在构建插入数据，并执行
			insertData(list);
			break;
		case date_collect_success:
			//如果是主动上报的zip压缩文件，会同时返回成功一个回执，丢弃这个回执
			if(isActive(data))  
				break;
			list.addAll(saveDataV8(data));
			//处理任务数据，更新任务状态
//			disposeTaskData(list, data, TaskType.SpectrumTask, true);
			//先构建删除数据，并执行
			delData(list);
			sleep(ProcessConstants.RECOVER_DATA_DEL_WAIT_TIME);
			//在构建插入数据，并执行
			insertData(list);
			break;
		default:
			Log.warn("[任务回收服务]设备日志回收处理异常,任务执行状态：" + status + "," + getLogMsg(data));
			//更新任务回收计数
//			updateRecoverCount(data, false);
			break;
		}
		return list;
	}

	private void delData(List<StoreInfo> list) {
		List<StoreInfo> resultList = new ArrayList<StoreInfo>();
		for(StoreInfo info : list) {
			if (info.getLabel().startsWith("delete"))
				resultList.add(info);
		}
		sendStore(resultList);
	}
	
	private void insertData(List<StoreInfo> list) {
		List<StoreInfo> resultList = new ArrayList<StoreInfo>();
		for(StoreInfo info : list) {
			if (info.getLabel().startsWith("insert"))
				resultList.add(info);
		}
		sendStore(resultList);
	}
	
	
	@Override
	public Object disposeV7(CollectData data) throws MeasureDisposeException {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_active_report:
			list.addAll(saveDataV7(data));
			//处理任务数据，更新任务状态
//			disposeTaskData(list, data, TaskType.SpectrumTask, true);
			//先构建删除数据，并执行
			delData(list);
			sleep(ProcessConstants.RECOVER_DATA_DEL_WAIT_TIME);
			//在构建插入数据，并执行
			insertData(list);
			break;
		case date_collect_success:
			//如果是主动上报的zip压缩文件，会同时返回成功一个回执，丢弃这个回执
			if(isV7Active(data))  
				break;

			list.addAll(saveDataV7(data));
			//处理任务数据，更新任务状态
//			disposeTaskData(list, data, TaskType.SpectrumTask, true);
			//先构建删除数据，并执行
			delData(list);
			sleep(ProcessConstants.RECOVER_DATA_DEL_WAIT_TIME);
			//在构建插入数据，并执行
			insertData(list);
			break;
		default:
			Log.warn("[任务回收服务]设备日志回收处理异常,任务执行状态：" + status + "," + getLogMsg(data));
			//更新任务回收计数
//			updateRecoverCount(data, false);
			break;
		}
		return list;
	}

	@Override
	public Object disposeV6(CollectData data) throws MeasureDisposeException {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_active_report:
			list.addAll(saveDataV6(data));
			//处理任务数据，更新任务状态
//			disposeTaskData(list, data, TaskType.SpectrumTask, true);
			//先构建删除数据，并执行
			delData(list);
			sleep(ProcessConstants.RECOVER_DATA_DEL_WAIT_TIME);
			//在构建插入数据，并执行
			insertData(list);
			break;
		case date_collect_success:
			//如果是主动上报的zip压缩文件，会同时返回成功一个回执，丢弃这个回执
			if(isV6Active(data))  
				break;

			list.addAll(saveDataV6(data));
			//处理任务数据，更新任务状态
//			disposeTaskData(list, data, TaskType.SpectrumTask, true);
			//先构建删除数据，并执行
			delData(list);
			sleep(ProcessConstants.RECOVER_DATA_DEL_WAIT_TIME);
			//在构建插入数据，并执行
			insertData(list);
			break;
		default:
			Log.warn("[任务回收服务]设备日志回收处理异常,任务执行状态：" + status + "," + getLogMsg(data));
			//更新任务回收计数
//			updateRecoverCount(data, false);
			break;
		}
		return list;
	}

	@Override
	public Object disposeV5(CollectData data) throws MeasureDisposeException {
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		ReportStatus status = data.getStatus();
		switch(status) {
		case date_collect_active_report:
			list.addAll(saveDataV5(data));
			//处理任务数据，更新任务状态
//			disposeTaskData(list, data, TaskType.SpectrumTask, true);
			//先构建删除数据，并执行
			delData(list);
			sleep(ProcessConstants.RECOVER_DATA_DEL_WAIT_TIME);
			//在构建插入数据，并执行
			insertData(list);
			break;
		case date_collect_success:
			//如果是主动上报的zip压缩文件，会同时返回成功一个回执，丢弃这个回执
			if(isV5Active(data))  
				break;

			list.addAll(saveDataV5(data));
			//处理任务数据，更新任务状态
//			disposeTaskData(list, data, TaskType.SpectrumTask, true);
			//先构建删除数据，并执行
			delData(list);
			sleep(ProcessConstants.RECOVER_DATA_DEL_WAIT_TIME);
			//在构建插入数据，并执行
			insertData(list);
			break;
		default:
			Log.warn("[任务回收服务]设备日志回收处理异常,任务执行状态：" + status + "," + getLogMsg(data));
			//更新任务回收计数
//			updateRecoverCount(data, false);
			break;
		}
		return list;
	}

	private List<StoreInfo> saveDataV8(CollectData data) {
		EquipHistoryDataStore eData = new EquipHistoryDataStore();
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentLogHistoryQueryR report = null;

		IReport ireport = (IReport) ((Report)data.getData().getReportData()).getReport();
		if(ireport instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.AutoReportFile) {
			return list;
		}else {
			report = getV8SpectrumHistoryReport(data);
		}
		if(report == null) 
			return list;
		List<com.neusoft.gbw.cp.conver.v8.protocol.vo.other.LogItem> ssList = report.getLogItems();
		if(ssList == null || ssList.size() <=0 )
			return list;
		for(com.neusoft.gbw.cp.conver.v8.protocol.vo.other.LogItem item : ssList) {
			eData.setDateTime(item.getDateTime());
			eData.setDesc(item.getDesc());
			eData.setName(item.getName());
			StoreInfo dinfo = new StoreInfo();
			StoreInfo info = new StoreInfo();
			try {
				Map<String, Object> map = NMBeanUtils.getObjectField(eData);
				dinfo.setDataMap(map);
				dinfo.setLabel("先删除 sql操作");
				list.add(info);
				info.setDataMap(map);
				info.setLabel("再增加 sql操作");
				list.add(info);
			} catch (NMBeanUtilsException e) {
				Log.error("", e);
			}
		}
		ssList.clear();
		ssList = null;
		return list;
	}

	private List<StoreInfo> saveDataV7(CollectData data) {
		EquipHistoryDataStore eData = new EquipHistoryDataStore();
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentLogHistoryQueryR report = null;

		IReport ireport = (IReport) ((Report)data.getData().getReportData()).getReport();
		if(ireport instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.AutoReportFile) {
			return list;
		}else {
			report = getV7SpectrumHistoryReport(data);
		}
		if(report == null) 
			return list;
		List<com.neusoft.gbw.cp.conver.v7.protocol.vo.other.LogItem> ssList = report.getLogItems();
		if(ssList == null || ssList.size() <=0 )
			return list;
		for(com.neusoft.gbw.cp.conver.v7.protocol.vo.other.LogItem item : ssList) {
			eData.setDateTime(item.getDateTime());
			eData.setDesc(item.getDesc());
			eData.setName(item.getName());
			StoreInfo dinfo = new StoreInfo();
			StoreInfo info = new StoreInfo();
			try {
				Map<String, Object> map = NMBeanUtils.getObjectField(eData);
				dinfo.setDataMap(map);
				dinfo.setLabel("先删除 sql操作");
				list.add(info);
				info.setDataMap(map);
				info.setLabel("再增加 sql操作");
				list.add(info);
			} catch (NMBeanUtilsException e) {
				Log.error("", e);
			}
		}
		ssList.clear();
		ssList = null;
		return list;
	}

	private List<StoreInfo> saveDataV6(CollectData data) {
		EquipHistoryDataStore eData = new EquipHistoryDataStore();
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentLogHistoryQueryR report = null;

		IReport ireport = (IReport) ((Report)data.getData().getReportData()).getReport();
		if(ireport instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.AutoReportFile) {
			return list;
		}else {
			report = getV6SpectrumHistoryReport(data);
		}
		if(report == null) 
			return list;
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.other.LogItem> ssList = report.getLogItems();
		if(ssList == null || ssList.size() <=0 )
			return list;
		for(com.neusoft.gbw.cp.conver.v6.protocol.vo.other.LogItem item : ssList) {
			eData.setDateTime(item.getDateTime());
			eData.setDesc(item.getDesc());
			eData.setName(item.getName());
			StoreInfo dinfo = new StoreInfo();
			StoreInfo info = new StoreInfo();
			try {
				Map<String, Object> map = NMBeanUtils.getObjectField(eData);
				dinfo.setDataMap(map);
				dinfo.setLabel("先删除 sql操作");
				list.add(info);
				info.setDataMap(map);
				info.setLabel("再增加 sql操作");
				list.add(info);
			} catch (NMBeanUtilsException e) {
				Log.error("", e);
			}
		}
		ssList.clear();
		ssList = null;
		return list;
	}

	private List<StoreInfo> saveDataV5(CollectData data) {
		EquipHistoryDataStore eData = new EquipHistoryDataStore();
		List<StoreInfo> list = new ArrayList<StoreInfo>();
		com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentLogHistoryQueryR report = null;

		IReport ireport = (IReport) ((Report)data.getData().getReportData()).getReport();
		if(ireport instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.AutoReportFile) {
			return list;
		}else {
			report = getV5SpectrumHistoryReport(data);
		}
		if(report == null) 
			return list;
		List<com.neusoft.gbw.cp.conver.v5.protocol.vo.other.LogItem> ssList = report.getLogItems();
		if(ssList == null || ssList.size() <=0 )
			return list;
		for(com.neusoft.gbw.cp.conver.v5.protocol.vo.other.LogItem item : ssList) {
			eData.setDateTime(item.getDateTime());
			eData.setDesc(item.getDesc());
			eData.setName(item.getName());
			StoreInfo dinfo = new StoreInfo();
			StoreInfo info = new StoreInfo();
			try {
				Map<String, Object> map = NMBeanUtils.getObjectField(eData);
				dinfo.setDataMap(map);
				dinfo.setLabel("先删除 sql操作");
				list.add(info);
				info.setDataMap(map);
				info.setLabel("再增加 sql操作");
				list.add(info);
			} catch (NMBeanUtilsException e) {
				Log.error("", e);
			}
		}
		ssList.clear();
		ssList = null;
		return list;
	}

	private com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentLogHistoryQueryR  getV7SpectrumHistoryReport(CollectData data) {
		return ((com.neusoft.gbw.cp.conver.v7.protocol.vo.other.EquipmentLogHistoryQueryR)((Report)data.getData().getReportData()).getReport());
	}

	private com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentLogHistoryQueryR getV6SpectrumHistoryReport(CollectData data) {
		return ((com.neusoft.gbw.cp.conver.v6.protocol.vo.other.EquipmentLogHistoryQueryR)((Report)data.getData().getReportData()).getReport());
	}

	private com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentLogHistoryQueryR getV5SpectrumHistoryReport(CollectData data) {
		return ((com.neusoft.gbw.cp.conver.v5.protocol.vo.other.EquipmentLogHistoryQueryR)((Report)data.getData().getReportData()).getReport());
	}

	private com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentLogHistoryQueryR getV8SpectrumHistoryReport(CollectData data) {
		return ((com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentLogHistoryQueryR)((Report)data.getData().getReportData()).getReport());
	}

	private String getLogMsg(CollectData data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("monitorID=" + data.getCollectTask().getBusTask().getMonitor_id() + ",");
		buffer.append("monitorCode=" + data.getCollectTask().getBusTask().getMonitor_code() + ",");
		return buffer.toString();
	}

	private boolean isActive(CollectData data) {
		boolean isActive = true;
		IReport ireport = null;
		try{
			ireport = (IReport) ((Report)data.getData().getReportData()).getReport();	
		}catch(Exception e) {
			return isActive;
		}
		if(ireport == null)
			return false;

		if(ireport instanceof com.neusoft.gbw.cp.conver.v8.protocol.vo.other.AutoReportFile) 
			//上报的zip解压处理的过程放入过滤的filter服务
			return isActive;
		isActive = false;
		return isActive;
	}

	private boolean isV7Active(CollectData data) {
		boolean isActive = true;
		IReport ireport = null;
		try{
			ireport = (IReport) ((Report)data.getData().getReportData()).getReport();	
		}catch(Exception e) {
			return isActive;
		}
		if(ireport == null)
			return false;

		if(ireport instanceof com.neusoft.gbw.cp.conver.v7.protocol.vo.other.AutoReportFile) 
			//上报的zip解压处理的过程放入过滤的filter服务
			return isActive;
		isActive = false;
		return isActive;
	}

	private boolean isV6Active(CollectData data) {
		boolean isActive = true;
		IReport ireport = null;
		try{
			ireport = (IReport) ((Report)data.getData().getReportData()).getReport();	
		}catch(Exception e) {
			return isActive;
		}
		if(ireport == null)
			return false;

		if(ireport instanceof com.neusoft.gbw.cp.conver.v6.protocol.vo.other.AutoReportFile) 
			//上报的zip解压处理的过程放入过滤的filter服务
			return isActive;
		isActive = false;
		return isActive;
	}

	private boolean isV5Active(CollectData data) {
		boolean isActive = true;
		IReport ireport = null;
		try{
			ireport = (IReport) ((Report)data.getData().getReportData()).getReport();	
		}catch(Exception e) {
			return isActive;
		}
		if(ireport == null)
			return false;

		if(ireport instanceof com.neusoft.gbw.cp.conver.v5.protocol.vo.other.AutoReportFile) 
			//上报的zip解压处理的过程放入过滤的filter服务
			return isActive;
		isActive = false;
		return isActive;
	}

}
