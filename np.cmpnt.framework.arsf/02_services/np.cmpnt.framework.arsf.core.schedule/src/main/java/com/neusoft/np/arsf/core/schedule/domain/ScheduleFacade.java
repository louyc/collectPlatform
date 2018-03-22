package com.neusoft.np.arsf.core.schedule.domain;

import com.neusoft.np.arsf.base.bundle.Log;
import com.neusoft.np.arsf.core.schedule.app.ScheduleControl;
import com.neusoft.np.arsf.core.schedule.quartz.TaskInfo;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App OSGi Services<br>
 * 功能描述: Facade接口<br>
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
public class ScheduleFacade {

	public static void addSchedule(TaskInfo taskInfo) {
		try {
			ScheduleControl.getInstance().getInput().put(taskInfo);
		} catch (InterruptedException e) {
			Log.error("Facade接口，添加任务失败", e);
		}
	}

	public static void delSchedule(TaskInfo taskInfo) {
		try {
			ScheduleControl.getInstance().getInput().put(taskInfo);
		} catch (InterruptedException e) {
			Log.error("Facade接口，删除任务失败", e);
		}
	}

}
