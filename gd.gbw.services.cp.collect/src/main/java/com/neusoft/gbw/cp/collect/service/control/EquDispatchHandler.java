package com.neusoft.gbw.cp.collect.service.control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.gbw.cp.collect.model.CollectTaskContext;
import com.neusoft.gbw.cp.collect.vo.SiteConfig;
import com.neusoft.gbw.cp.conver.vo.Query;
import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.business.BusinessTaskType;
import com.neusoft.gbw.cp.core.collect.CollectTask;
import com.neusoft.gbw.cp.core.collect.CollectTaskType;
import com.neusoft.gbw.cp.core.collect.ServletTransferAttr;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;

public class EquDispatchHandler extends NMService {

	private StationControlMgr stationMgr = null;

	public EquDispatchHandler(StationControlMgr stationMgr) {
		this.stationMgr = stationMgr;
	}

	@Override
	public void run() {
		CollectTask task = null;
		while(isThreadRunning()) {
			try {
				task = stationMgr.take();
			} catch (InterruptedException e) {
				Log.error("", e);
				break;
			}
			List<SiteConfig> moList =new ArrayList<SiteConfig>();
			String monitorEquCode = null;
			try {
				if(task.getBusTask().getType().equals(BusinessTaskType.measure_auto)){
					if(task.getAttrInfo().getColTaskType()==CollectTaskType.occup){
						List<Long> idList = task.getBusTask().getMonitor_id_list();
						if(idList==null){
							monitorEquCode = task.getAttrInfo().getFirstEquCode();
						}else{
							/**
							 * 多机选择采集
							 */
							moList= getUseableEquCode(idList);  
							long time=0l;
							String equCode = null;
							long miniTime = System.currentTimeMillis();
							SiteConfig theSiteConfig = null;
							if(moList.size()==1){ //只有一个站点时
								equCode = moList.get(0).getMoDevice().getFirstEnCode();
								if(equCode == null) {
									Log.warn("[采集服务]此站点未发现接收机编码，MonitorID=" + stationMgr.getMonitorID() + ", Code=" + equCode);
									continue;
								}
								if (!containEqu(equCode)) {
									Log.warn("[采集服务]此站点不存在接收机编码，MonitorID=" + stationMgr.getMonitorID() + ", Code=" + equCode);
									continue;
								}
								theSiteConfig = moList.get(0);
								monitorEquCode = moList.get(0).getMoDevice().getFirstEnCode();
							}else{ //包含2个以上站点
								for(SiteConfig sc: moList){
									equCode = sc.getMoDevice().getFirstEnCode();
									time = sc.getTimeStamp();
									if(equCode == null) {
										Log.warn("[采集服务]此站点未发现接收机编码，MonitorID=" + stationMgr.getMonitorID() + ", Code=" + equCode);
										continue;
									}
									if (!containEqu(equCode)) {
										Log.warn("[采集服务]此站点不存在接收机编码，MonitorID=" + stationMgr.getMonitorID() + ", Code=" + equCode);
										continue;
									}
									/*
									 * 任务优先级 站点在线状态  > 站点软件未开启状态（V8 协议以下   此站点状态不准确）
									 * 4：在线      2：软件未开启
									 */
	//								if(sc.getMoDevice().getOnline_state()==4){
	//									miniTime = time;
	//									monitorEquCode = equCode;
	//									theSiteConfig = sc;
	//									break;
	//								}
									//根据最后执行任务的时间戳，选择执行任务最早的站点进行本次工作
									if (time <= miniTime) {
										miniTime = time;
										monitorEquCode = equCode;
										theSiteConfig = sc;
									}
								}
							}
							if(monitorEquCode == null) {
								updateStatus(task);
								continue;
							}
							//设置最后执行任务的时间戳，用于在多个接收机中进行选择
							if (theSiteConfig != null) {
								theSiteConfig.setTimeStamp(System.currentTimeMillis());
							}
							task.getBusTask().setMonitor_id(Long.valueOf(theSiteConfig.getMoDevice().getMonitor_id()));
							task.getBusTask().setMonitor_code(theSiteConfig.getMoDevice().getMonitor_code());
							task.getBusTask().setManufacturer_id(theSiteConfig.getMoDevice().getManufacturer_id());
							task.getBusTask().setAlias_name(theSiteConfig.getMoDevice().getAlias_name());
							task.getBusTask().setAlias_code(theSiteConfig.getMoDevice().getAlias_code());
							task.getAttrInfo().setFirstEquCode(monitorEquCode);
							//转换任务中 选择出的站点的url地址
							ServletTransferAttr attr = (ServletTransferAttr)(task.getAttrInfo().getTransferAttr());
							SiteConfig conRight = CollectTaskContext.getModel().getSiteConfig(String.valueOf(theSiteConfig.getMoDevice().getMonitor_id()));
							attr.setUrl(conRight.getMoDevice().getUrl());
							task.getAttrInfo().setTransferAttr(attr);
							//转换任务中  协议信息中站点编码
							Query q = (Query)task.getData().getQuery();
							q.setDstCode(theSiteConfig.getMoDevice().getMonitor_code());
							task.getData().setQuery(q);
						}
					}else{
						monitorEquCode = task.getAttrInfo().getFirstEquCode();
					}
					Log.debug("下发的效果任务id"+task.getBusTask().getTask_id()+"   "+task.getBusTask().getMonitor_code()+
							"   "+task.getBusTask().getFreq()+"   "+task.getBusTask().getTask_type_id()
							+"    "+task.getAttrInfo().getColTaskType()+"   "+task.getBusTask().getMonitor_id()+" "
							+task.getBusTask().getOrgMonitorId()+" "+task.getBusTask().getMeasure_status());
				}else{
					monitorEquCode = task.getAttrInfo().getFirstEquCode();
					if(monitorEquCode == null) {
						Log.warn("[采集服务]此站点未发现接收机编码，MonitorID=" + monitorEquCode + ", Code=" + monitorEquCode);
						continue;
					}
					if (!containEqu(monitorEquCode)) {
						Log.warn("[采集服务]此站点不存在接收机编码，MonitorID=" + monitorEquCode+ ", Code=" + monitorEquCode);
						continue;
					}

				}

				if (!stationMgr.getEquStatusControl().containEquCode(monitorEquCode+task.getBusTask().getMonitor_id())) {
					stationMgr.startEquCollect(monitorEquCode+task.getBusTask().getMonitor_id(), task);//创建接收机控制管道
					Log.info("[采集服务]启动设备指定接收机采集功能。MonitorID=" + monitorEquCode + ", Code=" + monitorEquCode
							+"   "+task.getBusTask().getTask_id()+"   "+task.getBusTask().getMonitor_code()+
							"   "+task.getBusTask().getFreq()+"   "+task.getBusTask().getTask_type_id()
							+"   "+monitorEquCode+task.getBusTask().getMonitor_id());
					continue;
				}
				EquCollectCotrolMgr equCollectMgr = stationMgr.getEquStatusControl().getEquCollectCotrolMgr(monitorEquCode+
						task.getBusTask().getMonitor_id());
				stationMgr.putTask(equCollectMgr, task);
			}catch(Exception e) {
				Log.error("[采集服务]站点分发队列处理数据异常", e);
			}

		}
	}

	/**
	 * 下发任务前 站点或接收机出现故障  更新收测单元失败  lyc
	 * @param task
	 */
	public void updateStatus(CollectTask task){
		//更新过滤掉的采集任务状态
		if(task.getBusTask().getType().equals(BusinessTaskType.measure_auto)){
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("task_id",task.getBusTask().getTask_id());
			map.put("monitor_alias_code",task.getBusTask().getAlias_code());
			map.put("runplan_id",task.getBusTask().getRunplan_id());
			map.put("runplan_type_id",task.getBusTask().getTask_type_id());
			map.put("unit_collect_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			map.put("unit_status_id","-1");
			map.put("uncollect_reason","接收机故障或站点软件未开启");
			map.put("unit_begin",task.getBusTask().getUnit_begin());
			map.put("unit_end",task.getBusTask().getUnit_end());
			ARSFToolkit.sendEvent(EventServiceTopic.STORE_DATA_TOPIC, map);
		}
	}
	/**
	 * 站点任务优先级  手动控制mealList  > 自动控制autList
	 * @param idList
	 * @return
	 */
	public List<SiteConfig> getUseableEquCode(List<Long> idList){
		List<SiteConfig> autlList=new ArrayList<SiteConfig>();
		List<SiteConfig> mealList=new ArrayList<SiteConfig>();
		for(Long id: idList){
			SiteConfig config = CollectTaskContext.getModel().getSiteConfig(String.valueOf(id));
			//20161227  在线 1、软件未开启 2、掉线 3、软件开启4
			if(config.getMoDevice().getOnline_state()==3){	//站点掉线
				continue;
			}
			//仅使用自动控制模式的站点  0 手动   1自动  
			//手动  并且站点状态在线

			if(config.getMoDevice().getTaskControlModel().equals("0")){
				mealList.add(config);
			}
			autlList.add(config);
		}
		if(null!=mealList && mealList.size()>0){
			return mealList;
		}
		return autlList;
	}

	private boolean containEqu(String equCode) {
		//验证此编码是否属于此站点
		List<String> list = stationMgr.getConfig().getEquCodeList();
		return list.contains(equCode);
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
