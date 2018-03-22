package com.neusoft.gbw.cp.core.store;

public class EquAlarm {
	//报警ID
	private int alarmId;
	// 站点ID
	private long monitorId;
	//事件ID
	private int eventId;
	// 区域ID :对应直属台ID
	private int centerId;
	//指标组ID或业务告警的规则ID
	private int kgId;
	//指标组参数，具体标识
	private String kpiItem;
	//告警类型： 1、实时告警；2、业务告警
	private int alarmKind;
	// 告警级别ID
	private int alarmLevelId;
	/*
	 * 告警类型ID 1：供电异常报警 2：接收机异常报警 3：调制度卡报警 4：调幅度卡报警 5：语音压缩卡报警 6：频偏卡异常报警
	 * 7：电视接收机异常报警 8：视频压缩卡异常报警
	 */
	private int alarmTypeId;
	//告警内容或业务告警的详细信息
	private String alarmContent;
	//告警标题
	private String alarmTitle;
	// 告警时间
	private String alarmBeginTime;
	//告警恢复时间
	private String alarmCause;
	//实时告警时表示：告警原因；业务告警时表示：事件来源
	private String alarmAdditionalText;
	//状态 0：原发 1：确认 2：确认取消
	private int alarmState;
	// 处理时间
	private String terminationTime;
	// 处理人
	private String terminationOper;
	// 处理意见
	private String terminationOpinion;
	//报警恢复时间
	private String alarmEndTime;
	//工单ID
	private int orderId;
	//清除时间(手动恢复)
	private String renewTime;
	//清除人(手动恢复)
	private String renewOper;
	//清除意见(手动恢复)
	private String renewOpinion;
	//告警恢复时间与告警开始时间差
	private String alarmPeriodTime;
	//操作类型(0：自动 1：手动)
	private int operateType;
	/*===========================
					工单（gbal_order_t）
	===========================*/
	//故障原因及处理过程描述
	private String remark;
	//处理状态
	private String status;
	//待处理时间
	private String handleBeginDatetime;
	//已处理时间
	private String handleEndDatetime;
	//工单创建时间
	private String createDatetime;
	//创建状态
	private String createStatus;
	/*===========================
			        			其他
	   ===========================*/
	//站点名称
	private String monitorName;
	
	public String getMonitorName() {
		return monitorName;
	}
	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}
	public String getKpiItem() {
		return kpiItem;
	}
	public void setKpiItem(String kpiItem) {
		this.kpiItem = kpiItem;
	}

	public String getAlarmContent() {
		return alarmContent;
	}
	public void setAlarmContent(String alarmContent) {
		this.alarmContent = alarmContent;
	}
	public String getAlarmTitle() {
		return alarmTitle;
	}
	public void setAlarmTitle(String alarmTitle) {
		this.alarmTitle = alarmTitle;
	}
	public String getAlarmBeginTime() {
		return alarmBeginTime;
	}
	public void setAlarmBeginTime(String alarmBeginTime) {
		this.alarmBeginTime = alarmBeginTime;
	}
	public String getAlarmCause() {
		return alarmCause;
	}
	public void setAlarmCause(String alarmCause) {
		this.alarmCause = alarmCause;
	}
	public String getAlarmAdditionalText() {
		return alarmAdditionalText;
	}
	public void setAlarmAdditionalText(String alarmAdditionalText) {
		this.alarmAdditionalText = alarmAdditionalText;
	}
	public String getTerminationTime() {
		return terminationTime;
	}
	public void setTerminationTime(String terminationTime) {
		this.terminationTime = terminationTime;
	}
	public String getTerminationOper() {
		return terminationOper;
	}
	public void setTerminationOper(String terminationOper) {
		this.terminationOper = terminationOper;
	}
	public String getTerminationOpinion() {
		return terminationOpinion;
	}
	public void setTerminationOpinion(String terminationOpinion) {
		this.terminationOpinion = terminationOpinion;
	}
	public String getAlarmEndTime() {
		return alarmEndTime;
	}
	public void setAlarmEndTime(String alarmEndTime) {
		this.alarmEndTime = alarmEndTime;
	}
	public String getRenewTime() {
		return renewTime;
	}
	public void setRenewTime(String renewTime) {
		this.renewTime = renewTime;
	}
	public String getRenewOper() {
		return renewOper;
	}
	public void setRenewOper(String renewOper) {
		this.renewOper = renewOper;
	}
	public String getRenewOpinion() {
		return renewOpinion;
	}
	public void setRenewOpinion(String renewOpinion) {
		this.renewOpinion = renewOpinion;
	}
	public String getAlarmPeriodTime() {
		return alarmPeriodTime;
	}
	public void setAlarmPeriodTime(String alarmPeriodTime) {
		this.alarmPeriodTime = alarmPeriodTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getHandleBeginDatetime() {
		return handleBeginDatetime;
	}
	public void setHandleBeginDatetime(String handleBeginDatetime) {
		this.handleBeginDatetime = handleBeginDatetime;
	}
	public String getHandleEndDatetime() {
		return handleEndDatetime;
	}
	public void setHandleEndDatetime(String handleEndDatetime) {
		this.handleEndDatetime = handleEndDatetime;
	}
	public String getCreateDatetime() {
		return createDatetime;
	}
	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}
	public String getCreateStatus() {
		return createStatus;
	}
	public void setCreateStatus(String createStatus) {
		this.createStatus = createStatus;
	}
	public int getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(int alarmId) {
		this.alarmId = alarmId;
	}
	public long getMonitorId() {
		return monitorId;
	}
	public void setMonitorId(long monitorId) {
		this.monitorId = monitorId;
	}
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public int getCenterId() {
		return centerId;
	}
	public void setCenterId(int centerId) {
		this.centerId = centerId;
	}
	public int getKgId() {
		return kgId;
	}
	public void setKgId(int kgId) {
		this.kgId = kgId;
	}
	public int getAlarmKind() {
		return alarmKind;
	}
	public void setAlarmKind(int alarmKind) {
		this.alarmKind = alarmKind;
	}
	public int getAlarmLevelId() {
		return alarmLevelId;
	}
	public void setAlarmLevelId(int alarmLevelId) {
		this.alarmLevelId = alarmLevelId;
	}
	public int getAlarmTypeId() {
		return alarmTypeId;
	}
	public void setAlarmTypeId(int alarmTypeId) {
		this.alarmTypeId = alarmTypeId;
	}
	public int getAlarmState() {
		return alarmState;
	}
	public void setAlarmState(int alarmState) {
		this.alarmState = alarmState;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getOperateType() {
		return operateType;
	}
	public void setOperateType(int operateType) {
		this.operateType = operateType;
	}
}
