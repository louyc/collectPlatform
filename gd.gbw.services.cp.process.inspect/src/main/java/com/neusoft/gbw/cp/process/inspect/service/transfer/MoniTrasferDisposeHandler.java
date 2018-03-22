package com.neusoft.gbw.cp.process.inspect.service.transfer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.neusoft.gbw.cp.core.EventServiceTopic;
import com.neusoft.gbw.cp.core.site.MonitorDevice;
import com.neusoft.gbw.cp.core.store.EquAlarm;
import com.neusoft.gbw.cp.process.inspect.context.InspectTaskContext;
import com.neusoft.gbw.cp.process.inspect.util.InspectUtils;
import com.neusoft.gbw.cp.process.inspect.vo.InspectMonStat;
import com.neusoft.gbw.domain.monitor.intf.dto.MonitorDTO;
import com.neusoft.np.arsf.base.bundle.ARSFToolkit;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMService;
/**
 * 站点连通性处理
 * @author yanghao
 *
 */
public class MoniTrasferDisposeHandler extends NMService{
	
	private TransferInspectService handler = null;

	public MoniTrasferDisposeHandler(TransferInspectService handler) {
		this.handler = handler;
	}
	
	@Override
	public void run() {
		MonitorDevice device = null;
		int monitorStatus = 1;
		MonitorDTO mDto = null;
		InspectMonStat stat = null;
		while(isThreadRunning()) {
			try {
				device = handler.getData();
				//进行连通性测试，如果测试成功，向构建发送软件巡检消息，
				String monitorIp = device.getDevice_ip();
				//测试连通性
				boolean isConn = InspectUtils.isConnect(monitorIp);;
				
				if(!isConn) {
					//连通性异常，存储异常信息，向前台发送连通性消息
					monitorStatus = 3;
					//发送前台
					mDto = createDto(device, monitorStatus);
					InspectUtils.sendWeb(mDto);
					stat = createMonStat(device.getMonitor_id(), monitorStatus);
					InspectUtils.sendStore(stat);
					//发送站点状态更新消息（lyc）
					sendMonitorStatus(device.getMonitor_id(), monitorStatus);
				}else {
					//连通性正常，存储连通性信息，并发送至前台
					monitorStatus = 1;
					//发送前台
					mDto = createDto(device, monitorStatus);
					stat = createMonStat(device.getMonitor_id(), monitorStatus);
					InspectUtils.sendMonitStore(stat);
					
					//通知构建服务，发送版本查询信息观察软件是否存活  50 
                    //20170509   站点版本查询    注掉					
//					Date nowDate = new Date();
//					if(new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(nowDate).toString().split(":")[3].equals("50")){
//						sendCheckSoftStatus(device.getMonitor_id());
//					}
				}
				//发送告警处理服务，进行告警处理
				sendAlarmService(converAlarm(device.getMonitor_id(), monitorStatus));
			} catch (InterruptedException e) {
				Log.error(this.getClass().getName()+"队列存储报错", e);
				break;
			}finally {
				mDto = null;
				stat = null;
				device = null;
			}
		}
	}
	public static void main(String[] args) {
		Date nowDate = new Date();
		System.out.println(new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(nowDate).toString().split(":")[3]);
	}
//	private void sendWeb() {
//		EquAlarmOderDTO dto = new EquAlarmOderDTO();
//		JMSDTO jms = new JMSDTO();
//		jms.setTypeId(41);
//		jms.setObj(dto);
//		ARSFToolkit.sendEvent("JMS_SEND_MSG_TOPIC", jms);
//		Log.debug("发送告警信息至前台，topic=" + EventServiceTopic.JMS_SEND_MSG_TOPIC + ",jms=" + jms.toString());
//		
//	}
	
	public static void sendAlarmService(EquAlarm alarm ) {
		ARSFToolkit.sendEvent(EventServiceTopic.MONITOR_ALARM_DISPOSE_TOPIC, alarm);
	}
	/**
	 * 效果类 任务 多站点 采集 
	 * @param alarm
	 */
	public static void sendMonitorStatus(long monitorId, int status) {
		Log.debug("站点巡检 更新站点 在线状态  (效果类任务)"+status);
		HashMap<Long,Integer> info = new HashMap<Long,Integer>();
		info.put(monitorId, status);
		ARSFToolkit.sendEvent(EventServiceTopic.MONITOR_STATUS_NOTIFY_TOPIC, info);
	}
	
	private EquAlarm converAlarm(long monitorId, int status) {
		EquAlarm alarm = new EquAlarm();
		alarm.setEventId(1);
		alarm.setMonitorId(monitorId);
		alarm.setCenterId(-1); //以后提取直属台ID
		alarm.setKgId(-1);
		alarm.setKpiItem("");
		alarm.setAlarmKind(1); //告警类型
		alarm.setAlarmLevelId(-1);
		int alarmTypeId = InspectUtils.converAlarmType(status);
		alarm.setAlarmTypeId(alarmTypeId);
		alarm.setAlarmContent(getAlarm(alarmTypeId));	//告警描述
		alarm.setAlarmTitle(getAlarm(alarmTypeId));      //告警描述
		alarm.setAlarmState(status == 1 ? 1:0);      //告警状态0：原发、1：恢复、2：确认（删除）
		alarm.setAlarmBeginTime(status == 3 ? getCurrentTime() : "");  //告警时间
		alarm.setAlarmEndTime(status == 1 ? getCurrentTime() : "");    //告警恢复时间
		alarm.setAlarmPeriodTime(""); //告警历时
		alarm.setAlarmCause("2");     //alarm域 故障原因,1、外电停，2、通讯故障，2、设备故障
		alarm.setAlarmAdditionalText(""); //告警的附赠信息，比如电平
		alarm.setOperateType(0);	//操作类型(0：自动 1：手动)
		return alarm;
	}
	
	public String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}
	
	private String getAlarm(int type) {
		return  InspectTaskContext.getInstance().getAlarmType().get(type);
	}
	
	private InspectMonStat createMonStat(long monitorId, int monitorStatus) {
		InspectMonStat stat = new InspectMonStat();
		stat.setMonitor_id(monitorId);
		stat.setOnline_state(monitorStatus);
		return stat;
	}
	
	private MonitorDTO createDto(MonitorDevice device, int monitorStatus) {
		MonitorDTO dto = new MonitorDTO();
		dto.setOnlineState(monitorStatus + "");
		dto.setMonitorCode(device.getMonitor_code());
		dto.setMonitorId(device.getMonitor_id() + "");
		return dto;
	}
	
	private void sendCheckSoftStatus(long monitor_id) {
		ARSFToolkit.sendEvent(EventServiceTopic.MONITOR_SOFT_ONLINE_STATUS_TOPIC, monitor_id);
	}
	
//	private boolean isEquConnect(String monitorIp) {
//		return InspectUtils.isConnect(monitorIp);
//	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}
