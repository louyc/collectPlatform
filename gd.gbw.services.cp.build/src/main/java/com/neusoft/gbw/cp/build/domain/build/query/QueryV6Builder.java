package com.neusoft.gbw.cp.build.domain.build.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.app.evaluation.intf.dto.KpiRealtimeQueryDTO;
import com.neusoft.gbw.app.evaluation.intf.dto.SpectrumRealtimeQueryDTO;
import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniControlDTO;
import com.neusoft.gbw.app.monitorMgr.intf.dto.MoniDeviceStatusDTO;
import com.neusoft.gbw.cp.build.domain.build.IQueryBuild;
import com.neusoft.gbw.cp.build.domain.build.equ.EquAlarmParamSetV6Builder;
import com.neusoft.gbw.cp.build.domain.build.equ.EquInitParamSetV6Builder;
import com.neusoft.gbw.cp.build.domain.build.equ.EquipmentStatusTaskV6Builder;
import com.neusoft.gbw.cp.build.domain.build.equ.ProgramCommandTaskBuilder;
import com.neusoft.gbw.cp.build.domain.build.quality.QualityAlarmParamSetV6Builder;
import com.neusoft.gbw.cp.build.domain.build.quality.QualityRealtimeQueryV6Builder;
import com.neusoft.gbw.cp.build.domain.build.spectrum.SpectrumRealtimeQueryV6Builder;
import com.neusoft.gbw.cp.build.infrastructure.constants.BuildConstants;
import com.neusoft.gbw.cp.build.infrastructure.constants.ConfigVariable;
import com.neusoft.gbw.cp.build.infrastructure.exception.BuildException;
import com.neusoft.gbw.cp.build.infrastructure.util.DataUtils;
import com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.RealtimeStream;
import com.neusoft.gbw.cp.conver.v6.protocol.vo.audio.StreamRealtimeQuery;
import com.neusoft.gbw.cp.conver.v6.protocol.vo.other.FileRetrieve;
import com.neusoft.gbw.cp.conver.v6.protocol.vo.other.FileRetrieve_File;
import com.neusoft.gbw.cp.conver.v6.protocol.vo.other.TaskDelete;
import com.neusoft.gbw.cp.conver.v6.protocol.vo.other.TaskStatusQuery;
import com.neusoft.gbw.cp.conver.v8.protocol.vo.other.EquipmentLogHistoryQuery;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.core.collect.TaskPriority;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.load.data.build.domain.model.DataMgrCentreModel;
import com.neusoft.gbw.cp.load.data.build.domain.vo.Task;
import com.neusoft.gbw.cp.load.data.build.domain.vo.TaskOnlineListener;
import com.neusoft.gbw.domain.monitor.intf.dto.MoniInspectResultDTO;
import com.neusoft.gbw.domain.monitor.intf.dto.MoniSetingDTO;
import com.neusoft.gbw.domain.task.effect.intf.dto.RealTimeStreamDTO;
import com.neusoft.gbw.domain.task.mgr.intf.dto.DownLoadFileDTO;
import com.neusoft.gbw.domain.task.mgr.intf.dto.TaskRecordDataDTO;
import com.neusoft.gbw.domain.util.ConverterUtil;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMFormateException;
import com.neusoft.np.arsf.common.util.NPJsonUtil;
import com.neusoft.np.arsf.net.core.NetEventProtocol;

public class QueryV6Builder extends QueryV6SetBuilder implements IQueryBuild {

	@Override
	public Object buildQualityAlarmHistoryQuery(Object obj, TaskPriority taskPriority) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object buildQualityAlarmParamSet(Object obj, TaskPriority taskPriority) {
		if (obj instanceof Map) {
			Map<String, String> map = (HashMap<String, String>)obj;
			String request = map.get(NetEventProtocol.SYNT_REQUEST);
			MoniSetingDTO dto = null;
			try {
				dto = (MoniSetingDTO)ConverterUtil.xmlToObj(request);
				return QualityAlarmParamSetV6Builder.buildQualityAlarmParamSet(dto, taskPriority);
			} catch (Exception e) {
				Log.error("", e);
				return null;
			}
		}
		return null;
	}

	@Override
	public Object buildQualityRealtimeQuery(Object obj, TaskPriority taskPriority) {
		try {
			return QualityRealtimeQueryV6Builder.buildQualityRealtimeQuery((KpiRealtimeQueryDTO)obj, taskPriority);
		} catch (BuildException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Object buildStreamRealtimeQuery(Object obj, TaskPriority taskPriority) {
		if (obj instanceof Task) {
			return buildAutoStreamQuery((Task)obj, taskPriority);
		} else if (obj instanceof RealTimeStreamDTO) {
			RealTimeStreamDTO dto = (RealTimeStreamDTO) obj;
			return buildStreamRealtimeQuery(dto, taskPriority);
		} else if (obj instanceof TaskOnlineListener)
			return buildOnlineStreamQuery((TaskOnlineListener)obj, taskPriority);
		return null;
	}

	private Object buildAutoStreamQuery(Task task, TaskPriority taskPriority) {
		Query query = new Query();
		StreamRealtimeQuery streamQuery = new StreamRealtimeQuery();
		RealtimeStream stream = new RealtimeStream();

		long msgID = DataUtils.getMsgID(streamQuery);
		query.setMsgID(msgID + "");
		query.setVersion(BuildConstants.XML_VERSION_6+"");
		query.setType(BuildConstants.MSG_DOWN_TYPE);
		query.setDateTime(DataUtils.getCurrentTime());
		query.setSrcCode(ConfigVariable.MSG_SRC_CODE);
		//		query.setDstCode(task.getTaskMonitor().getMonitor_code());
		if(null!=task.getTaskMonitorList() && task.getTaskMonitorList().size()>0){
			query.setDstCode(task.getTaskMonitorList().get(0).getMonitor_code());
		}else{
			query.setDstCode(task.getTaskMonitor().getMonitor_code());
		}
		query.setPriority(BuildConstants.MAX_PRIORITY);
		query.setQuery(streamQuery);

		stream.setAction(BuildConstants.STREAM_START);
		stream.setBand(task.getTaskFreq().getBand()+"");
		stream.setBps(BuildConstants.V5_BPS);
		stream.setFreq(task.getTaskFreq().getFreq());
		stream.setEquCode(task.getTaskFreq().getReceiver_code());
		stream.setLastURL("");
		stream.setEncode(BuildConstants.MPEG2_ENCODE);
		stream.setIndex(String.valueOf(msgID));
		streamQuery.addRealtimeStream(stream);

		return query;
	}

	private Object buildOnlineStreamQuery(TaskOnlineListener task, TaskPriority taskPriority) {
		Query query = new Query();
		StreamRealtimeQuery streamQuery = new StreamRealtimeQuery();
		RealtimeStream stream = new RealtimeStream();

		long msgID = DataUtils.getMsgID(streamQuery);
		query.setMsgID(msgID + "");
		query.setVersion(BuildConstants.XML_VERSION_6+"");
		query.setType(BuildConstants.MSG_DOWN_TYPE);
		query.setDateTime(DataUtils.getCurrentTime());
		query.setSrcCode(ConfigVariable.MSG_SRC_CODE);
		MonitorDevice info = null;
		long monitorId = task.getMonitorId();
		info = DataMgrCentreModel.getInstance().getMonitorDevice(monitorId);
		if (info == null) {
			Log.warn("[在线监听任务构建]为指定的站点信息 monitorId=" + monitorId);
		} else {
			query.setDstCode(info.getMonitor_code());
		}
		query.setPriority(BuildConstants.MAX_PRIORITY);
		query.setQuery(streamQuery);

		stream.setAction(BuildConstants.STREAM_START);
		stream.setBand(task.getBand() + "");
		stream.setBps(BuildConstants.V5_BPS);
		stream.setFreq(task.getFreq() + "");
		stream.setEquCode(task.getEquCode());
		stream.setLastURL("");
		stream.setEncode(BuildConstants.MPEG2_ENCODE);
		stream.setIndex(String.valueOf(msgID));
		streamQuery.addRealtimeStream(stream);

		return query;
	}

	private Object buildStreamRealtimeQuery(RealTimeStreamDTO realtimeDTO, TaskPriority taskPriority) {
		Query query = new Query();
		StreamRealtimeQuery streamQuery = new StreamRealtimeQuery();
		RealtimeStream stream = new RealtimeStream();

		long msgID = DataUtils.getMsgID(streamQuery);
		query.setMsgID(msgID+ "");
		query.setVersion(BuildConstants.XML_VERSION_6+"");
		query.setType(BuildConstants.MSG_DOWN_TYPE);
		query.setDateTime(DataUtils.getCurrentTime());
		query.setSrcCode(ConfigVariable.MSG_SRC_CODE);

		long monitorId = Long.parseLong(realtimeDTO.getMonitorId());
		MonitorDevice info = null;
		info = DataMgrCentreModel.getInstance().getMonitorDevice(monitorId);
		if (info == null) {
			Log.warn("[实时音频任务构建]为指定的站点信息 monitorId=" + monitorId);
		} else {
			query.setDstCode(info.getMonitor_code());
		}
		query.setPriority(BuildConstants.MAX_PRIORITY);

		stream.setAction(realtimeDTO.getCommand());
		stream.setBand(realtimeDTO.getBand());
		stream.setBps(realtimeDTO.getBps());
		stream.setFreq(realtimeDTO.getFreq());
		stream.setEquCode(realtimeDTO.getEquCode());
		stream.setLastURL(realtimeDTO.getLastUrl());
		stream.setEncode(realtimeDTO.getEncode());
		stream.setIndex(String.valueOf(msgID));
		streamQuery.addRealtimeStream(stream);
		query.setQuery(streamQuery);

		return query;
	}
	@Override
	public Object buildSpectrumRealtimeQuery(Object obj, TaskPriority taskPriority) {
		try {
			return SpectrumRealtimeQueryV6Builder.buildSpectrumRealtimeQuery((SpectrumRealtimeQueryDTO)obj, taskPriority);
		} catch (BuildException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Object buildVideoRealtimeQuery(Object obj, TaskPriority taskPriority) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object buildEquAlarmHistoryQuery(Object obj, TaskPriority taskPriority) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object buildEquAlarmParamSet(Object obj, TaskPriority taskPriority) {
		if (obj instanceof Map) {
			Map<String, String> map = (HashMap<String, String>)obj;
			String request = map.get(NetEventProtocol.SYNT_REQUEST);
			MoniSetingDTO dto = null;
			try {
				dto = (MoniSetingDTO)ConverterUtil.xmlToObj(request);
				return EquAlarmParamSetV6Builder.buildEquAlarmParamSet(dto,taskPriority);
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	@Override
	public Object buildEquStatusRealtimeQuery(Object obj, TaskPriority taskPriority) {
		String monitorId = null;
		if(obj instanceof MoniDeviceStatusDTO) {
			MoniDeviceStatusDTO statusDto = (MoniDeviceStatusDTO) obj;
			monitorId = statusDto.getMonitorId();
		}
		if(obj instanceof MoniInspectResultDTO) {
			MoniInspectResultDTO statusDto = (MoniInspectResultDTO) obj;
			monitorId = statusDto.getMonitorId();
		}
		try {
			return EquipmentStatusTaskV6Builder.buildRealTimeStreamEquipmentStatusQuery(monitorId,taskPriority);
		} catch (BuildException e) {
			e.printStackTrace();
			return null;
		}
	}

	//20161013   设备历史日志查询 lyc
	@Override
	public Object buildEquLogHistoryQuery(Object obj, TaskPriority taskPriority) {
		String monitorId = null;
		String  startDateTime=null;
		String  endDateTime=null;
		MoniDeviceStatusDTO statusDto = (MoniDeviceStatusDTO) obj;
		monitorId = statusDto.getMonitorId();
		startDateTime=statusDto.getExpireTime();
		endDateTime=statusDto.getReportInterval();

		Query query = new Query();
		EquipmentLogHistoryQuery quert = new EquipmentLogHistoryQuery();
		quert.setEndDateTime(startDateTime);
		quert.setEndDateTime(endDateTime);
		query.setVersion(BuildConstants.XML_VERSION_6 + "");
		Long msgID = DataUtils.getMsgID(quert);
		query.setMsgID(msgID+"");
		query.setType(BuildConstants.MSG_DOWN_TYPE);
		query.setDateTime(DataUtils.getCurrentTime());
		query.setSrcCode(ConfigVariable.MSG_SRC_CODE);
		query.setPriority(taskPriority.getCollectPriority()+"");
		query.setQuery(quert);
		long monitorID = Long.parseLong(monitorId);
		MonitorDevice info = null;
		info = DataMgrCentreModel.getInstance().getMonitorDevice(monitorID);
		if (info == null) {
			Log.info("获取设备信息为空 ID=" + monitorID);
		} else {
			Log.debug("[任务构建]设备状态任务获取 monitorCode=" + info.getMonitor_code());
			query.setDstCode(info.getMonitor_code());
		}
		return query;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object buildEquInitParamSet(Object obj, TaskPriority taskPriority) {
		if (obj instanceof Map) {
			Map<String, String> map = (HashMap<String, String>)obj;
			String request = map.get(NetEventProtocol.SYNT_REQUEST);
			MoniSetingDTO dto = null;
			try {
				dto = (MoniSetingDTO)ConverterUtil.xmlToObj(request);
				return EquInitParamSetV6Builder.buildEquInitParamSet(dto,taskPriority);
			} catch (Exception e) {
				Log.error("", e);
				return null;
			}
		}
		return null;
	}

	@Override
	public Object buildReceiverControl(Object obj, TaskPriority taskPriority) {

		return null;
	}

	@Override
	public Object buildTaskDelete(Object obj, TaskPriority taskPriority) {
		Task task = (Task) obj;
		Query query = new Query();
		TaskDelete delTask = new TaskDelete();
		delTask.setTaskID(DataUtils.getTaskID(task)+"");
		delTask.setBand("");
		delTask.setFreq(task.getTaskFreq().getFreq());
		delTask.setStartDateTime(task.getMeasureTask().getValidity_b_time());
		delTask.setEndDateTime(task.getMeasureTask().getValidity_e_time());
		delTask.setSrcCode("");
		delTask.setDate("");
		delTask.setTaskType("");
		query.setVersion(BuildConstants.XML_VERSION_6+"");
		Long msgID = DataUtils.getMsgID(delTask);
		query.setMsgID(msgID+"");
		query.setType(BuildConstants.MSG_DOWN_TYPE);
		query.setDateTime(DataUtils.getCurrentTime());
		query.setSrcCode(ConfigVariable.MSG_SRC_CODE);
		query.setPriority(taskPriority.getCollectPriority()+"");
		//		query.setDstCode(task.getTaskMonitor().getMonitor_code());
		if(null!=task.getTaskMonitorList() && task.getTaskMonitorList().size()>0){
			query.setDstCode(task.getTaskMonitorList().get(0).getMonitor_code());
		}else{
			query.setDstCode(task.getTaskMonitor().getMonitor_code());
		}
		query.setQuery(delTask);
		return query;
	}

	@Override
	public Object buildTaskStatusQuery(Object obj, TaskPriority taskPriority) {
		Query query = new Query();
		List<com.neusoft.gbw.cp.conver.v6.protocol.vo.other.Task> taskList = new ArrayList<com.neusoft.gbw.cp.conver.v6.protocol.vo.other.Task>();
		TaskStatusQuery status = new TaskStatusQuery();
		query.setVersion(BuildConstants.XML_VERSION_6+"");
		Long msgID = DataUtils.getMsgID(status);
		query.setMsgID(msgID+"");
		query.setType(BuildConstants.MSG_DOWN_TYPE);
		query.setDateTime(DataUtils.getCurrentTime());
		query.setSrcCode(ConfigVariable.MSG_SRC_CODE);
		query.setPriority(taskPriority.getCollectPriority()+"");

		if(obj instanceof MoniInspectResultDTO) {
			MoniInspectResultDTO monitorDto = (MoniInspectResultDTO)obj;
			long monitorId = Long.parseLong(monitorDto.getMonitorId());
			MonitorDevice info = DataMgrCentreModel.getInstance().getMonitorDevice(monitorId);
			if (info == null) {
				Log.warn("[在线监听任务构建]为指定的站点信息 monitorId=" + monitorId);
			} else {
				query.setDstCode(info.getMonitor_code());
			}
			String taskIds = monitorDto.getResultId();
			String[] array = taskIds.split(",");
			for(String taskid : array) {
				com.neusoft.gbw.cp.conver.v6.protocol.vo.other.Task ptask = new com.neusoft.gbw.cp.conver.v6.protocol.vo.other.Task();
				ptask.setTaskID(taskid);
				taskList.add(ptask);
			}

		}
		status.setTasks(taskList);
		query.setQuery(status);
		// TODO Auto-generated method stub
		return query;
	}

	@Override
	public Object buildFileQuery(Object obj, TaskPriority taskPriority) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object buildFileRetrieve(Object obj, TaskPriority taskPriority) {
		Query query = new Query();
		query.setVersion(BuildConstants.XML_VERSION_6+"");
		query.setMsgID(DataUtils.getMsgID(query) + "");
		query.setType(BuildConstants.MSG_DOWN_TYPE);
		query.setDateTime(DataUtils.getCurrentTime());
		query.setSrcCode(ConfigVariable.MSG_SRC_CODE);
		query.setPriority(taskPriority.getCollectPriority()+"");

		FileRetrieve file = new FileRetrieve();
		long monitorID = 0;
		if (obj instanceof Map) {
			Map<String, String> map = (HashMap<String, String>)obj;
			String request = map.get(NetEventProtocol.SYNT_REQUEST);
			DownLoadFileDTO dto = null;
			try {
				dto = (DownLoadFileDTO)ConverterUtil.xmlToObj(request);
				List<TaskRecordDataDTO> taskList = dto.getTaskRecordList();
				for(TaskRecordDataDTO taskDTO: taskList) {
					monitorID = Long.parseLong(taskDTO.getMonitorId());
					FileRetrieve_File retFile = new FileRetrieve_File();
					retFile.setName(taskDTO.getFilename());
					file.addFileRetrieve_File(retFile);
				}
			} catch (Exception e) {
				Log.error("", e);
				return null;
			}
		}

		MonitorDevice info = null;
		info = DataMgrCentreModel.getInstance().getMonitorDevice(monitorID);
		if (info == null) {
			Log.warn("[实时音频任务构建]为指定的站点信息 monitorId=" + monitorID);
		} else {
			query.setDstCode(info.getMonitor_code());
		}
		query.setQuery(file);
		return query;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object buildProgramCommand(Object obj, TaskPriority taskPriority) {
		String monitorId = null;
		String ptype = null;
		try {
			if (obj instanceof Map) {
				MoniControlDTO dto=null;
				Map<String, String> map = (HashMap<String, String>)obj;
				String request = map.get(NetEventProtocol.SYNT_REQUEST);
				dto = (MoniControlDTO)ConverterUtil.xmlToObj(request);
				monitorId = dto.getMonitorId();
				ptype = dto.getFlg();
			}
			if(obj instanceof MoniInspectResultDTO){
				MoniInspectResultDTO monitorDto = (MoniInspectResultDTO)obj;
				monitorId = monitorDto.getMonitorId();
				ptype = "5";//类型5为巡检类型
			}
			return ProgramCommandTaskBuilder.buildV6ProgramCommand(monitorId, ptype);
		} catch (Exception e) {
			Log.error("", e);
			return null;
		}
	}


	@Override
	public Object buildStreamRealtimeClientQuery(Object obj, TaskPriority taskPriority) {
		//无此接口
		return null;
	}

	@Override
	public Object buildStreamRealtimeClientStop(Object obj, TaskPriority taskPriority) {
		//无此接口
		return null;
	}

	public static Map<String, String> decode(String task){
		Map<String, String> attrs = null;
		try {
			attrs = NPJsonUtil.jsonToMap(task);
		} catch (NMFormateException e) {
		}
		return attrs;
	}
}
