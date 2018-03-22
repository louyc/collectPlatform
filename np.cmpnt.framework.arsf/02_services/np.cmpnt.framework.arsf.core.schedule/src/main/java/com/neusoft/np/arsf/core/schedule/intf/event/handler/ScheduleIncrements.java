package com.neusoft.np.arsf.core.schedule.intf.event.handler;

import java.util.Map;

import com.neusoft.np.arsf.common.exception.NMException;
import com.neusoft.np.arsf.core.schedule.domain.ScheduleFacade;
import com.neusoft.np.arsf.core.schedule.infra.constants.ScheduleDeclare;
import com.neusoft.np.arsf.core.schedule.intf.event.XmlEventHandler;
import com.neusoft.np.arsf.core.schedule.quartz.ScheduleConstants;
import com.neusoft.np.arsf.core.schedule.quartz.TaskBuilder;
import com.neusoft.np.arsf.core.schedule.quartz.TaskInfo;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App OSGi Services<br>
 * 功能描述: 添加调度任务事件监听EventHandler<br>
 * 创建日期: 2013年11月8日 <br>
 * 版权信息: Copyright (c) 2013<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2013年11月8日       黄守凯        创建
 * </pre>
 */
public class ScheduleIncrements extends XmlEventHandler {

	@Override
	public String getTopicName() {
		return ScheduleDeclare.SCHEDULE_INCREMENTS;
	}

	@Override
	public void processEvent(Map<String, String> eventData) throws NMException {
		eventData.put(ScheduleConstants.TASKOPERATORTYPE, ScheduleConstants.TASKOPERATORADD);
		TaskInfo taskInfo = TaskBuilder.buildTaskInfo(eventData);
		ScheduleFacade.addSchedule(taskInfo);
	}

}
