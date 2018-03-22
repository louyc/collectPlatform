package com.neusoft.gbw.cp.build;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neusoft.gbw.cp.build.application.TaskBuilderCentre;
import com.neusoft.gbw.cp.build.domain.mode.BuildContext;
import com.neusoft.gbw.cp.build.interfaces.event.handler.AutoRecoverTaskHandler;
import com.neusoft.gbw.cp.build.interfaces.event.handler.BuildDelSetTaskHandler;
import com.neusoft.gbw.cp.build.interfaces.event.handler.ControlMonitorHandler;
import com.neusoft.gbw.cp.build.interfaces.event.handler.DeleteTaskHandler;
import com.neusoft.gbw.cp.build.interfaces.event.handler.DropClientTaskHandler;
import com.neusoft.gbw.cp.build.interfaces.event.handler.EquAlarmSetTaskHandler;
import com.neusoft.gbw.cp.build.interfaces.event.handler.EquInitSetTaskHandler;
import com.neusoft.gbw.cp.build.interfaces.event.handler.JMSTaskHandler;
import com.neusoft.gbw.cp.build.interfaces.event.handler.MonitorTransferHandler;
import com.neusoft.gbw.cp.build.interfaces.event.handler.QualityAlarmSetTaskHandler;
import com.neusoft.gbw.cp.build.interfaces.event.handler.RecoverTaskHandler;
import com.neusoft.gbw.cp.build.interfaces.event.handler.RequestMsgHandler;
import com.neusoft.gbw.cp.build.interfaces.event.handler.SetTaskHandler;
import com.neusoft.gbw.cp.build.interfaces.event.handler.SyntDBHandler;
import com.neusoft.gbw.cp.build.interfaces.event.handler.SyntStreamOverHandler;
import com.neusoft.gbw.cp.build.interfaces.event.handler.ThreadListenerHandler;
import com.neusoft.gbw.cp.build.interfaces.event.handler.UpdateTaskHandler;
import com.neusoft.gbw.cp.build.interfaces.event.handler.UploadStreamFileHandler;
import com.neusoft.gbw.cp.core.service.IBuildDataService;
import com.neusoft.np.arsf.base.bundle.BaseActivator;
import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.service.db.NPDataSourceFactory;
import com.neusoft.np.arsf.service.event.NMSSubject;
import com.neusoft.np.arsf.service.transfer.NPTransferService;

public class Activator extends BaseActivator {
	
	private TaskBuilderCentre buildMgr = null;
	
	@Override
	public Collection<BaseEventHandler> getEventHandler() {
		List<BaseEventHandler> list = new ArrayList<BaseEventHandler>();
		list.add(new RequestMsgHandler());
		list.add(new UpdateTaskHandler(buildMgr.getMgr())); //rest  修改任务
		list.add(new DeleteTaskHandler(buildMgr.getMgr())); // 删除任务
		list.add(new SetTaskHandler(buildMgr.getMgr()));    //创建任务
		list.add(new RecoverTaskHandler(buildMgr.getMgr()));//回收任务
		list.add(new JMSTaskHandler(buildMgr.getMgr()));    //jms接收信息
		list.add(new DropClientTaskHandler(buildMgr.getMgr())); //rest 删除客户端任务
		list.add(new SyntStreamOverHandler());                    
		list.add(new ControlMonitorHandler(buildMgr.getMgr())); // 同步站点信息
		list.add(new EquAlarmSetTaskHandler(buildMgr.getMgr()));// 保存站点DEv信息
		list.add(new EquInitSetTaskHandler(buildMgr.getMgr())); // 保存站点base信息
		list.add(new QualityAlarmSetTaskHandler(buildMgr.getMgr()));//保存站点kpi信息
		list.add(new MonitorTransferHandler(buildMgr.getMgr())); //站点程序存活状态校验 filter
		list.add(new SyntDBHandler());   //数据库信息更新
		list.add(new BuildDelSetTaskHandler());   //三满任务删除   20160808  lyc
		list.add(new ThreadListenerHandler());
		list.addAll(BuildContext.getInstance().getEventList());
		list.add(new UploadStreamFileHandler(buildMgr.getMgr()));
		list.add(new AutoRecoverTaskHandler(buildMgr.getMgr()));
		return list;
	}
	
	@Override
	public void init() {
		bindCoreServices();
		bindService(NMSSubject.class);
		bindService(NPDataSourceFactory.class);
		bindService(NPTransferService.class);
		bindService(IBuildDataService.class);
		
		//服务启动
		if (buildMgr == null) {
			buildMgr = new TaskBuilderCentre();
			buildMgr.start();
		}
	}

	@Override
	public void stop() {
		unbindService(NPDataSourceFactory.class);
		unbindService(NMSSubject.class);
		unbindCoreServices();
		if (buildMgr != null)
			buildMgr.stop();
	}
	
}
