package com.neusoft.np.arsf.core.schedule.intf.event.handler;

import java.util.Map;

import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.common.util.NMBeanUtils;
import com.neusoft.np.arsf.common.util.NMBeanUtilsException;
import com.neusoft.np.arsf.common.util.NMFormateException;
import com.neusoft.np.arsf.core.schedule.domain.ScheduleFacade;
import com.neusoft.np.arsf.core.schedule.infra.constants.ScheduleDeclare;
import com.neusoft.np.arsf.core.schedule.quartz.ScheduleConstants;
import com.neusoft.np.arsf.core.schedule.quartz.TaskBuilder;
import com.neusoft.np.arsf.core.schedule.quartz.TaskInfo;
import com.neusoft.np.arsf.core.schedule.vo.ScheduleMsg;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App OSGi Services<br>
 * 功能描述: 添加调度任务事件监听EventHandler<br>
 * 创建日期: 2015年05月19日 <br>
 * 版权信息: Copyright (c) 2013<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: jia.h@neusoft.com">贾浩</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2015年05月19日       贾浩        创建
 * </pre>
 */
public class ScheduleIncrementsObj implements BaseEventHandler {

	@Override
	public String getTopicName() {
		return ScheduleDeclare.SCHEDULE_INCREMENTS_OBJ;
	}

	/**
	 *	modify by jiahao 
	 * (non-Javadoc)
	 * @see com.neusoft.np.arsf.base.bundle.BaseEventHandler#processEvent(java.lang.Object)
	 */
	@Override
	public boolean processEvent(Object arg0) {
		if(arg0 == null) 
			return false;
		if(arg0 instanceof ScheduleMsg) {
			//将对象转化为map
			Map<String, String> eventData = null;
				try {
					eventData = NMBeanUtils.getObjectFieldStr(arg0);
					eventData.put(ScheduleConstants.TASKOPERATORTYPE, ScheduleConstants.TASKOPERATORADD);
					TaskInfo taskInfo = TaskBuilder.buildTaskInfo(eventData);
					ScheduleFacade.addSchedule(taskInfo);
				} catch (NMBeanUtilsException e) {
					Log.error("事件接收，数据解析错误。接收到的内容为：" + eventData, e);
					return false;
				} catch (NMFormateException e) {
					Log.error("事件接收，数据解析错误。接收到的内容为：" + eventData, e);
					return false;
				}
		}
		return true;
	}

}
