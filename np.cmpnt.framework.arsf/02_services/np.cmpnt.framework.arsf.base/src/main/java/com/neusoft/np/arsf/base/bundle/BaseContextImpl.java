package com.neusoft.np.arsf.base.bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.osgi.framework.BundleContext;

import com.neusoft.np.arsf.base.bundle.event.NPEventInfo;
import com.neusoft.np.arsf.base.bundle.event.NPEventRegister;
import com.neusoft.np.arsf.common.util.NMOSGiServicePool;
import com.neusoft.np.arsf.common.util.NMServiceCentre;

/**
 * 
 * 项目名称: Netpatrol 5.1<br>
 * 模块名称: App Layer 采集处理平台<br>
 * 功能描述: 自定义服务上下文Context接口实现<br>
 * 创建日期: 2013年9月26日 <br>
 * 版权信息: Copyright (c) 2013<br>
 * 公司信息: 东软集团股份有限公司 电信事业部-网管产品与系统部<br> 
 * @author <a href="mailto: huangshk@neusoft.com">黄守凯</a>
 * @version v1.0
 * <pre>
 * 修改历史
 *   序号      日期          修改人       修改原因
 *    1    2013年9月26日       黄守凯        创建
 * </pre>
 */
public class BaseContextImpl implements BaseContext {

	private String customName;

	private BundleContext context;

	private NMOSGiServicePool servicePool;

	private NPEventRegister register;

	private ConcurrentMap<String, List<BaseEventHandler>> eventHandlers = new ConcurrentHashMap<String, List<BaseEventHandler>>();

	private NMServiceCentre serviceCentre;

	private BlockingQueue<NPEventInfo> eventQueue;

	private String shortName;

	protected static class BasePoolHolder {
		private static final BaseContextImpl INSTANCE = new BaseContextImpl();
	}

	private BaseContextImpl() {
	}

	public static BaseContextImpl getInstance() {
		return BasePoolHolder.INSTANCE;
	}

	@Override
	public String getBundleName() {
		if ("".equals(customName)) {
			if (context == null) {
				return null;
			}
			// return "Bundle_" + context.getBundle().getSymbolicName() + "_";
			return "Bundle: " + context.getBundle().getSymbolicName() + " ";
		} else {
			return "" + customName;
		}
	}

	@Override
	public String getSymbolicName() {
		return context.getBundle().getSymbolicName();
	}

	@Override
	public String getStortName() {
		if (shortName == null || "".equals(shortName)) {
			shortName = getSymbolicName().replaceFirst("com.neusoft.np.", "");
		}
		return shortName;
	}

	@Override
	public BundleContext getContext() {
		return context;
	}

	protected void setContext(BundleContext context) {
		this.context = context;
	}

	@Override
	public NMOSGiServicePool getServicePool() {
		return servicePool;
	}

	protected void setServicePool(NMOSGiServicePool servicePool) {
		this.servicePool = servicePool;
	}

	protected NPEventRegister getRegister() {
		return register;
	}

	protected void setRegister(NPEventRegister register) {
		this.register = register;
	}

	public BlockingQueue<NPEventInfo> getEventQueue() {
		return eventQueue;
	}

	protected void setEventQueue(BlockingQueue<NPEventInfo> eventQueue) {
		this.eventQueue = eventQueue;
	}

	// -------------------- 事件Event相关 -------------------- 

	public List<BaseEventHandler> getEventHandler(String topicName) {
		return eventHandlers.get(topicName);
	}

	public boolean isEventHandlerEmpty() {
		return eventHandlers.isEmpty();
	}

	protected void addEventHandler(BaseEventHandler handler) {
		if (!eventHandlers.containsKey(handler.getTopicName())) {
			eventHandlers.put(handler.getTopicName(), new ArrayList<BaseEventHandler>());
		}
		eventHandlers.get(handler.getTopicName()).add(handler);
	}

	protected void initEventHandler(Collection<BaseEventHandler> handlers) {
		for (BaseEventHandler handler : handlers) {
			addEventHandler(handler);
		}
	}

	@Override
	public NMServiceCentre getServiceCentre() {
		return serviceCentre;
	}

	protected void setServiceCentre(NMServiceCentre serviceCentre) {
		this.serviceCentre = serviceCentre;
	}

	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	@Override
	public String getSymbolicVersion() {
		return context.getBundle().getVersion().toString();
	}

	public void clear() {
		serviceCentre.removeServiceByName(getBundleName() + "NPEventListener");
		serviceCentre = null;
		eventHandlers = null;
	}

}
