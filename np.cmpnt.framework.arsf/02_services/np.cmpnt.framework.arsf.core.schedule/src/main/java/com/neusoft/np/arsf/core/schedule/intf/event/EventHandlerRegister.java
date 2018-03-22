package com.neusoft.np.arsf.core.schedule.intf.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.neusoft.np.arsf.base.bundle.BaseEventHandler;
import com.neusoft.np.arsf.core.schedule.intf.event.handler.ScheduleIncrements;
import com.neusoft.np.arsf.core.schedule.intf.event.handler.ScheduleIncrementsObj;
import com.neusoft.np.arsf.core.schedule.intf.event.handler.ScheduleRemove;
import com.neusoft.np.arsf.core.schedule.intf.event.handler.ScheduleRemoveObj;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App OSGi Services<br>
 * 功能描述: 本服务注册的EventHandler<br>
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
public class EventHandlerRegister {

	public static Collection<BaseEventHandler> getEventHandler() {
		List<BaseEventHandler> handlers = new ArrayList<BaseEventHandler>();
		handlers.add(new ScheduleIncrements());
		handlers.add(new ScheduleRemove());
		handlers.add(new ScheduleIncrementsObj());
		handlers.add(new ScheduleRemoveObj());
		return handlers;
	}

}
