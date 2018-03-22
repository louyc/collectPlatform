package com.neusoft.np.arsf.core.schedule.infra.constants;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App OSGi Services<br>
 * 功能描述: 服务对外声明参数<br>
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
public interface ScheduleDeclare {

	String BUNDLE_NAME = "[arsf.core.schedule]";

	String SCHEDULE_INCREMENTS = "SCHEDULE_INCREMENTS";

	String SCHEDULE_REMOVE = "SCHEDULE_REMOVE";
	
	String SCHEDULE_INCREMENTS_OBJ = "SCHEDULE_INCREMENTS_OBJ";
	
	String SCHEDULE_REMOVE_OBJ = "SCHEDULE_REMOVE_OBJ";

}
