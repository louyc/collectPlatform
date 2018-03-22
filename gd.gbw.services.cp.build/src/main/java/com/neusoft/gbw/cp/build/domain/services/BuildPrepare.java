package com.neusoft.gbw.cp.build.domain.services;

import java.util.HashMap;
import java.util.Map;

import com.neusoft.gbw.cp.build.domain.build.IQueryBuild;
import com.neusoft.gbw.cp.build.domain.build.collect.AbstractTaskBuilder;
import com.neusoft.gbw.cp.build.domain.build.collect.ControlMonitorTaskBuilder;
import com.neusoft.gbw.cp.build.domain.build.collect.EquStatusHistoryQueryTaskBuilder;
import com.neusoft.gbw.cp.build.domain.build.collect.EquStatusRealtimeQueryTaskBuilder;
import com.neusoft.gbw.cp.build.domain.build.collect.ManualTaskBuilder;
import com.neusoft.gbw.cp.build.domain.build.collect.MeasureUnitTaskBuilder;
import com.neusoft.gbw.cp.build.domain.build.collect.MonitorInspectTaskBuilder;
import com.neusoft.gbw.cp.build.domain.build.collect.OnlineListenerTaskBuilder;
import com.neusoft.gbw.cp.build.domain.build.collect.ParamInitSetTaskBuilder;
import com.neusoft.gbw.cp.build.domain.build.collect.QualityRealtimeQueryTaskBuilder;
import com.neusoft.gbw.cp.build.domain.build.collect.SpectrumRealtimeQueryTaskBuilder;
import com.neusoft.gbw.cp.build.domain.build.collect.StreamRealtimeClientQueryTaskBuilder;
import com.neusoft.gbw.cp.build.domain.build.collect.StreamRealtimeClientStopTaskBuilder;
import com.neusoft.gbw.cp.build.domain.build.collect.StreamRealtimeQueryTaskBuilder;
import com.neusoft.gbw.cp.build.domain.build.collect.UploadStreamFileTaskBuilder;
import com.neusoft.gbw.cp.build.domain.build.query.QueryV5Builder;
import com.neusoft.gbw.cp.build.domain.build.query.QueryV6Builder;
import com.neusoft.gbw.cp.build.domain.build.query.QueryV7Builder;
import com.neusoft.gbw.cp.build.domain.build.query.QueryV8Builder;
import com.neusoft.gbw.cp.conver.ParameterValidationFactory;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.protocol.ProtocolType;
import com.neusoft.gbw.cp.load.data.build.domain.vo.BuildType;
import com.neusoft.np.arsf.base.bundle.Log;

public class BuildPrepare {

	public Map<String, AbstractTaskBuilder> prepareTaskBuider() {
		Map<String, AbstractTaskBuilder> taskBuildMap = new HashMap<String, AbstractTaskBuilder>();
		AbstractTaskBuilder builder = null;
		BuildType type = null;
		
		//手动任务--下发：质量、频谱、音频、频偏
		type = new BuildType(ProtocolType.QualityTaskSet, BusinessTaskType.measure_manual_set);
		builder = new ManualTaskBuilder();
		taskBuildMap.put(type.getKey(), builder);
		
		type = new BuildType(ProtocolType.SpectrumTaskSet, BusinessTaskType.measure_manual_set);
		taskBuildMap.put(type.getKey(), builder);
		
		type = new BuildType(ProtocolType.OffsetTaskSet, BusinessTaskType.measure_manual_set);
		taskBuildMap.put(type.getKey(), builder);
		
		type = new BuildType(ProtocolType.StreamTaskSet, BusinessTaskType.measure_manual_set);
		taskBuildMap.put(type.getKey(), builder);
		
		//手动任务--回收：质量、频谱、音频、频偏
		type = new BuildType(ProtocolType.QualityHistoryQuery, BusinessTaskType.measure_manual_recover);
		taskBuildMap.put(type.getKey(), builder);
		
		type = new BuildType(ProtocolType.SpectrumHistoryQuery, BusinessTaskType.measure_manual_recover);
		taskBuildMap.put(type.getKey(), builder);
		
		type = new BuildType(ProtocolType.OffsetHistoryQuery, BusinessTaskType.measure_manual_recover);
		taskBuildMap.put(type.getKey(), builder);
		
		type = new BuildType(ProtocolType.StreamHistoryQuery, BusinessTaskType.measure_manual_recover);
		taskBuildMap.put(type.getKey(), builder);
		
		//手动删除任务--
		type = new BuildType(ProtocolType.TaskDelete, BusinessTaskType.measure_manual_del);
		taskBuildMap.put(type.getKey(), builder);
		
		
		//实时播音
		type = new BuildType(ProtocolType.StreamRealtimeQuery, BusinessTaskType.measure_realtime);
		builder = new StreamRealtimeQueryTaskBuilder();
		taskBuildMap.put(type.getKey(), builder);
		
		//实时录音
		type = new BuildType(ProtocolType.StreamRealtimeQuery, BusinessTaskType.measure_real_record);
		taskBuildMap.put(type.getKey(), builder);
		
		//实时指标
		type = new BuildType(ProtocolType.QualityRealtimeQuery, BusinessTaskType.measure_realtime);
		builder = new QualityRealtimeQueryTaskBuilder();
		taskBuildMap.put(type.getKey(), builder);
		
		//实时频谱   20160902 lyc
		type = new BuildType(ProtocolType.SpectrumRealtimeQuery, BusinessTaskType.measure_realtime);
		builder = new SpectrumRealtimeQueryTaskBuilder();
		taskBuildMap.put(type.getKey(), builder);
		
		//收测单元（实时音频） 中央广播、实验
		type = new BuildType(ProtocolType.StreamRealtimeQuery, BusinessTaskType.measure_auto);
		builder = new MeasureUnitTaskBuilder();
		taskBuildMap.put(type.getKey(), builder);
		
		//在线监听（实时音频） 中央广播、实验
		type = new BuildType(ProtocolType.StreamRealtimeQuery, BusinessTaskType.measure_online);
		builder = new OnlineListenerTaskBuilder();
		taskBuildMap.put(type.getKey(), builder);
		
		//设备实时状态查询
		type = new BuildType(ProtocolType.EquStatusRealtimeQuery, BusinessTaskType.measure_realtime);
		builder = new EquStatusRealtimeQueryTaskBuilder();
		taskBuildMap.put(type.getKey(), builder);
		
		//设备客户端连接查询
		type = new BuildType(ProtocolType.StreamRealtimeClientQuery, BusinessTaskType.measure_realtime);
		builder = new StreamRealtimeClientQueryTaskBuilder();
		taskBuildMap.put(type.getKey(), builder);
		
		//中断客户端音频连接
		type = new BuildType(ProtocolType.StreamRealtimeClientStop, BusinessTaskType.measure_realtime);
		builder = new StreamRealtimeClientStopTaskBuilder();
		taskBuildMap.put(type.getKey(), builder);
		
		//版本查询
		type = new BuildType(ProtocolType.ProgramCommand, BusinessTaskType.measure_realtime);
		builder = new ControlMonitorTaskBuilder();
		taskBuildMap.put(type.getKey(), builder);

		//指标报警设置任务
		type = new BuildType(ProtocolType.QualityAlarmParamSet, BusinessTaskType.measure_realtime);
		builder = new ParamInitSetTaskBuilder();
		taskBuildMap.put(type.getKey(), builder);
		
		//设备报警设置任务
		type = new BuildType(ProtocolType.EquAlarmParamSet, BusinessTaskType.measure_realtime);
		taskBuildMap.put(type.getKey(), builder);
		
		//设备初始化参数设置任务
		type = new BuildType(ProtocolType.EquInitParamSet, BusinessTaskType.measure_realtime);
		taskBuildMap.put(type.getKey(), builder);
		
		//文件获取，即手动音频文件的下载
		type = new BuildType(ProtocolType.FileRetrieve, BusinessTaskType.measure_manual_recover);
		builder = new UploadStreamFileTaskBuilder();
		taskBuildMap.put(type.getKey(), builder);
		//任务巡检，设备状态，设备版本，设备任务状态
		type = new BuildType(ProtocolType.TaskStatusQuery, BusinessTaskType.measure_inspect);
		builder = new MonitorInspectTaskBuilder();
		taskBuildMap.put(type.getKey(), builder);
		
		type = new BuildType(ProtocolType.EquStatusRealtimeQuery, BusinessTaskType.measure_inspect);
		builder = new MonitorInspectTaskBuilder();
		taskBuildMap.put(type.getKey(), builder);
		
		type = new BuildType(ProtocolType.ProgramCommand, BusinessTaskType.measure_inspect);
		builder = new MonitorInspectTaskBuilder();
		taskBuildMap.put(type.getKey(), builder);
		
		//设备日志历史查询   20161010   lyc
		type = new BuildType(ProtocolType.EquLogHistoryQuery, BusinessTaskType.measure_manual_recover);
		builder = new EquStatusHistoryQueryTaskBuilder();
		taskBuildMap.put(type.getKey(), builder);
				
		Log.debug(this.getClass().getName()+"集合taskBuildMap 长度"+taskBuildMap.size());
		return taskBuildMap;
	}
	
	public Map<Integer, IQueryBuild> prepareQueryBuilder() {
		Map<Integer, IQueryBuild> queryBuildMap = new HashMap<Integer, IQueryBuild>();
		
		queryBuildMap.put(5, new QueryV5Builder());
		queryBuildMap.put(6, new QueryV6Builder());
		queryBuildMap.put(7, new QueryV7Builder());
		queryBuildMap.put(8, new QueryV8Builder());
		
		return queryBuildMap;
	}
	
	public Map<Integer, Object> prepareQueryVerifier() {
		Map<Integer, Object> queryVerifyMap = new HashMap<Integer, Object>();
		queryVerifyMap.put(5, null);
		queryVerifyMap.put(6, null);
		queryVerifyMap.put(7, null);
		queryVerifyMap.put(8, ParameterValidationFactory.getInstance().newParameterValidationService());
		return queryVerifyMap;
	}
}
