package com.neusoft.gbw.cp.process.measure.channel.manual;

import com.neusoft.gbw.cp.core.collect.CollectData;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.process.measure.channel.ITaskProcess;
import com.neusoft.gbw.cp.process.measure.channel.IdentityTypeExecutor;
import com.neusoft.gbw.cp.process.measure.exception.MeasureDisposeException;
import com.neusoft.gbw.cp.process.measure.vo.ManualType;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class ManualTaskDisposeHandler extends NMService {
	
	private ManualTaskChannel channel = null;
	
	public ManualTaskDisposeHandler(ManualTaskChannel channel) {
		this.channel = channel;
	}

	@Override
	public void run() {
		CollectData data = null;
		ITaskProcess executor = null;
//		List<StoreInfo> infoList = new ArrayList<StoreInfo>();
		while(isThreadRunning()) {
			try {
				data = (CollectData)channel.take();
			} catch (InterruptedException e) {
				Log.debug(this.getClass().getName()+"队列存储报错",e);
				break;
			}
			CollectTask task = data.getCollectTask();
			ManualType type = getManualType(task);
			executor = channel.getManualDispose(type);
			if (executor == null) {
				Log.warn("任务处理通道没有指定名称的处理类。Name=" + type);
				continue;
			}
			
			int version = data.getCollectTask().getData().getProVersion();
			if (version == 0) {
				Log.warn("任务处理通道数据没有对应的版本类型，可能是上报数据");
				continue;
			}
			
			try {
				switch(version) {
				case 8:
					executor.disposeV8(data);
					break;
				case 7:
					executor.disposeV7(data);
					break;
				case 6:
					executor.disposeV6(data);
					break;
				case 5:
					executor.disposeV5(data);
					break;
				}
			} catch (MeasureDisposeException e) {
				Log.error("", e);
			}
			
//			switch(type) {
//			case set_status:
//				//更新下发状态
//				infoList = (List<StoreInfo>)executor.dispose(data);
//				if(infoList == null || infoList.isEmpty()) {
//					Log.warn("设置任务数据转换存储对象为空，处理服务名称=" + type.name());
//				}
//				sendStore(infoList);
//				break;
//			default:
//				//存储历史数据操作
//				infoList = (List<StoreInfo>) executor.dispose(data);
//				if(infoList == null || infoList.isEmpty()) {
//					Log.warn("回收任务数据转换存储对象为空，处理服务名称=" + type.name());
//				}
//				
//				//创建一个修改状态的executor
//				executor = channel.getManualDispose(ManualType.set_recover_status);
//			    StoreInfo info = (StoreInfo) executor.dispose(data);
//				if(info != null) {
//					infoList.add(info);
//					
//				sendStore(infoList);
//				break;
//			}
			
//			infoList.addAll((List<StoreInfo>)executor.dispose(data));
//			if(infoList == null || infoList.isEmpty()) {
//				Log.warn("没有待处理业务数据， 处理服务名称=" + type.name());
//				continue;
//			}
			
		}
	}
	
	private ManualType getManualType(CollectTask task) {
		return IdentityTypeExecutor.identityManualType(task);
	}
	
	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String arg0) {
		this.serviceName = arg0;
	}
}
