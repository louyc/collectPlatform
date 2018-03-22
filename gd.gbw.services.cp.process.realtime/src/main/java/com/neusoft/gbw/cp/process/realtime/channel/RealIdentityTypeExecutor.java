package com.neusoft.gbw.cp.process.realtime.channel;

import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.protocol.ProtocolData;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.process.realtime.vo.RealtimeType;
import com.neusoft.np.arsf.base.bundle.Log;

public class RealIdentityTypeExecutor {

	public static RealtimeType identityRealtimeType(CollectTask task) {
		RealtimeType type = null;
		
		ProtocolData data = task.getData();
		if(data == null){ 
			Log.warn("[实时处理服务]未找到实时任务中的协议数据，protocolData=" + data);
			return type;
		}
		ProtocolType proType = task.getData().getType();
		switch(proType) {
		case EquAlarmParamSet:
			type = RealtimeType.param_init_set;   //设备初始化设置
			break;
		case EquInitParamSet:
			type = RealtimeType.param_init_set;  //设备初始化设置
			break;
		case EquStatusRealtimeQuery:    
			type = RealtimeType.equ_status;   //设备状态
			break;
		case EquLogHistoryQuery:    
			type = RealtimeType.manual_recover;   //设备历史日志查询  20161013  lyc
			break;
		case FileRetrieve:                   //文件回收
			type = RealtimeType.upload_stream;
			break;
		case ProgramCommand:
			type = RealtimeType.program_command;   //版本查询
			break;
		case QualityRealtimeQuery:
			type = RealtimeType.quality_realtime;    //实时指标
			break;
		case SpectrumRealtimeQuery:
			type = RealtimeType.spectrum_realtime;    //实时频谱
			break;
		case StreamRealtimeClientQuery:
			type = RealtimeType.client_stream;     //客户端连接
			break;
		case StreamRealtimeClientStop:
			type = RealtimeType.client_stop;    //客户端连接断开
			break;
		case StreamRealtimeQuery:
			type = RealtimeType.real_stream;
			break;
		case QualityAlarmParamSet:
			type = RealtimeType.param_init_set;   //设备初始化设置
			break;
		case TaskDelete:
			type = RealtimeType.manual_task;     //手动任务
			break;
		case QualityTaskSet:
			type = RealtimeType.manual_task;     //手动任务
			break;
		case OffsetTaskSet:
			type = RealtimeType.manual_task;
			break;
		case StreamTaskSet:
			type = RealtimeType.manual_task;
			break;
		case SpectrumTaskSet:
			type = RealtimeType.manual_task;
			break;
		case QualityAlarmHistoryQuery:
			type = RealtimeType.manual_recover;    //手动回收
			break;
		case SpectrumHistoryQuery:
			type = RealtimeType.manual_recover;
			break;
		case OffsetHistoryQuery:
			type = RealtimeType.manual_recover;
			break;
		case StreamHistoryQuery:
			type = RealtimeType.manual_recover;
			break;
		case QualityHistoryQuery:
			type = RealtimeType.manual_recover;
			break;
		default:
			break;
		}
		return type;
	}
}
