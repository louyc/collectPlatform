package com.neusoft.np.arsf.db.infra.config;

import com.neusoft.np.arsf.base.bundle.BaseContext;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App Layer 采集&处理平台<br>
 * 功能描述: <br>
 * 创建日期: 2013年10月17日 <br>
 * 版权信息: Copyright (c) 2013<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2013年10月17日       黄守凯        创建
 * </pre>
 */
public class ClassContext {

	protected static class ClassContextHolder {
		private static final ClassContext INSTANCE = new ClassContext();
	}

	private ClassContext() {
	}

	public static ClassContext getContext() {
		return ClassContextHolder.INSTANCE;
	}

	private BaseContext baseContext;

	public BaseContext getBaseContext() {
		return baseContext;
	}

	public void setBaseContext(BaseContext baseContext) {
		this.baseContext = baseContext;
	}
}
