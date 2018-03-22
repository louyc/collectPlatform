package com.neusoft.gbw.cp.process.measure.channel;

import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.process.measure.vo.ManualType;
import com.neusoft.np.arsf.base.bundle.Log;

public class IdentityTypeExecutor {
	
	public static ManualType identityManualType(CollectTask task) {
		ManualType type = null;
		ProtocolType proType = task.getData().getType();
		Log.debug("实时类任务 协议类型接口名：："+proType);
		switch(proType) {
		case EquStatusRealtimeQuery:
			break;
		case EquLogHistoryQuery:  //20161013 lyc 设备历史日志
			type = ManualType.equ_history;
			break;
		case OffsetHistoryQuery:
			type = ManualType.offset_history;
			break;
		case OffsetTaskSet:
			type = ManualType.set_status;
			break;
		case QualityHistoryQuery:
			type = ManualType.quality_history;
			break;
		case QualityTaskSet:
			type = ManualType.set_status;
			break;
		case SpectrumHistoryQuery:
			type = ManualType.spectrum_history;
			break;
		case SpectrumTaskSet:
			type = ManualType.set_status;
			break;
		case StreamHistoryQuery:
			type = ManualType.stream_history;
			break;
		case StreamTaskSet:
			type = ManualType.set_status;
			break;
		case FileRetrieve:
			type = ManualType.upload_stream;
			break;
		default:
			break;
		
		}
		
		return type;
	}
}
